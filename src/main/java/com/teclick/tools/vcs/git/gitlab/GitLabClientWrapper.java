package com.teclick.tools.vcs.git.gitlab;

import com.teclick.tools.vcs.*;
import com.teclick.tools.vcs.git.GitException;
import com.teclick.tools.vcs.git.gitlab.entity.*;
import com.teclick.tools.vcs.utils.Zip;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Nelson on 2017-05-24.
 * GitLabClientWrapper
 */
public class GitLabClientWrapper implements VCS {

    private GitLabApi gitLabApi;

    private VCSContext context;

    public GitLabClientWrapper(VCSContext context) throws VCSException {
        try {
            GitLabApiClient gitLabApiClient = new GitLabApiClient(context, 10000);
            this.gitLabApi = gitLabApiClient.getGitLabApi();
        } catch (GitException e) {
            throw new VCSException("GitLabClientWrapper", e);
        }
        this.context = context;
    }

    @Override
    public List<VersionCommentItem> getVersionsAfterLastBuild(String project, String branch, String lastBuildVersion) throws VCSException {
        try {
            Integer projectId = Integer.parseInt(project);

            Date endDate = new Date();
            endDate.setTime(endDate.getTime() + 1000 * 60 * 10);
            String strEndDate = formatDateTimeWithISO8601(endDate);

            String strBeginDate = null;
            if ((null != lastBuildVersion) && (!lastBuildVersion.trim().equals(""))) {
                strBeginDate = getCommitDate(projectId, lastBuildVersion);
            }

            List<Commit> commits = gitLabApi.getCommits(projectId, branch, null, strBeginDate, strEndDate);
            List<VersionCommentItem> result = new ArrayList<>(commits.size());
            for (Commit commit : commits) {
                if (!commit.getId().equals(lastBuildVersion)) {
                    VersionCommentItem versionCommentItem = new VersionCommentItem();
                    versionCommentItem.setVersion(commit.getId());
                    versionCommentItem.setComments(commit.getMessage());
                    versionCommentItem.setCommittedDate(commit.getCreated_at());
                    result.add(versionCommentItem);
                }
            }

            return result;
        } catch (GitException e) {
            throw new VCSException("getVersionsAfterLastBuild", e);
        }
    }

    @Override
    public List<ProjectItem> listProjects(String groupName) throws VCSException {
        try {
            List<ProjectItem> result = new ArrayList<>();
            Group group = getGroup(groupName);
            if (null != group) {
                List<Project> projects = gitLabApi.getGroupProjects(group.getId());
                if (null != projects) {
                    for (Project project : projects) {
                        ProjectItem item = new ProjectItem();
                        item.setId(project.getId().toString());
                        item.setName(project.getName());
                        result.add(item);
                    }
                }
            }
            return result;
        } catch (GitException e) {
            throw new VCSException("listProjects", e);
        }
    }

    @Override
    public List<String> listProjectBranch(String project) throws VCSException {
        try {
            List<Branch> branches = gitLabApi.getBranches(Integer.parseInt(project));
            List<String> result = new ArrayList<>(branches.size());
            for (Branch branch : branches) {
                result.add(branch.getName());
            }
            return result;
        } catch (GitException e) {
            throw new VCSException("listProjectBranch", e);
        }
    }

    @Override
    public void checkout(File folder, String project, String branch, String version) throws VCSException {
        Response response = null;
        try {
            Integer projectId = Integer.parseInt(project);
            response = gitLabApi.getRepositoryArchive(projectId, version);
            try (InputStream in = response.readEntity(InputStream.class)) {
                Zip.unzip(in, folder, false);
                response.close();
            }
        } catch (GitException | IOException e) {
            if (null != response) {
                response.close();
            }
            throw new VCSException("checkout", e);
        }
    }

    @Override
    public VersionCommentItem getMaxVersion(String project, String branch, String subPath, String lastBuildVersion, String targetVersion) throws VCSException {
        try {
            Integer projectId = Integer.parseInt(project);

            String strBeginDate = null;

            if ((null != lastBuildVersion) && (lastBuildVersion.trim().equals(""))) {
                strBeginDate = getCommitDate(projectId, lastBuildVersion);
            }

            String strEndDate = getCommitDate(projectId, targetVersion);

            List<Commit> commits = gitLabApi.getCommits(projectId, branch, subPath, strBeginDate, strEndDate);

            VersionCommentItem versionCommentItem = new VersionCommentItem();
            if ((null != commits) && (commits.size() > 0)) {
                versionCommentItem.setVersion(commits.get(0).getId());
                versionCommentItem.setComments(commits.get(0).getMessage());
                versionCommentItem.setCommittedDate(commits.get(0).getCreated_at());
            }
            return versionCommentItem;
        } catch (GitException e) {
            throw new VCSException("getMaxVersion", e);
        }
    }

    @Override
    public void importToVcs(String groupName, File folder, String projectName, String branch) throws VCSException {
        try {
            String sudoUser = null;
            Group group = getGroup(groupName);
            if (null != group) {
                List<User> users = gitLabApi.getGroupMembers(group.getId());
                for (User user : users) {
                    if (user.getAccessLevel() == 50) {
                        sudoUser = user.getUsername();
                    }
                }
            } else {
                throw new GitException("Can not lookup group name " + groupName);
            }

            Namespace namespace = null;
            List<Namespace> namespaces = gitLabApi.searchNamespace(groupName);
            for (Namespace space : namespaces) {
                if (space.getName().equals(groupName)) {
                    namespace = space;
                    break;
                }
            }

            if (null != namespace) {
                gitLabApi.addProject(projectName, namespace.getId(), "Project initialize ...", false, sudoUser);
            } else {
                throw new GitException("Group name not found");
            }
        } catch (GitException e) {
            throw new VCSException("importToVcs:searchNamespace", e);
        }
    }

    @Override
    public void createBranch(String project, String branch, String newBranch) throws VCSException {
    }

    @Override
    public VCSContext getContext() {
        return context;
    }


    //=====================================================================================
    // Group & User
    private User getUser(String account) {
        User result = null;
        List<User> users = gitLabApi.getUsers(account);
        for (User user : users) {
            if (user.getUsername().equals(account)) {
                result = user;
                break;
            }
        }
        return result;
    }

    private Group getGroup(String groupName) {
        List<Group> groups = gitLabApi.getGroups(groupName);
        Group result = null;
        for (Group group : groups) {
            if (group.getName().toLowerCase().equals(groupName.toLowerCase())) {
                result = group;
                break;
            }
        }
        return result;
    }

    @Override
    public void addGroup(String name, String description, String sudoUser) throws VCSException {
        try {
            Group group = getGroup(name);
            if (null == group) {
                gitLabApi.addGroup(name, name, description, sudoUser);
            } else {
                throw new GitException("Group name [" + name + "] already exists");
            }
        } catch (Exception e) {
            throw new VCSException("addGroup", e);
        }
    }

    @Override
    public void delGroup(String name) throws VCSException {
        try {
            Group group = getGroup(name);
            if (null != group) {
                gitLabApi.delGroup(group.getId());
            } else {
                throw new GitException("Group name [" + name + "] not found");
            }
        } catch (Exception e) {
            throw new VCSException("delGroup", e);
        }
    }

    @Override
    public boolean groupExists(String name) {
        return getGroup(name) != null;
    }

    /**
     * @param account login account
     * @param groupName group name
     * @param accessLevel
         10 => Guest access
         20 => Reporter access
         30 => Developer access
         40 => Master access
         50 => Owner access # Only valid for groups
     * @throws VCSException
     */
    @Override
    public void addGroupUser(String account, String groupName, int accessLevel) throws VCSException {
        try {
            Group group = getGroup(groupName);
            User user = getUser(account);
            if ((null != group) && (null != user)) {
                gitLabApi.addGroupMember(group.getId(), user.getId(), accessLevel);
            } else {
                throw new GitException("Can not lookup "
                        + (null == group ? "group name [" + groupName + "] " : "")
                        + (null == user ? "user name [" + account + "] " : ""));
            }
        } catch (Exception e) {
            throw new VCSException("addGroupUser", e);
        }
    }

    @Override
    public void setGroupUser(String account, String groupName, int accessLevel) throws VCSException {
        try {
            Group group = getGroup(groupName);
            User user = getUser(account);
            if ((null != group) && (null != user)) {
                gitLabApi.setGroupMembers(group.getId(), user.getId(), accessLevel);
            } else {
                throw new GitException("Can not lookup "
                        + (null == group ? "group name [" + groupName + "] " : "")
                        + (null == user ? "user name [" + account + "] " : ""));
            }
        } catch (Exception e) {
            throw new VCSException("setGroupUser", e);
        }
    }

    @Override
    public void delGroupUser(String account, String groupName) throws VCSException {
        try {
            Group group = getGroup(groupName);
            User user = getUser(account);
            if ((null != group) && (null != user)) {
                gitLabApi.delGroupMembers(group.getId(), user.getId());
            } else {
                throw new GitException("Can not lookup "
                        + (null == group ? "group name [" + groupName + "] " : "")
                        + (null == user ? "user name [" + account + "] " : ""));
            }
        } catch (Exception e) {
            throw new VCSException("delGroupUser", e);
        }
    }

    @Override
    public boolean isUserInGroup(String account, String groupName) throws VCSException {
        try {
            Group group = getGroup(groupName);
            User user = getUser(account);
            if ((null != group) && (null != user)) {
                User U = gitLabApi.getGroupMember(group.getId(), user.getId());
                return null != U;
            }
            return false;
        } catch (WebApplicationException e) {
            return false;
        } catch (Exception e) {
            throw new VCSException("isUserInGroup", e);
        }
    }

    @Override
    public boolean userExists(String account) {
        List<User> users = gitLabApi.getUsers(account);
        for (User user : users) {
            if (user.getUsername().equals(account)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void changeUserPermission(String account, boolean canCreateGroup, boolean external) throws VCSException {
        try {
            User user = getUser(account);
            if (null != user) {
                gitLabApi.changeMemberPermission(user.getId(), canCreateGroup, external);
            } else {
                throw new GitException("Can not lookup user: " + account);
            }
        } catch (Exception e) {
            throw new VCSException("changeUserPermission", e);
        }
    }

    private String formatDateTimeWithISO8601(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, -8);
        Date time = calendar.getTime();
        SimpleDateFormat ISO8601DateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        return ISO8601DateFormat.format(time);
    }

    private String getCommitDate(Integer projectId, String targetVersion) throws GitException {
        Commit commitTargetBuild = gitLabApi.getCommits(projectId, targetVersion);
        Date endDate = commitTargetBuild.getCreated_at();
        return formatDateTimeWithISO8601(endDate);
    }

/*    private String getFileName(MultivaluedMap<String, String> header) {
        String[] contentDisposition = header.getFirst("Content-Disposition").split(";");
        for (String filename : contentDisposition) {
            if ((filename.trim().startsWith("filename"))) {
                String[] name = filename.split("=");
                return name[1].trim().replaceAll("\"", "");
            }
        }
        return "unknown";
    }*/
}

package com.teclick.tools.vcs.git;

import com.teclick.tools.vcs.*;
import com.teclick.tools.vcs.git.gitlab.*;
import com.teclick.tools.vcs.git.gitlab.entity.Branch;
import com.teclick.tools.vcs.git.gitlab.entity.Commit;
import com.teclick.tools.vcs.git.gitlab.entity.Project;
import com.teclick.tools.vcs.utils.Zip;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Nelson on 2017-05-24.
 * GitClientWrapper
 */
public class GitClientWrapper implements VCS {

    //private String gitRoot;

    private GitlabApiClient gitlabApiClient;

    private VCSContext context;

    public GitClientWrapper(VCSContext context) throws VCSException {
        try {
            //this.gitRoot = context.getRootPath();
            this.gitlabApiClient = new GitlabApiClient(context.getRootPath(), context.getAccount(), context.getPassword(), 10000);
        } catch (GitlabException e) {
            throw new VCSException("GitClientWrapper", e);
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

            List<Commit> commits = gitlabApiClient.getCommits(projectId, branch, null, strBeginDate, strEndDate);
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
        } catch (GitlabException e) {
            throw new VCSException("getVersionsAfterLastBuild", e);
        }
    }

    @Override
    public List<ProjectItem> listProjects(String groupName) throws VCSException {
        try {
            List<Project> projects = gitlabApiClient.getProjects();
            List<ProjectItem> result = new ArrayList<>(projects.size());
            for (Project project : projects) {
                if (project.getNamespace().getName().equals(groupName)) {
                    ProjectItem item = new ProjectItem();
                    item.setId(project.getId().toString());
                    item.setName(project.getName());
                    result.add(item);
                }
            }
            return result;
        } catch (GitlabException e) {
            throw new VCSException("listProjects", e);
        }
    }

    @Override
    public List<String> listProjectBranch(String project) throws VCSException {
        try {
            List<Branch> branches = gitlabApiClient.getBranches(Integer.parseInt(project));
            List<String> result = new ArrayList<>(branches.size());
            for (Branch branch : branches) {
                result.add(branch.getName());
            }
            return result;
        } catch (GitlabException e) {
            throw new VCSException("listProjectBranch", e);
        }
    }

    @Override
    public void checkout(File folder, String project, String branch, String version) throws VCSException {
        Response response = null;
        try {
            Integer projectId = Integer.parseInt(project);
            response = gitlabApiClient.getRepositoryArchive(projectId, version);
            try (InputStream in = response.readEntity(InputStream.class)) {
                Zip.unzip(in, folder, false);
                response.close();
            }
        } catch (GitlabException | IOException e) {
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

            List<Commit> commits = gitlabApiClient.getCommits(projectId, branch, subPath, strBeginDate, strEndDate);

            VersionCommentItem versionCommentItem = new VersionCommentItem();
            if ((null != commits) && (commits.size() > 0)) {
                versionCommentItem.setVersion(commits.get(0).getId());
                versionCommentItem.setComments(commits.get(0).getMessage());
                versionCommentItem.setCommittedDate(commits.get(0).getCreated_at());
            }
            return versionCommentItem;
        } catch (GitlabException e) {
            throw new VCSException("getMaxVersion", e);
        }
    }

    @Override
    public void importToSVN(String groupName, File folder, String projectName, String branch) throws VCSException {

    }

    @Override
    public void createBranch(String project, String branch, String newBranch) throws VCSException {

    }

    @Override
    public VCSContext getContext() {
        return context;
    }

    private String formatDateTimeWithISO8601(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, -8);
        Date time = calendar.getTime();
        SimpleDateFormat ISO8601DateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        return ISO8601DateFormat.format(time);
    }

    private String getCommitDate(Integer projectId, String targetVersion) throws GitlabException {
        Commit commitTargetBuild = gitlabApiClient.getCommits(projectId, targetVersion);
        Date endDate = commitTargetBuild.getCreated_at();
        return formatDateTimeWithISO8601(endDate);
    }

    private String getFileName(MultivaluedMap<String, String> header) {
        String[] contentDisposition = header.getFirst("Content-Disposition").split(";");
        for (String filename : contentDisposition) {
            if ((filename.trim().startsWith("filename"))) {
                String[] name = filename.split("=");
                return name[1].trim().replaceAll("\"", "");
            }
        }
        return "unknown";
    }
}

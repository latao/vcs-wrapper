package com.teclick.tools.vcs.svn;

import com.teclick.tools.vcs.*;
import org.tmatesoft.svn.core.*;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNCopySource;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by Nelson on 2015/3/23.
 * SVN access class
 */
public class SVNClientWrapper implements Closeable, VCS {

    private String svnRoot;

    private SVNClientManager svnClientManager;

    private VCSContext context;

    public SVNClientWrapper(VCSContext context) throws VCSException {
        try {
            this.svnRoot = SVNURL.parseURIEncoded(context.getRootPath()).toDecodedString();
            svnClientManager = SVNClientManager.newInstance(SVNWCUtil.createDefaultOptions(true), context.getAccount(), context.getPassword());
        } catch (SVNException e) {
            throw new VCSException("SVNClientWrapper", e);
        }
        this.context = context;
    }

    @Override
    public void checkout(File Folder, String project, String branch, String version) throws VCSException {

        String projectSvnRoot = getProjectSvnRoot(project, branch);
        try {
            SVNURL svnurl = SVNURL.parseURIEncoded(projectSvnRoot);
            svnClientManager.getUpdateClient().doCheckout(svnurl, Folder, SVNRevision.HEAD, SVNRevision.create(Long.parseLong(version)), SVNDepth.fromRecurse(true), false);
        } catch (SVNException e) {
            throw new VCSException("Get code from SVN: " + projectSvnRoot, e);
        }
    }

    @Override
    public VersionCommentItem getMaxVersion(String project, String branch, String subPath, String lastBuildVersion, final String maxVersion) throws VCSException {

        String projectSvnRoot = getProjectSvnRoot(project, branch);

        final VersionCommentItem result = new VersionCommentItem();
        try {
            SVNURL svnurl = SVNURL.parseURIEncoded(projectSvnRoot + "/" + subPath.replace('\\', '/'));
            SVNRepository repository = svnClientManager.createRepository(svnurl, false);

            repository.log(
                    new String[]{},
                    Long.parseLong(lastBuildVersion), -1,
                    false, false,
                    new ISVNLogEntryHandler() {
                        @Override
                        public void handleLogEntry(SVNLogEntry logEntry) {
                            if (logEntry.getRevision() <= Long.parseLong(maxVersion)) {
                                result.setComments(logEntry.getMessage());
                                result.setVersion(String.valueOf(logEntry.getRevision()));
                                result.setCommittedDate(logEntry.getDate());
                            }
                        }
                    }
            );
        } catch (SVNException e) {
            throw new VCSException("", e);
        }

        return result;
    }

    @Override
    public List<VersionCommentItem> getVersionsAfterLastBuild(String project, String branch, String lastBuildVersion) throws VCSException {

        String projectSvnRoot = getProjectSvnRoot(project, branch);

        final long lastBuildVersionId = Long.parseLong(lastBuildVersion);

        final List<VersionCommentItem> result = new ArrayList<>();
        try {
            SVNURL svnurl = SVNURL.parseURIEncoded(projectSvnRoot);
            SVNRepository repository = svnClientManager.createRepository(svnurl, false);

            repository.log(
                    new String[]{},
                    lastBuildVersionId, -1,
                    false, false,
                    new ISVNLogEntryHandler() {
                        @Override
                        public void handleLogEntry(SVNLogEntry logEntry) {
                            if (logEntry.getRevision() != lastBuildVersionId) {
                                VersionCommentItem info = new VersionCommentItem();
                                info.setVersion(String.valueOf(logEntry.getRevision()));
                                info.setComments(logEntry.getMessage());
                                info.setCommittedDate(logEntry.getDate());
                                result.add(info);
                            }
                        }
                    }
            );
        } catch (SVNException e) {
            throw new VCSException("getVersionsAfterLastBuild", e);
        }

        return result;
    }

    @SuppressWarnings("WhileLoopReplaceableByForEach")
    @Override
    public List<ProjectItem> listProjects(String groupName) throws VCSException {
        try {
            SVNURL svnurl = SVNURL.parseURIEncoded(svnRoot);
            SVNRepository repository = svnClientManager.createRepository(svnurl, false);
            Collection entries = repository.getDir("/", -1, null, (Collection) null);

            List<ProjectItem> result = new ArrayList<>(entries.size());

            Iterator iterator = entries.iterator();
            while (iterator.hasNext()) {
                SVNDirEntry entry = (SVNDirEntry) iterator.next();
                if (entry.getKind() == SVNNodeKind.DIR) {
                    ProjectItem projectItem = new ProjectItem();
                    projectItem.setName(entry.getName());
                    projectItem.setId("0");
                    result.add(projectItem);
                }
            }

            Collections.sort(result, new Comparator<ProjectItem>() {
                @Override
                public int compare(ProjectItem o1, ProjectItem o2) {
                    return o1.getName().compareToIgnoreCase(o2.getName());
                }
            });

            return result;

        } catch (SVNException e) {
            throw new VCSException("listProjects", e);
        }
    }

    @SuppressWarnings("WhileLoopReplaceableByForEach")
    @Override
    public List<String> listProjectBranch(String project) throws VCSException {
        try {
            SVNURL svnurl = SVNURL.parseURIEncoded(svnRoot);
            SVNRepository repository = svnClientManager.createRepository(svnurl, false);
            Collection entries = repository.getDir(project, -1, null, (Collection) null);

            List<String> result = new ArrayList<>(entries.size());

            Iterator iterator = entries.iterator();
            while (iterator.hasNext()) {
                SVNDirEntry entry = (SVNDirEntry) iterator.next();
                if (entry.getKind() == SVNNodeKind.DIR) {
                    String branchFullName = entry.getName();
                    String branchPreFix = project + "-";
                    if (branchFullName.startsWith(branchPreFix) && (branchFullName.length() > branchPreFix.length())) {
                        result.add(branchFullName.substring(branchPreFix.length()));
                    }
                }
            }

            return result;
        } catch (SVNException e) {
            throw new VCSException("listProjectBranch", e);
        }
    }

    @Override
    public void importToVcs(String groupName, File folder, String projectName, String branch) throws VCSException {
        String projectSvnRoot = getProjectSvnRoot(projectName, branch);
        try {
            SVNURL svnurl = SVNURL.parseURIEncoded(projectSvnRoot);
            svnClientManager.getCommitClient().doImport(folder, svnurl, "Project Initialize", null, true, true, SVNDepth.fromRecurse(true));
        } catch (SVNException e) {
            throw new VCSException("importToVcs", e);
        }
    }

    @Override
    public void createBranch(String project, String branch, String newBranch) throws VCSException {

        String projectSvnRoot = getProjectSvnRoot(project, branch);
        String newPrjSvnRoot = getProjectSvnRoot(project, newBranch);
        try {
            SVNURL srcURL = SVNURL.parseURIEncoded(projectSvnRoot);
            SVNURL desURL = SVNURL.parseURIEncoded(newPrjSvnRoot);
            SVNCopySource copySource = new SVNCopySource(SVNRevision.UNDEFINED, SVNRevision.HEAD, srcURL);
            svnClientManager.getCopyClient().doCopy(new SVNCopySource[]{copySource}, desURL, false, true, true, "new branch from " + projectSvnRoot, null);
        } catch (SVNException e) {
            throw new VCSException("createBranch", e);
        }
    }

    @Override
    public void close() throws IOException {
        svnClientManager.dispose();
    }

    @Override
    public VCSContext getContext() {
        return context;
    }

    @Override
    public void addGroup(String name, String description, String sudoUser) {
    }

    @Override
    public void delGroup(String name) throws VCSException {
    }

    @Override
    public boolean groupExists(String name) {
        return true;
    }

    @Override
    public void addGroupUser(String account, String groupName, int accessLevel) throws VCSException {

    }

    @Override
    public void setGroupUser(String account, String groupName, int accessLevel) throws VCSException {

    }

    @Override
    public void delGroupUser(String account, String groupName) throws VCSException {

    }

    @Override
    public boolean isUserInGroup(String account, String group) {
        return false;
    }

    @Override
    public boolean userExists(String account) {
        return false;
    }

    @Override
    public void changeUserPermission(String account, boolean canCreateGroup, boolean external) throws VCSException {

    }

    private String getProjectSvnRoot(String project, String branch) {
        if ((null == branch) || (branch.isEmpty())) {
            return svnRoot.concat(project);
        } else {
            return svnRoot.concat("/").concat(project).concat("/").concat(project).concat("-").concat(branch);
        }
    }

}

package com.teclick.tools.vcs.git;

import com.teclick.tools.vcs.*;

import java.io.File;
import java.util.List;

/**
 * Created by Nelson on 2017-06-02 14:38.
 * BitbucketClientWrapper
 */
public class BitbucketClientWrapper implements VCS {

    private VCSContext context;

    public BitbucketClientWrapper(VCSContext context) throws VCSException {

        this.context = context;
    }

    @Override
    public List<ProjectItem> listProjects(String groupName) throws VCSException {
        return null;
    }

    @Override
    public List<String> listProjectBranch(String project) throws VCSException {
        return null;
    }

    @Override
    public List<VersionCommentItem> getVersionsAfterLastBuild(String project, String branch, String lastBuildVersion) throws VCSException {
        return null;
    }

    @Override
    public void checkout(File Folder, String project, String branch, String version) throws VCSException {

    }

    @Override
    public VersionCommentItem getMaxVersion(String project, String branch, String module, String lastBuildVersion, String maxVersion) throws VCSException {
        return null;
    }

    @Override
    public void importToVcs(String groupName, File folder, String projectName, String branch) throws VCSException {

    }

    @Override
    public void createBranch(String project, String branch, String newBranch) throws VCSException {

    }

    @Override
    public VCSContext getContext() {
        return null;
    }
}

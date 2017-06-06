package com.teclick.tools.vcs.git.bitbucket;

import com.teclick.tools.vcs.*;
import com.teclick.tools.vcs.git.GitException;

import java.io.File;
import java.util.List;

/**
 * Created by Nelson on 2017-06-02 14:38.
 * BitBucketClientWrapper
 */
public class BitBucketClientWrapper implements VCS {

    private VCSContext context;

    private BitBucketApi bitBucketApi;

    public BitBucketClientWrapper(VCSContext context) throws VCSException {
        try {
            BitBucketApiClient bitBucketApiClient = new BitBucketApiClient(context.getRootPath(), context.getAccount(), context.getPassword(), 10000);
            this.bitBucketApi = bitBucketApiClient.getBitBucketApi();
        } catch (GitException e) {
            throw new VCSException("BitBucketClientWrapper", e);
        }
        this.context = context;
    }

    @Override
    public List<ProjectItem> listProjects(String groupName) throws VCSException {
        bitBucketApi.getUserRepositories();
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
        return context;
    }

    @Override
    public int addGroup(String name, String description) {
        return 0;
    }

    @Override
    public boolean groupExists(String groupName) {
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
    public boolean groupUserExists(String account, String group) {
        return false;
    }

    @Override
    public boolean userExists(String account) {
        return false;
    }

    @Override
    public void changeUserPermission(String account, boolean canCreateGroup, boolean external) throws VCSException {

    }
}

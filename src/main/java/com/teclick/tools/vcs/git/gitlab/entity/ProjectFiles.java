package com.teclick.tools.vcs.git.gitlab.entity;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nelson on 2017-06-09 14:09.
 * ProjectFiles
 */
public class ProjectFiles {

    public ProjectFiles(String branch, String commitMessage) {
        this.branch = branch;
        this.commit_message = commitMessage;
    }

    public ProjectFiles(String commitMessage) {
        this("master", commitMessage);
    }

    public void loadProjectFiles(File folder) throws IOException {
        String basePath = folder.getCanonicalPath();
        lookupFiles(basePath, folder);
    }

    private void lookupFiles(String basePath, File folder) throws IOException {
        File[] files = folder.listFiles();
        if (null != files) {
            for (File file : files) {
                if (file.isDirectory()) {
                    lookupFiles(basePath, file);
                } else {
                    addFile(basePath, file);
                }
            }
        }
    }

    private void addFile(String basePath, File file) throws IOException {
        String filePath = file.getCanonicalPath().substring(basePath.length()).replace("\\", "/");
        String content = FileUtils.readFileToString(file, Charset.forName("UTF-8"));

        ProjectFile projectFile = new ProjectFile("create", filePath, content);

        actions.add(projectFile);
    }

    private String branch;
    private String commit_message;
    private List<ProjectFile> actions = new ArrayList<>();

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getCommit_message() {
        return commit_message;
    }

    public void setCommit_message(String commit_message) {
        this.commit_message = commit_message;
    }

    public List<ProjectFile> getActions() {
        return actions;
    }

    public void setActions(List<ProjectFile> actions) {
        this.actions = actions;
    }
}

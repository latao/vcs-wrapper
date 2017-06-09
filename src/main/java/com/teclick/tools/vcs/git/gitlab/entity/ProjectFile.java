package com.teclick.tools.vcs.git.gitlab.entity;

/**
 * Created by Nelson on 2017-06-09 14:07.
 * ProjectFile
 */
public class ProjectFile {

    public ProjectFile(String action, String filePath, String content) {
        this.action = action;
        this.file_path = filePath;
        this.content = content;
    }

    private String action;
    private String file_path;
    private String content;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

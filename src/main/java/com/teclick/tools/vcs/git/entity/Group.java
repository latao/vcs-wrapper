package com.teclick.tools.vcs.git.entity;

/**
 * Created by Nelson on 2017-06-06 10:01.
 * Group
 */
public class Group {

    private int id;

    private String name;

    private String path;

    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

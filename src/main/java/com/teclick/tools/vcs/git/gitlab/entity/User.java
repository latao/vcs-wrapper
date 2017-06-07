package com.teclick.tools.vcs.git.gitlab.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Nelson on 2017-06-06 10:39.
 * User
 */
public class User {

    private int id;

    private String username;

    private String name;

    private String email;

    @JsonProperty("access_level")
    private int accessLevel;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(int accessLevel) {
        this.accessLevel = accessLevel;
    }
}

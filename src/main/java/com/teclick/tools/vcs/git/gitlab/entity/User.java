package com.teclick.tools.vcs.git.gitlab.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Nelson on 2017-06-06 10:39.
 * User
 */
public class User {
    // common field
    private String username;

    // git lab field
    private int id;
    private String name;
    private String email;

    // bit bucket field
    private String uuid;
    private String type;

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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

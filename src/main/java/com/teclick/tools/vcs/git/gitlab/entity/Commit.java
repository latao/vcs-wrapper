package com.teclick.tools.vcs.git.gitlab.entity;

import java.util.Date;
import java.util.List;

/**
 * Created by Nelson on 2017-06-01 21:45.
 * Commit
 */
public class Commit {

    private Date authored_date;
    private String author_email;
    private String author_name;
    private Date committed_date;
    private Date created_at;
    private String id;
    private String message;
    private List<String> parent_ids;
    private String short_id;
    private String status;
    private String title;

    public Date getAuthored_date() {
        return authored_date;
    }

    public void setAuthored_date(Date authored_date) {
        this.authored_date = authored_date;
    }

    public String getAuthor_email() {
        return author_email;
    }

    public void setAuthor_email(String author_email) {
        this.author_email = author_email;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public Date getCommitted_date() {
        return committed_date;
    }

    public void setCommitted_date(Date committed_date) {
        this.committed_date = committed_date;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getParent_ids() {
        return parent_ids;
    }

    public void setParent_ids(List<String> parent_ids) {
        this.parent_ids = parent_ids;
    }

    public String getShort_id() {
        return short_id;
    }

    public void setShort_id(String short_id) {
        this.short_id = short_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}

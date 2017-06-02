package com.teclick.tools.vcs;

import java.util.Date;

/**
 * Created by Nelson on 2015/3/26.
 * VersionCommentItem
 */
public class VersionCommentItem {

    private String version = null;

    private String comments = null;

    private Date committedDate = null;

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Date getCommittedDate() {
        return committedDate;
    }

    public void setCommittedDate(Date committedDate) {
        this.committedDate = committedDate;
    }
}

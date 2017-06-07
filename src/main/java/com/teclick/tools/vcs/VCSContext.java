package com.teclick.tools.vcs;

/**
 * Created by Nelson on 2015/3/26.
 * VCSContext
 */
public class VCSContext {

    public VCSContext() {
    }

    public VCSContext(String rootPath, String account, String password) {
        this.rootPath = rootPath;
        this.account = account;
        this.password = password;
    }

    public VCSContext(String rootPath, String privateToken) {
        this.rootPath = rootPath;
        this.privateToken = privateToken;
    }

    private String rootPath;
    private String account;
    private String password;
    private String privateToken = null;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public String getPrivateToken() {
        return privateToken;
    }

    public void setPrivateToken(String privateToken) {
        this.privateToken = privateToken;
    }
}

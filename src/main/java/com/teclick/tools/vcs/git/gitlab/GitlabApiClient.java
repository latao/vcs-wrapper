package com.teclick.tools.vcs.git.gitlab;

import com.teclick.tools.vcs.git.GitClientBase;
import com.teclick.tools.vcs.git.GitException;
import com.teclick.tools.vcs.git.gitlab.entity.Session;
import org.apache.cxf.jaxrs.client.WebClient;

/**
 * Created by Nelson on 2017-05-31 14:04.
 * GitLabApiClient
 */
public class GitLabApiClient extends GitClientBase {

    public GitLabApi getGitLabApi() {
        return gitLabApi;
    }

    private GitLabApi gitLabApi;

    @SuppressWarnings("unchecked")
    public GitLabApiClient(String url, String account, String password) throws GitException {
        super(url, account, password, GitLabApi.class);
    }

    @SuppressWarnings("unchecked")
    public GitLabApiClient(String url, String account, String password, int timeoutInSecond) throws GitException {
        super(url, account, password, timeoutInSecond, GitLabApi.class);
    }

    @Override
    protected void addAuthorizationHeader(String account, String password) throws GitException {
        gitLabApi = (GitLabApi) client;
        Session session = gitLabApi.login(account, password);
        WebClient.client(gitLabApi).header("PRIVATE-TOKEN", session.getPrivate_token());
    }

}

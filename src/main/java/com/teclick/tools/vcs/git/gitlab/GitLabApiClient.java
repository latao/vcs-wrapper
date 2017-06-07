package com.teclick.tools.vcs.git.gitlab;

import com.teclick.tools.vcs.VCSContext;
import com.teclick.tools.vcs.git.GitClientBase;
import com.teclick.tools.vcs.git.GitException;
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
    public GitLabApiClient(VCSContext context) throws GitException {
        super(context, GitLabApi.class);
    }

    @SuppressWarnings("unchecked")
    public GitLabApiClient(VCSContext context, int timeoutInSecond) throws GitException {
        super(context, timeoutInSecond, GitLabApi.class);
    }

    @Override
    protected void addAuthorizationHeader(VCSContext context) throws GitException {
        gitLabApi = (GitLabApi) client;
        WebClient.client(gitLabApi).header("PRIVATE-TOKEN", context.getPrivateToken());
    }

}

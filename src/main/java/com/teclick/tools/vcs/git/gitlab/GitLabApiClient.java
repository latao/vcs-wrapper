package com.teclick.tools.vcs.git.gitlab;

import com.teclick.tools.vcs.VCSContext;
import com.teclick.tools.vcs.git.GitClientBase;
import com.teclick.tools.vcs.git.GitException;
import org.apache.cxf.jaxrs.client.WebClient;

import java.util.Collections;

/**
 * Created by Nelson on 2017-05-31 14:04.
 * GitLabApiClient
 */
public class GitLabApiClient extends GitClientBase {

    public GitLabApi getGitLabApi() {
        return (GitLabApi) client;
    }

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
        bean.setHeaders(Collections.singletonMap("PRIVATE-TOKEN", context.getPrivateToken()));
    }

}

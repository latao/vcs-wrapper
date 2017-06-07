package com.teclick.tools.vcs.git.bitbucket;

import com.teclick.tools.vcs.VCSContext;
import com.teclick.tools.vcs.git.GitClientBase;
import com.teclick.tools.vcs.git.GitException;
import org.apache.cxf.jaxrs.client.WebClient;

/**
 * Created by Nelson on 2017-06-02 20:10.
 * BitBucketApiClient
 */
public class BitBucketApiClient extends GitClientBase {

    public BitBucketApi getBitBucketApi() {
        return (BitBucketApi) client;
    }

    @SuppressWarnings("unchecked")
    public BitBucketApiClient(VCSContext context) throws GitException {
        super(context, BitBucketApi.class);
    }

    @SuppressWarnings("unchecked")
    public BitBucketApiClient(VCSContext context, int timeoutInSecond) throws GitException {
        super(context, timeoutInSecond, BitBucketApi.class);
    }

    @Override
    protected void addAuthorizationHeader(VCSContext context) {
        bean.setUsername(context.getAccount());
        bean.setPassword(context.getPassword());
    }

}

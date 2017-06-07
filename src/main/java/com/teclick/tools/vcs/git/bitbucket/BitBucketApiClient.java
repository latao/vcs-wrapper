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
        return bitBucketApi;
    }

    private BitBucketApi bitBucketApi;

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
        bitBucketApi = (BitBucketApi) client;
        String authorizationHeader = "Basic " + org.apache.cxf.common.util.Base64Utility.encode((context.getAccount() + ":" + context.getPassword()).getBytes());
        WebClient.client(client).header("Authorization", authorizationHeader);
    }

}

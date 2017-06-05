package com.teclick.tools.vcs.git.bitbucket;

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
    public BitBucketApiClient(String url, String account, String password) throws GitException {
        super(url, account, password, BitBucketApi.class);
    }

    @SuppressWarnings("unchecked")
    public BitBucketApiClient(String url, String account, String password, int timeoutInSecond) throws GitException {
        super(url, account, password, timeoutInSecond, BitBucketApi.class);
    }

    @Override
    protected void addAuthorizationHeader(String account, String password) {
        bitBucketApi = (BitBucketApi) client;
        String authorizationHeader = "Basic " + org.apache.cxf.common.util.Base64Utility.encode((account + ":" + password).getBytes());
        WebClient.client(client).header("Authorization", authorizationHeader);
    }

}

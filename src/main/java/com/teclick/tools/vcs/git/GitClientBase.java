package com.teclick.tools.vcs.git;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Nelson on 2017-06-02 20:52.
 * GitClientBase
 */
public abstract class GitClientBase<T> {

    protected T client;

    public GitClientBase(String url, String account, String password, Class<T> clazz) throws GitException {
        JacksonJsonProvider jsonProvider = new JacksonJsonProvider();
        jsonProvider.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        RestExceptionHandler exceptionHandler = new RestExceptionHandler();
        List<Object> providers = new ArrayList<>(2);
        providers.add(jsonProvider);
        providers.add(exceptionHandler);

        client = JAXRSClientFactory.create(url, clazz, providers, true);

        if (System.getProperty("debug", "").equals("true")) {
            WebClient.getConfig(client).getInInterceptors().add(new LoggingInInterceptor());
            WebClient.getConfig(client).getOutInterceptors().add(new LoggingOutInterceptor());
        }

        addAuthorizationHeader(account, password);
    }

    public GitClientBase(String url, String account, String password, int timeoutInSecond, Class<T> clazz) throws GitException {
        this(url, account, password, clazz);

        HTTPConduit conduit = WebClient.getConfig(client).getHttpConduit();
        HTTPClientPolicy policy = new HTTPClientPolicy();
        policy.setReceiveTimeout(timeoutInSecond * 1000);
        conduit.setClient(policy);
    }

    protected abstract void addAuthorizationHeader(String account, String password) throws GitException;
}

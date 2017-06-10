package com.teclick.tools.vcs.git;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.teclick.tools.vcs.VCSContext;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxrs.client.JAXRSClientFactoryBean;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;

/**
 * Created by Nelson on 2017-06-02 20:52.
 * GitClientBase
 */
public abstract class GitClientBase<T> {

    protected T client;

    protected JAXRSClientFactoryBean bean;

    public GitClientBase(VCSContext context, Class<T> clazz) throws GitException {
        createClient(context, clazz);
    }

    public GitClientBase(VCSContext context, Class<T> clazz, int timeoutInSecond) throws GitException {
        this(context, clazz);

        HTTPConduit conduit = WebClient.getConfig(client).getHttpConduit();
        HTTPClientPolicy policy = new HTTPClientPolicy();
        policy.setReceiveTimeout(timeoutInSecond * 1000);
        conduit.setClient(policy);
    }

    protected abstract void addAuthorizationHeader(VCSContext context) throws GitException;

    private void createClient(VCSContext context, Class<T> cls) throws GitException {
        bean = new JAXRSClientFactoryBean();
        bean.setThreadSafe(true);
        bean.setServiceClass(cls);
        bean.setAddress(context.getRootPath());

        JacksonJsonProvider jsonProvider = new JacksonJsonProvider();
        jsonProvider.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        bean.setProvider(jsonProvider);

        bean.getInInterceptors().add(new ResponseHeadersInterceptor());

        if (System.getProperty("debug", "").equals("true")) {
            bean.getInInterceptors().add(new LoggingInInterceptor());
            bean.getOutInterceptors().add(new LoggingOutInterceptor());
        }

        addAuthorizationHeader(context);

        client = bean.create(cls);
    }
}

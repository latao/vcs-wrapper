package com.teclick.tools.vcs.git;

import org.apache.cxf.jaxrs.client.ResponseExceptionMapper;

import javax.ws.rs.core.Response;

/**
 * Created by lipeng on 2017-06-05 21:00.
 * RestExceptionHandler
 */
public class RestExceptionHandler implements ResponseExceptionMapper {

    @Override
    public Throwable fromResponse(Response response) {
        return new GitException("REST Call return error", new Exception("sdfkljas"));
    }
}

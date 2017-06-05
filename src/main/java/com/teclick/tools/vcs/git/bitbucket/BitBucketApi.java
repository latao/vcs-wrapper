package com.teclick.tools.vcs.git.bitbucket;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by Nelson on 2017-06-02 20:05.
 * BitBucketApi
 */
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON + ";charset=UTF-8"})
public interface BitBucketApi {

    @GET
    @Path("user/repositories")
    void getUserRepositories();

    @GET
    @Path("repositories/{username}")
    void getUserRepositories(@PathParam("username") String userName);
}

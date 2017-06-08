package com.teclick.tools.vcs.git.bitbucket;

import com.teclick.tools.vcs.git.entity.Group;
import com.teclick.tools.vcs.git.entity.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by Nelson on 2017-06-02 20:05.
 * BitBucketApi
 */
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON + ";charset=UTF-8"})
public interface BitBucketApi {

    @GET
    @Path("users/{userName}")
    User getUser(@PathParam("userName") String userName);

    @GET
    @Path("teams")
    List<Group> getGroups(@QueryParam("rule") String rule);

    @GET
    @Path("repositories/{username}")
    void getUserRepositories(@PathParam("username") String userName);
}

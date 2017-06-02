package com.teclick.tools.vcs.git.gitlab;

import com.teclick.tools.vcs.git.gitlab.entity.Branch;
import com.teclick.tools.vcs.git.gitlab.entity.Commit;
import com.teclick.tools.vcs.git.gitlab.entity.Project;
import com.teclick.tools.vcs.git.gitlab.entity.Session;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by Nelson on 2017-05-31 14:03.
 * GitlabApi
 */
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON + ";charset=UTF-8"})
public interface GitlabApi {

    @POST
    @Path("session")
    Session login(@QueryParam("login") String account, @QueryParam("password") String password) throws GitlabException;

    @GET
    @Path("projects")
    List<Project> getProjects() throws GitlabException;

    @GET
    @Path("projects/{projectId}/repository/branches")
    List<Branch> getBranches(@PathParam("projectId") int projectId) throws GitlabException;

    @GET
    @Path("projects/{projectId}/repository/commits")
    List<Commit> getCommits(@PathParam("projectId") int projectId,
                            @QueryParam("ref_name") String ref_name,
                            @QueryParam("path") String path,
                            @QueryParam("since") String since,
                            @QueryParam("until") String until) throws GitlabException;

    @GET
    @Path("projects/{projectId}/repository/commits/{sha}")
    Commit getCommits(@PathParam("projectId") int projectId, @PathParam("sha") String sha) throws GitlabException;

    @GET
    @Path("projects/{projectId}/repository/archive.zip")
    Response getRepositoryArchive(@PathParam("projectId") int projectId, @QueryParam("sha") String sha) throws GitlabException;

}

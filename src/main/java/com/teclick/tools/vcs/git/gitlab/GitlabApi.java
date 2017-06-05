package com.teclick.tools.vcs.git.gitlab;

import com.teclick.tools.vcs.git.GitException;
import com.teclick.tools.vcs.git.gitlab.entity.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by Nelson on 2017-05-31 14:03.
 * GitLabApi
 */
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON + ";charset=UTF-8"})
public interface GitLabApi {

    @POST
    @Path("session")
    Session login(@QueryParam("login") String account, @QueryParam("password") String password) throws GitException;

    @GET
    @Path("projects")
    List<Project> getProjects() throws GitException;

    @GET
    @Path("projects/{projectId}/repository/branches")
    List<Branch> getBranches(@PathParam("projectId") int projectId) throws GitException;

    @GET
    @Path("projects/{projectId}/repository/commits")
    List<Commit> getCommits(@PathParam("projectId") int projectId,
                            @QueryParam("ref_name") String ref_name,
                            @QueryParam("path") String path,
                            @QueryParam("since") String since,
                            @QueryParam("until") String until) throws GitException;

    @GET
    @Path("projects/{projectId}/repository/commits/{sha}")
    Commit getCommits(@PathParam("projectId") int projectId, @PathParam("sha") String sha) throws GitException;

    @GET
    @Path("projects/{projectId}/repository/archive.zip")
    Response getRepositoryArchive(@PathParam("projectId") int projectId, @QueryParam("sha") String sha) throws GitException;

    @GET
    @Path("namespaces")
    List<Namespace> searchNamespace(@QueryParam("search") String groupName) throws GitException;

    @POST
    @Path("projects")
    Project createProject(@QueryParam("name") String projectName, @QueryParam("namespace_id") int namespaceId,
                          @QueryParam("description") String description, @QueryParam("public") boolean isPublic) throws GitException;

}

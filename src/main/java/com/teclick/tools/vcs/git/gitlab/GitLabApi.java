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

    @POST
    @Path("groups")
    Group createGroup(@QueryParam("name") String name, @QueryParam("path") String path, @QueryParam("description") String description);

    @GET
    @Path("groups")
    List<Group> getGroups();

    @GET
    @Path("groups")
    List<Group> getGroups(@QueryParam("search") String groupName);

    @GET
    @Path("users")
    List<User> getUsers();

    @GET
    @Path("users")
    List<User> getUsers(@QueryParam("username") String userName);

    @POST
    @Path("groups/{id}/members")
    void createGroupMember(@PathParam("id") int groupId, @QueryParam("user_id") int userId, @QueryParam("access_level") int accussLevel);

    @GET
    @Path("groups/{id}/members")
    List<User> getGroupMembers(@PathParam("id") int groupId);

    @GET
    @Path("groups/{id}/members/{user_id}")
    User getGroupMember(@PathParam("id") int groupId, @PathParam("user_id") int userId);

    @PUT
    @Path("groups/{id}/members/{user_id}")
    void setGroupMembers(@PathParam("id") int groupId, @PathParam("user_id") int userId, @QueryParam("access_level") int accessLevel);

    @DELETE
    @Path("groups/{id}/members/{user_id}")
    void delGroupMembers(@PathParam("id") int groupId, @PathParam("user_id") int userId);

}

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
    @Path("groups/{id}/projects")
    List<Project> getGroupProjects(@PathParam("id") int groupId) throws GitException;

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
    Project addProject(@QueryParam("name") String projectName, @QueryParam("namespace_id") int namespaceId,
                       @QueryParam("description") String description, @QueryParam("public") boolean isPublic, @HeaderParam("SUDO") String sudoUser) throws GitException;

    @POST
    @Path("groups")
    Group addGroup(@QueryParam("name") String name, @QueryParam("path") String path, @QueryParam("description") String description, @HeaderParam("SUDO") String sudoUser);

    @GET
    @Path("groups")
    List<Group> getGroups();

    @GET
    @Path("groups")
    List<Group> getGroups(@QueryParam("search") String groupName);

    @DELETE
    @Path("groups/{id}")
    void delGroup(@PathParam("id") int id);

    @GET
    @Path("users")
    List<User> getUsers();

    @GET
    @Path("users")
    List<User> getUsers(@QueryParam("username") String userName);

    @POST
    @Path("groups/{id}/members")
    void addGroupMember(@PathParam("id") int groupId, @QueryParam("user_id") int userId, @QueryParam("access_level") int accessLevel);

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

    @PUT
    @Path("users/{id}")
    void changeMemberPermission(@PathParam("id") int userId, @QueryParam("can_create_group") boolean can_create_group, @QueryParam("external") boolean external);

    @PUT
    @Path("groups/{id}")
    void setGroup(@PathParam("id") int groupId, @QueryParam("name") String name, @QueryParam("path") String path, @QueryParam("description") String description);

    @POST
    @Path("groups/{id}/projects/{project_id}")
    void transferProjectToGroup(@PathParam("id") int groupId, @PathParam("project_id") int projectId);

}

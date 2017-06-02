package com.teclick.tools.vcs.git.gitlab;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.teclick.tools.vcs.git.gitlab.entity.*;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;

/**
 * Created by Nelson on 2017-05-31 14:04.
 * GitlabApiClient
 */
public class GitlabApiClient implements GitlabApi {

    private final GitlabApi gitlabApi;

    public GitlabApiClient(String url, String account, String password) throws GitlabException {

        JacksonJsonProvider provider = new JacksonJsonProvider();
        provider.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        gitlabApi = JAXRSClientFactory.create(url, GitlabApi.class, Collections.singletonList(provider), true);

        if (System.getProperty("debug", "").equals("true")) {
            WebClient.getConfig(gitlabApi).getInInterceptors().add(new LoggingInInterceptor());
            WebClient.getConfig(gitlabApi).getOutInterceptors().add(new LoggingOutInterceptor());
        }

        login(account, password);
    }

    public GitlabApiClient(String url, String account, String password, int timeoutInSecond) throws GitlabException {
        this(url, account, password);

        HTTPConduit conduit = WebClient.getConfig(gitlabApi).getHttpConduit();
        HTTPClientPolicy policy = new HTTPClientPolicy();
        policy.setReceiveTimeout(timeoutInSecond * 1000);
        conduit.setClient(policy);
    }

    @Override
    public Session login(String account, String password) throws GitlabException {
        try {
            Session session = gitlabApi.login(account, password);
            WebClient.client(gitlabApi).header("PRIVATE-TOKEN", session.getPrivate_token());
            return session;
        } catch (WebApplicationException e) {
            throw new GitlabException("login", e);
        }
    }

    @Override
    public List<Project> getProjects() throws GitlabException {
        try {
            return gitlabApi.getProjects();
        } catch (WebApplicationException e) {
            throw new GitlabException("getProjects", e);
        }
    }

    @Override
    public List<Branch> getBranches(int projectId) throws GitlabException {
        try {
            return gitlabApi.getBranches(projectId);
        } catch (WebApplicationException e) {
            throw new GitlabException("getBranches", e);
        }
    }

    @Override
    public List<Commit> getCommits(int projectId, String ref_name, String path, String since, String until) throws GitlabException {
        try {
            return gitlabApi.getCommits(projectId, ref_name, path, since, until);
        } catch (WebApplicationException e) {
            throw new GitlabException("getCommits", e);
        }
    }

    @Override
    public Commit getCommits(int projectId, String sha) throws GitlabException {
        try {
            return gitlabApi.getCommits(projectId, sha);
        } catch (WebApplicationException e) {
            throw new GitlabException("getCommits", e);
        }
    }

    @Override
    public Response getRepositoryArchive(int projectId, String sha) throws GitlabException {
        try {
            return gitlabApi.getRepositoryArchive(projectId, sha);
        } catch (WebApplicationException e) {
            throw new GitlabException("getRepositoryArchive", e);
        }
    }

    @Override
    public List<Namespace> searchNamespace(String groupName) throws GitlabException {
        try {
            return gitlabApi.searchNamespace(groupName);
        } catch (WebApplicationException e) {
            throw new GitlabException("searchNamespace", e);
        }
    }

    @Override
    public Project createProject(String projectName, int namespaceId, String description, boolean isPublic) throws GitlabException {
        try {
            return gitlabApi.createProject(projectName, namespaceId, description, isPublic);
        } catch (WebApplicationException e) {
            throw new GitlabException("searchNamespace", e);
        }
    }

    private String getFileName(MultivaluedMap<String, String> header) {
        String[] contentDisposition = header.getFirst("Content-Disposition").split(";");
        for (String filename : contentDisposition) {
            if ((filename.trim().startsWith("filename"))) {
                String[] name = filename.split("=");
                return name[1].trim().replaceAll("\"", "");
            }
        }
        return "unknown";
    }

}

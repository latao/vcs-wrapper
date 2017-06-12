package com.teclick.tools.vcs.git.gitlab.entity;

import javax.ws.rs.QueryParam;

/**
 * Created by Nelson on 2017-06-12 10:02.
 * GroupProjectParam
 */
public class GroupProjectParam {

    @QueryParam("archived")
    private Boolean archived;

    @QueryParam("visibility")
    private String visibility;

    @QueryParam("order_by")
    private String orderBy;

    @QueryParam("sort")
    private String sort;

    @QueryParam("search")
    private String search;

    @QueryParam("simple")
    private Boolean simple;

    @QueryParam("owned")
    private Boolean owned;

    @QueryParam("starred")
    private Boolean starred;

    @QueryParam("page")
    private Integer page;

    @QueryParam("per_page")
    private Integer pageSize;

    public Boolean getSimple() {
        return simple;
    }

    public void setSimple(Boolean simple) {
        this.simple = simple;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Boolean getArchived() {
        return archived;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public Boolean getOwned() {
        return owned;
    }

    public void setOwned(Boolean owned) {
        this.owned = owned;
    }

    public Boolean getStarred() {
        return starred;
    }

    public void setStarred(Boolean starred) {
        this.starred = starred;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }
}

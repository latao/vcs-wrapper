package com.teclick.tools.vcs;

import java.io.File;
import java.util.List;

/**
 * Created by Nelson on 2017-05-24.
 * VCS
 */
public interface VCS {

    /**
     * 界面使用
     * 取得项目的名称列表
     * @return 返回项目的集合
     */
    List<ProjectItem> listProjects(String groupName) throws VCSException;

    /**
     * 取得某个项目分支的列表
     * @param project 项目的名称
     * @return 返回分支的集合
     */
    List<String> listProjectBranch(String project) throws VCSException;

    /**
     * 界面使用
     * 取得自上次编译后的版本信息
     * @param project 项目的名称
     * @param branch  项目的分支
     * @param lastBuildVersion 上次编译的版本
     * @return 返回自上次编译版本后的版本信息
     * @throws VCSException 当发生异常统一转换成成这个异常
     */
    List<VersionCommentItem> getVersionsAfterLastBuild(String project, String branch, String lastBuildVersion) throws VCSException;

    /**
     * 下载代码到指定的目录
     * @param Folder 代码存放的目录
     * @param project 项目的名称
     * @param branch 项目的分支
     * @param version 下载的版本
     * @throws VCSException 当发生异常统一转换成成这个异常
     */
    void checkout(File Folder, String project, String branch, String version) throws VCSException;

    /**
     * 通过这个方法判断是否要自动升级版本号
     * @param project 项目的名称
     * @param branch  项目的分支
     * @param module  项目的 Module
     * @param lastBuildVersion 上次编译时的版本
     * @param maxVersion 本次将要编译的版本
     * @return 版本的信息
     * @throws VCSException 当发生异常统一转换成成这个异常
     */
    VersionCommentItem getMaxVersion(String project, String branch, String module, String lastBuildVersion, final String maxVersion) throws VCSException;

    /**
     * 把指定目录的内容放到版本控制系统
     * @param folder 目录
     * @param projectName 项目名称
     * @param branch 分支名称
     * @throws VCSException 当发生异常统一转换成成这个异常
     */
    void importToVcs(String groupName, File folder, String projectName, String branch) throws VCSException;

    /**
     * 从现有分支创建一个分支
     * @param project 项目名称或者编号
     * @param branch 分支名称
     * @param newBranch 新分支名称
     * @throws VCSException 当发生异常统一转换成成这个异常
     */
    void createBranch(String project, String branch, String newBranch) throws VCSException;

    /**
     * 返回运行的环境
     * @return 运行环境的对象
     */
    VCSContext getContext();
}

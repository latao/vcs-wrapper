package com.teclick.tools.vcs;

import com.teclick.tools.vcs.git.GitClientWrapper;
import com.teclick.tools.vcs.svn.SVNClientWrapper;

/**
 * Created by Nelson on 2017-05-25.
 * VCSFactory
 */
public class VCSFactory {

    public enum VCSType {none, git, svn}

    public static VCSType valueOf(String key) {
        for (VCSType v : VCSType.values()) {
            if (v.toString().equals(key)) {
                return v;
            }
        }
        return VCSType.none;
    }

    public static VCS newInstance(VCSType vcsType, VCSContext context) throws VCSException {

        switch (vcsType) {
            case git: {
                return new GitClientWrapper(context);
            }

            case svn: {
                return new SVNClientWrapper(context);
            }

            default: {
                return null;
            }
        }

    }

}

package com.teclick.tools.vcs;

import com.teclick.tools.vcs.git.BitbucketClientWrapper;
import com.teclick.tools.vcs.git.GitlabClientWrapper;
import com.teclick.tools.vcs.svn.SVNClientWrapper;

/**
 * Created by Nelson on 2017-05-25.
 * VCSFactory
 */
public class VCSFactory {

    public enum VCSType {none, git_lab, git_bit_bucket, svn}

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
            case git_lab: {
                return new GitlabClientWrapper(context);
            }
            case svn: {
                return new SVNClientWrapper(context);
            }
            case git_bit_bucket: {
                return new BitbucketClientWrapper(context);
            }
            default: {
                return null;
            }
        }

    }

}

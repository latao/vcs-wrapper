package com.teclick.tools.vcs.git;

/**
 * Created by Nelson on 2017-06-05 19:50.
 * GitException
 */
public class GitException extends Exception {

    public GitException(String message) {
        super(message);
    }

    public GitException(String message, Throwable cause) {
        super(message, cause);
    }
}

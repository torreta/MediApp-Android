package com.mediapp.ft.sync;

/**
 * Created by Frank on 3/28/2015.
 */
public interface ServerAuthenticate {
    public String userSignUp(final String userName, final String email, final String pass, String authType) throws Exception;
    public String userSignIn(final String user, final String pass, String authType) throws Exception;
}

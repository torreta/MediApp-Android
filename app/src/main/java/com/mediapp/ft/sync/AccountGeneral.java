package com.mediapp.ft.sync;

/**
 * Created by Frank on 3/28/2015.
 */
public class AccountGeneral {
    /**
     * Account type id
     */
    public static final String ACCOUNT_TYPE = "com.mediapp.ft";

    /**
     * Account name
     */
    public static final String ACCOUNT_NAME = "MediApp";

    /**
     * Auth token types
     */
    public static final String AUTHTOKEN_TYPE_READ_ONLY = "Read only";
    public static final String AUTHTOKEN_TYPE_READ_ONLY_LABEL = "Read only access to an MediApp account";

    public static final String AUTHTOKEN_TYPE_FULL_ACCESS = "Full access";
    public static final String AUTHTOKEN_TYPE_FULL_ACCESS_LABEL = "Full access to an MediApp account";

    /**
     * User data fields
     */
    public static final String USERDATA_USER_OBJ_ID = "userObjectId";   //Parse.com object id

    public static final ServerAuthenticate sServerAuthenticate = new ParseComServerAuthenticate();
}

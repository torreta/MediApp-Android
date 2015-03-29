package com.mediapp.ft.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

/**
 * Created by Frank on 3/28/2015.
 */
public class SyncUtils {

    //Use getBaseContext() when calling this method to set argument
    public String getToken(Context context){

        AccountManager mAccountManager;
        Account account;
        String token;
        String accountType;
        String name;

        // Get Account type name
        accountType = AccountGeneral.ACCOUNT_TYPE;

        // Search MediApp Account
        mAccountManager = AccountManager.get(context);
        name = mAccountManager.getAccountsByType(accountType)[0].name;

        // Create new account
        account = new Account(name, accountType);

        // Get Auth Token from that account
        token = mAccountManager.peekAuthToken(account, accountType);

        return token;
    }
}

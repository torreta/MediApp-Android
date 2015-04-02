package com.mediapp.ft.mediapp;

import android.app.Application;

/**
 * Created by Frank on 4/1/2015.
 */

public  class  Global extends Application {
    private String accountName = null;

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String name) {
        this.accountName = name;
    }
}
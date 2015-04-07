package com.mediapp.ft.mediapp;



import android.content.Intent;
import android.content.BroadcastReceiver;
import android.content.Context;

import android.app.NotificationManager;

/**
 * Created by torreta on 07/04/2015.
 */
public class AlarmReceiver extends BroadcastReceiver {

    NotificationManager notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service1 = new Intent(context, AlarmService.class);
        context.startService(service1);
    }
}
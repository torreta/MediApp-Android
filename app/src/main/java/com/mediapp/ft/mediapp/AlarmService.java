package com.mediapp.ft.mediapp;

/**
 * Created by torreta on 07/04/2015.
 */

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;


public class AlarmService extends IntentService
{

    private static final int NOTIFICATION_ID = 3;
    private static final String TAG = "PILL ALARM";
    private NotificationManager notificationManager;
    private PendingIntent pendingIntent;


    public AlarmService() {
        super("AlarmService");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // don't notify if they've played in last 24 hr
        Log.i(TAG,"Alarm Service has started. segun");
        Context context = this.getApplicationContext();
        notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent mIntent = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("test", "test");
        mIntent.putExtras(bundle);
        pendingIntent = PendingIntent.getActivity(context, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Resources res = this.getResources();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        builder.setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.pillsilla)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.pillsilla))
                .setTicker("meh")
                .setAutoCancel(true)
                .setContentTitle("Sirve?")
                .setContentText("Sirve===???");


        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
        Log.i(TAG,"Notifications sent.");

    }

}
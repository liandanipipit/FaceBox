package com.pipitliandani.android.facebox;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;

import com.pipitliandani.android.facebox.fragments.BirthdayFragment;

/**
 * Created by User on 28/06/2018.
 */

public class MyNewIntentService extends IntentService {
    private static final int NOTIFICATION_ID = 3;
    public MyNewIntentService(){
        super("MyNewIntentService");
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Today is your friends birthday_icon");
        builder.setContentText("Tap to see it");
        builder.setSmallIcon(R.drawable.logoptlen);
        Intent notifyIntent = new Intent(this, NavigationDrawer.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        Notification notif = builder.build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(NOTIFICATION_ID, notif);

    }
}

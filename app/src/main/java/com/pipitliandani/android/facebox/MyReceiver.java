package com.pipitliandani.android.facebox;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by User on 28/06/2018.
 */

public class MyReceiver extends BroadcastReceiver {
    String TAG = "ALARM_RECEIVER";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && context != null){
            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)){
                Log.d(TAG, "onReceive: BOOT_COMPLETED");
                NotificationScheduler.setReminder(context, MyReceiver.class, 5, 0);
                return;
            }
        }
        Log.d(TAG, "Receive");
        NotificationScheduler.showNotification(context, NavigationDrawer.class, "It's your friend birthday", "Tap to open");

    }
}

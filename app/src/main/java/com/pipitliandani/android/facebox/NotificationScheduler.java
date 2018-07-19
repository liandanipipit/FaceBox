package com.pipitliandani.android.facebox;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.pipitliandani.android.facebox.R;

import java.util.Calendar;

/**
 * Created by User on 01/07/2018.
 */

public class NotificationScheduler {
    public static final int DAILY_REMINDER_REQUEST_CODE = 100;
    public static final String TAG = "NotificationScheduler";
    public static void setReminder(Context context, Class<?> cls, int hour, int min){
        Calendar calendar =  Calendar.getInstance();
        Calendar setCalendar = Calendar.getInstance();
        setCalendar.set(Calendar.HOUR_OF_DAY, hour);
        setCalendar.set(Calendar.MINUTE, min);
        setCalendar.set(Calendar.SECOND, 0);
        Log.d(TAG, hour+"");
        Log.d(TAG, min+"");

        cancelReminder(context, cls);
        if (setCalendar.before(calendar))
            setCalendar.add(Calendar.DATE, 1);
        ComponentName receiver = new ComponentName(context, cls);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
        Intent intent = new Intent(context, cls);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, DAILY_REMINDER_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, setCalendar.getTimeInMillis(), 1000*60*60*24, pendingIntent);
    }
    public static void cancelReminder(Context context, Class<?> cls){
        ComponentName receiver = new ComponentName(context, cls);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
        Intent intent = new Intent(context, cls);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, DAILY_REMINDER_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
    }
    public static void showNotification(Context context, Class<?> cls, String title, String content){
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent notifIntent = new Intent(context, cls);
        notifIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(cls);
        stackBuilder.addNextIntent(notifIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(DAILY_REMINDER_REQUEST_CODE, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "FaceBox");

        Log.d(TAG, title);
        Log.d(TAG, content);
        Notification notification = builder.setContentTitle(title).setContentText(content)
                .setAutoCancel(true)
                .setSound(alarmSound)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent).build();

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("FaceBox", "FaceBox Application", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("FaceBox Application for PT Len");
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(0, notification);
    }
}

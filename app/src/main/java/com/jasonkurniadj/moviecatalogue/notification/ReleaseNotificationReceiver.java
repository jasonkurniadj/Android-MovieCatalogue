package com.jasonkurniadj.moviecatalogue.notification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

import com.jasonkurniadj.moviecatalogue.MainActivity;
import com.jasonkurniadj.moviecatalogue.R;
import com.jasonkurniadj.moviecatalogue.model.ItemModel;

import java.util.ArrayList;
import java.util.Calendar;

public class ReleaseNotificationReceiver extends BroadcastReceiver {

    public static final String BUNDLE_DATA = "BUNDLE_DATA";
    public static final String EXTRA_DATA = "EXTRA_DATA";

    private static final String RELEASE_REMINDER_CHANNEL_ID = "RELEASE_REMAINDER_CHANNEL_ID";
    private static final CharSequence RELEASE_REMINDER_CHANNEL_NAME = "RELEASE_REMINDER_CHANNEL_NAME";
    private static final String RELEASE_REMINDER_GROUP_NAME = "RELEASE_REMINDER_GROUP_NAME";
    private static final int RELEASE_NOTIFICATION_ID = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        showNotification(context, intent);
    }

    public void setNotification(Context context, ArrayList<ItemModel> releaseInstance) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.AM_PM, Calendar.AM);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReleaseNotificationReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_DATA, releaseInstance);
        intent.putExtra(EXTRA_DATA, bundle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, RELEASE_NOTIFICATION_ID, intent,0);

        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    public void cancelNotification(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReleaseNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, RELEASE_NOTIFICATION_ID, intent,0);
        pendingIntent.cancel();

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

    public boolean isSetNotification(Context context) {
        Intent intent = new Intent(context, ReleaseNotificationReceiver.class);
        return PendingIntent.getBroadcast(context, RELEASE_NOTIFICATION_ID, intent, PendingIntent.FLAG_NO_CREATE) != null;
    }

    private void showNotification(Context context, Intent intent) {
        Bundle bundle = intent.getBundleExtra(EXTRA_DATA);
        ArrayList<ItemModel> modelList = (ArrayList<ItemModel>) bundle.getSerializable(BUNDLE_DATA);

        Intent mIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, RELEASE_NOTIFICATION_ID, mIntent,0);

        int totalRelease = 1;
        if (modelList != null) {
            totalRelease = modelList.size();
        }

        for (int i=0; i<totalRelease; i++) {
            String movieName = context.getResources().getString(R.string.release_notification_title);
            String movieOverview = context.getResources().getString(R.string.release_notification_content);

            if (modelList != null) {
                ItemModel model = modelList.get(i);
                movieName = model.getItemName();
                movieOverview = model.getItemDescription();
            }

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, RELEASE_REMINDER_CHANNEL_ID)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_local_movies_black_24dp)
                    .setContentTitle(movieName)
                    .setContentText(movieOverview)
                    .setSubText(context.getResources().getString(R.string.release_notification_sub_title, i+1))
                    .setGroup(RELEASE_REMINDER_GROUP_NAME)
                    .setAutoCancel(true);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(RELEASE_REMINDER_CHANNEL_ID, RELEASE_REMINDER_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
                builder.setChannelId(RELEASE_REMINDER_CHANNEL_ID);

                if (notificationManager != null) {
                    notificationManager.createNotificationChannel(channel);
                }
            }

            Notification notification = builder.build();
            if (notificationManager != null) {
                notificationManager.notify(RELEASE_NOTIFICATION_ID, notification);
            }
        }
    }
}

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

import androidx.core.app.NotificationCompat;

import com.jasonkurniadj.moviecatalogue.MainActivity;
import com.jasonkurniadj.moviecatalogue.R;

import java.util.Calendar;

public class DailyNotificationReceiver extends BroadcastReceiver {

    private static final String DAILY_REMINDER_CHANNEL_ID = "DAILY_REMAINDER_CHANNEL";
    private static final CharSequence DAILY_REMINDER_CHANNEL_NAME = "DAILY_REMAINDER_NAME";
    private static final int DAILY_NOTIFICATION_ID = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        showNotification(context);
    }

    public void setNotification (Context context) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.AM_PM, Calendar.AM);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, DailyNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, DAILY_NOTIFICATION_ID, intent,0);

        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    public void cancelNotification (Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, DailyNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, DAILY_NOTIFICATION_ID, intent,0);
        pendingIntent.cancel();

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

    public boolean isSetNotification (Context context) {
        Intent intent = new Intent(context, DailyNotificationReceiver.class);
        return PendingIntent.getBroadcast(context, DAILY_NOTIFICATION_ID, intent, PendingIntent.FLAG_NO_CREATE) != null;
    }

    private void showNotification (Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, DAILY_NOTIFICATION_ID, intent,0);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, DAILY_REMINDER_CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_local_movies_black_24dp)
                .setContentTitle(context.getResources().getString(R.string.daily_notification_title))
                .setContentText(context.getResources().getString(R.string.daily_notification_content))
                .setSubText(context.getResources().getString(R.string.daily_notification_sub_title))
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(DAILY_REMINDER_CHANNEL_ID, DAILY_REMINDER_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            builder.setChannelId(DAILY_REMINDER_CHANNEL_ID);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        Notification notification = builder.build();
        if (notificationManager != null) {
            notificationManager.notify(DAILY_NOTIFICATION_ID, notification);
        }
    }
}

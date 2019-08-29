package com.jasonkurniadj.moviecatalogue.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent;

        serviceIntent = new Intent(context, NotificationService.class);
        context.startService(serviceIntent);
    }
}

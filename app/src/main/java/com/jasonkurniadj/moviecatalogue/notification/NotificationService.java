package com.jasonkurniadj.moviecatalogue.notification;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.jasonkurniadj.moviecatalogue.asyncTask.GetHomeListTask;
import com.jasonkurniadj.moviecatalogue.model.ItemModel;

import java.util.ArrayList;

public class NotificationService extends Service {

    private ArrayList<ItemModel> releaseNotification;
    public void setReleaseNotification(ArrayList<ItemModel> releaseNotification) {
        this.releaseNotification = releaseNotification;
    }

    public NotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    private void setNotification() {
        DailyNotificationReceiver dailyNotificationReceiver = new DailyNotificationReceiver();
        dailyNotificationReceiver.setNotification(getApplicationContext());

        GetHomeListTask task = new GetHomeListTask(getApplicationContext(), 1);
        task.execute(1);

        ReleaseNotificationReceiver releaseNotificationReceiver = new ReleaseNotificationReceiver();
        releaseNotificationReceiver.setNotification(getApplicationContext(), releaseNotification);
    }
}

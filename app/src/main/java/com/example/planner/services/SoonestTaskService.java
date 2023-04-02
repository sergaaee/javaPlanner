package com.example.planner.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.view.View;

import com.example.planner.receivers.NotificationTaskTime;

import java.time.LocalDateTime;

public class SoonestTaskService extends Service {
    public SoonestTaskService() {
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        View view = null;
        SharedPreferences preferences = getSharedPreferences("activities.MainPageActivity", MODE_PRIVATE);
        createNotificationChannel();
        if (LocalDateTime.now().toString().split("\\.")[0].equals(preferences.getString("time", null))){
            createNotification(view);
            SystemClock.sleep(1000);
        }
        onTaskRemoved(intent);
        return Service.START_STICKY;
    }


    private void createNotificationChannel() {

        NotificationChannel serviceChannel = new NotificationChannel(
                "channel 1",
                "Example Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
        );

        NotificationManager manager = getSystemService(NotificationManager.class);
        if (manager != null) {
            manager.createNotificationChannel(serviceChannel);
        }
    }

    public void createNotification(View view) {
        SharedPreferences preferences = getSharedPreferences("activities.MainPageActivity", MODE_PRIVATE);
        Intent i = new Intent(this, NotificationTaskTime.class);
        i.putExtra("name", preferences.getString("name", "null"));
        sendBroadcast(i);

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartServiceIntent = new Intent(getApplicationContext(),this.getClass());
        restartServiceIntent.setPackage(getPackageName());
        startService(restartServiceIntent);
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
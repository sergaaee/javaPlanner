package com.example.planner;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.planner.activities.AllTasksActivity;

public class NotificationTaskTime extends BroadcastReceiver {

    private String name = "";
    private int counter = 1;

    @Override
    public void onReceive(Context context, @NonNull Intent intent) {

        Bundle extras = intent.getExtras();
        name = extras.getString("name");
        sendNotification(context);

    }


    private void sendNotification(Context context) {

        Intent resultIntent = new Intent(context, AllTasksActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent,
                PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel 1")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Task starts")
                .setContentText(String.format("Your task %s starts now!", name))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        // notificationId is a unique int for each notification that you must define
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(counter++, builder.build());
    }

}

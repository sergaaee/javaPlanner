package com.example.planner.receivers;

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

import com.example.planner.R;
import com.example.planner.activities.AuthActivity;

public class NotificationTaskReceiver extends BroadcastReceiver {

    private String name = "";
    private int counter = 1;

    @Override
    public void onReceive(Context context, @NonNull Intent intent) {

        Bundle extras = intent.getExtras();
        name = extras.getString("name");
        sendNotification(context);

    }


    private void sendNotification(Context context) {

        Intent resultIntent = new Intent(context, AuthActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent,
                PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel 1")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Task starts")
                .setContentText(String.format("Your task %s starts now!", name))
                .setContentIntent(resultPendingIntent)
                .addAction(R.drawable.ic_launcher_background, "Open", resultPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        // ALREADY HANDLED IN TASK_CREATING_ACTIVITY
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

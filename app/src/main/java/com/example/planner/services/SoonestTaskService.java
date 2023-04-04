package com.example.planner.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.view.View;

import com.example.planner.api.ApiUsage;
import com.example.planner.api.Tasks;
import com.example.planner.receivers.NotificationTaskTime;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class SoonestTaskService extends Service {
    public SoonestTaskService() {
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        View view = null;
        SharedPreferences preferences = getSharedPreferences("activities.MainPageActivity", MODE_PRIVATE);
        createNotificationChannel();
        String time = LocalDateTime.now().toString().split("\\.")[0];
        if (time.equals(preferences.getString("sTime", "null"))){
            createNotification(view);
            updateSoonestTask();
            SystemClock.sleep(960);
        }
        onTaskRemoved(intent);
        return Service.START_STICKY;
    }

    private void updateSoonestTask(){

        String token = getSharedPreferences("activities.AuthActivity", MODE_PRIVATE).getString("access_token", "null");
        String name = getSharedPreferences("activities.MainPageActivity", MODE_PRIVATE).getString("name", "null");
        String sTime = getSharedPreferences("activities.MainPageActivity", MODE_PRIVATE).getString("sTime", "null");
        String eTime = getSharedPreferences("activities.MainPageActivity", MODE_PRIVATE).getString("eTime", "null");
        String desc = getSharedPreferences("activities.MainPageActivity", MODE_PRIVATE).getString("desc", "null");
        String responseUpdateTask = Tasks.taskUpdate(token, name, name, sTime, eTime, desc, "-");
        if (responseUpdateTask.equals("Success")) {
            try {
                String Response = ApiUsage.getMethod("users",
                        getSharedPreferences("activities.AuthActivity", MODE_PRIVATE).getString("access_token", "null"),
                        new String[]{""},
                        new String[]{""});
                if (!Response.equals("401")) {

                    JSONArray response = new JSONArray(Response);
                    JSONArray tasks = response.getJSONArray(1);
                    if (tasks.length() > 0) {
                        JSONObject soonestTask = tasks.getJSONObject(0);
                        String taskName = soonestTask.getString("name");
                        String taskSTime  = LocalDate.parse(soonestTask.getString("start_time").split("T")[0]).
                                format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)) + " " +
                                soonestTask.getString("start_time").split("T")[1].split("\\.")[0];
                        String taskEtime = LocalDate.parse(soonestTask.getString("end_time").split("T")[0]).
                                format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)) + " " +
                                soonestTask.getString("end_time").split("T")[1].split("\\.")[0];
                        String taskDesc = soonestTask.getString("description");
                        String taskStatus = soonestTask.getString("status");
                        getSharedPreferences("activities.MainPageActivity", MODE_PRIVATE)
                                .edit()
                                .putString("name", taskName)
                                .putString("sTime", taskSTime)
                                .putString("eTime", taskEtime)
                                .putString("desc", taskDesc)
                                .putString("status", taskStatus)
                                .apply();
                    }
                }
            } catch (JSONException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void createNotificationChannel() {

        NotificationChannel serviceChannel = new NotificationChannel(
                "channel 1",
                "Task Channel",
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
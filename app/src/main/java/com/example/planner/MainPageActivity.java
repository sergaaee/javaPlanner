package com.example.planner;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class MainPageActivity extends AppCompatActivity {

    private TextView welcomeField;
    private TextView soonestTaskField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_page);
        String token = AuthActivity.getAccessToken();
        try {
            String Response = ApiUsage.getMethod("users",
                    token,
                    new String[]{""},
                    new String[]{""});
            if (!Response.equals("401")) {
                welcomeField = findViewById(R.id.textWelcomeBack);
                soonestTaskField = findViewById(R.id.textSoonestTask);
                parseJSONResponse(new JSONArray(Response));
            }
        } catch (JSONException | IOException e) {
            throw new RuntimeException(e);
        }


    }

    @SuppressLint("SetTextI18n")
    private void parseJSONResponse(@NonNull JSONArray response) throws JSONException {

        JSONObject userData = response.getJSONObject(0);
        String username = userData.getString("username");
        welcomeField.setText(getString(R.string.welcome_back) + " " + username);
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
            soonestTaskField.setText(getString(R.string.soonest_task) + "\n\n" +
                    getString(R.string.task_name) + " " + taskName + "\n" +
                    getString(R.string.start_time) + " " + taskSTime + "\n" +
                    getString(R.string.end_time) + " " + taskEtime + "\n" +
                    getString(R.string.task_desc) + " " + taskDesc
                    );
        }
    }

    public void newTask(View view) {
        Intent intent = new Intent(MainPageActivity.this,
                TaskCreatingActivity.class);
        startActivity(intent);
    }

    public void allTasks(View view){
        Intent intent = new Intent(MainPageActivity.this,
                AllTasksActivity.class);
        startActivity(intent);
    }

}
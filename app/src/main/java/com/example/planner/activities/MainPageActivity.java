package com.example.planner.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.planner.api.ApiUsage;
import com.example.planner.R;

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
    private String username, email,regDate;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_page);
        String token = getSharedPreferences("activities.AuthActivity", MODE_PRIVATE).getString("access_token", "null");
        createSoonestTask(token);

    }


    public void createSoonestTask(String token){
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.main_page_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("SetTextI18n")
    private void parseJSONResponse(@NonNull JSONArray response) throws JSONException {

        JSONObject userData = response.getJSONObject(0);
        username = userData.getString("username");
        email = userData.getString("email");
        userId = userData.getInt("id");
        regDate = userData.getString("reg_date");
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
            String taskStatus = soonestTask.getString("status");
            soonestTaskField.setText(getString(R.string.soonest_task) + "\n\n" +
                    getString(R.string.task_name) + " " + taskName + "\n" +
                    getString(R.string.start_time) + " " + taskSTime + "\n" +
                    getString(R.string.end_time) + " " + taskEtime + "\n" +
                    getString(R.string.task_desc) + " " + taskDesc + "\n" +
                    getString(R.string.status) + " " + taskStatus
                    );
            SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
            sharedPref.edit()
                    .putString("name", taskName)
                    .putString("sTime", soonestTask.getString("start_time"))
                    .putString("eTime", soonestTask.getString("end_time"))
                    .putString("desc", taskDesc)
                    .putString("status", taskStatus)
                    .apply();
        }
    }

    public void newTask(View view) {
        Intent intent = new Intent(MainPageActivity.this,
                TaskCreatingActivity.class);
        startActivity(intent);
    }

    public void toProfile(View view){
        Intent intent = new Intent(MainPageActivity.this,
                UserProfileActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("id", userId);
        intent.putExtra("email", email);
        intent.putExtra("regDate", regDate);
        startActivity(intent);
    }

    public void allTasks(View view){
        Intent intent = new Intent(MainPageActivity.this,
                AllTasksActivity.class);
        startActivity(intent);
    }

    public void allTasksMenu(MenuItem menuItem){
        Intent intent = new Intent(MainPageActivity.this,
                AllTasksActivity.class);
        startActivity(intent);
    }

    public void toProfileMenu(MenuItem menuItem){
        Intent intent = new Intent(MainPageActivity.this,
                UserProfileActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("id", userId);
        intent.putExtra("email", email);
        intent.putExtra("regDate", regDate);
        startActivity(intent);
    }

    public void logOutMenu(MenuItem menuItem){
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.title_log_out))
                .setMessage(getString(R.string.exit_confirmation))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.ok, (dialog, whichButton) -> {
                    getSharedPreferences("activities.AuthActivity", MODE_PRIVATE).edit().putString("refresh_token", "null").apply();
                    getSharedPreferences("activities.AuthActivity", MODE_PRIVATE).edit().putString("access_token", "null").apply();
                    Intent intent = new Intent(MainPageActivity.this,
                            AuthActivity.class);
                    startActivity(intent);
                })
                .setNegativeButton(android.R.string.cancel, null).show();
    }

    public void addFriendMenu(MenuItem item) {
        Intent intent = new Intent(MainPageActivity.this,
                AllFriendsActivity.class);
        startActivity(intent);
    }
}
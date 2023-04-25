package com.example.planner.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.planner.R;
import com.example.planner.api.Friends;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class FriendsTasksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_friends_tasks);
        viewBuilding();
    }

    @SuppressLint("SetTextI18n")
    private void viewBuilding(){
        LinearLayout layout = findViewById(R.id.linearLayoutTasksCategories);
        Bundle extras = getIntent().getExtras();
        try {
            String Response = Friends.getFriendsTasks(getSharedPreferences("activities.AuthActivity", MODE_PRIVATE).getString("access_token", "null"),  extras.getString("id"));
            if (!Response.equals("401") && !Response.equals("422")) {
                String[][] tasks = parseJSONResponse(new JSONArray(Response));
                if (!tasks[0][0].equals("No")){
                    for (String[] task : tasks) {
                        TextView taskName = new TextView(this);
                        taskName.setText(getString(R.string.task_name) + " " + task[0]);
                        TextView taskSTime = new TextView(this);
                        taskSTime.setText(getString(R.string.start_time) + " " + task[1]);
                        TextView taskETime = new TextView(this);
                        taskETime.setText(getString(R.string.end_time) + " " + task[2]);
                        TextView taskDesc = new TextView(this);
                        taskDesc.setText(getString(R.string.task_desc) + " " + task[3]);
                        LinearLayout layoutNoStatus = new LinearLayout(this);
                        layoutNoStatus.setOrientation(LinearLayout.VERTICAL);
                        LinearLayout layoutToDo = new LinearLayout(this);
                        layoutToDo.setOrientation(LinearLayout.VERTICAL);
                        LinearLayout layoutInProgress = new LinearLayout(this);
                        layoutInProgress.setOrientation(LinearLayout.VERTICAL);
                        LinearLayout layoutCompleted = new LinearLayout(this);
                        layoutCompleted.setOrientation(LinearLayout.VERTICAL);
                        switch (task[4]){
                            case "-":
                                layoutNoStatus.addView(taskName);
                                layoutNoStatus.addView(taskSTime);
                                layoutNoStatus.addView(taskETime);
                                layoutNoStatus.addView(taskDesc);
                                break;
                            case "To do":
                                layoutToDo.setBackgroundColor(getColor(R.color.teal_700));
                                TextView toDo = new TextView(this);
                                toDo.setText(getResources().getStringArray(R.array.taskStatuses)[1]);
                                layoutToDo.addView(toDo);
                                layoutToDo.addView(taskName);
                                layoutToDo.addView(taskSTime);
                                layoutToDo.addView(taskETime);
                                layoutToDo.addView(taskDesc);
                                break;
                            case "In progress":
                                layoutInProgress.setBackgroundColor(getColor(R.color.in_progress));
                                TextView prog = new TextView(this);
                                prog.setText(getResources().getStringArray(R.array.taskStatuses)[2]);
                                layoutInProgress.addView(prog);
                                layoutInProgress.addView(taskName);
                                layoutInProgress.addView(taskSTime);
                                layoutInProgress.addView(taskETime);
                                layoutInProgress.addView(taskDesc);
                                break;
                            case "Completed":
                                layoutCompleted.setBackgroundColor(getColor(R.color.completed));
                                TextView comp = new TextView(this);
                                comp.setText(getResources().getStringArray(R.array.taskStatuses)[3]);
                                layoutCompleted.addView(comp);
                                layoutCompleted.addView(taskName);
                                layoutCompleted.addView(taskSTime);
                                layoutCompleted.addView(taskETime);
                                layoutCompleted.addView(taskDesc);
                                break;
                        }
                        layout.addView(layoutNoStatus);
                        layout.addView(layoutToDo);
                        layout.addView(layoutInProgress);
                        layout.addView(layoutCompleted);
                        layout.addView(new TextView(this));
                    }

                }
            }
            else {
                Toast.makeText(this, "Something went wrong...", Toast.LENGTH_LONG).show();
                startActivity(new Intent(FriendsTasksActivity.this, FriendProfileActivity.class).
                        putExtra("id", extras.getString("id")).
                        putExtra("status", extras.getString("status")));
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }


    @NonNull
    private String[][] parseJSONResponse(@NonNull JSONArray response) throws JSONException {
        JSONArray tasks = response.getJSONArray(0);
        String [][] result = new String[tasks.length()][];
        if (tasks.length() > 0) {
            for (int i = 0; i < tasks.length(); i++) {
                JSONObject task = tasks.getJSONObject(i);
                String taskName = task.getString("name");
                String taskSTime = LocalDate.parse(task.getString("start_time").split("T")[0]).
                        format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)) + " " +
                        task.getString("start_time").split("T")[1].split("\\.")[0];
                String taskETime = LocalDate.parse(task.getString("end_time").split("T")[0]).
                        format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)) + " " +
                        task.getString("end_time").split("T")[1].split("\\.")[0];
                String taskDesc = task.getString("description");
                String taskStatus = task.getString("status");
                result[i] = new String[]{taskName, taskSTime, taskETime, taskDesc, taskStatus};
            }
        }
        else {
            result = new String[1][];
            result[0] = new String[]{"No"};
        }
        return result;
    }

    public void backToFriendProfile(View view) {
        Bundle extras = getIntent().getExtras();
        Intent intent = new Intent(FriendsTasksActivity.this, FriendProfileActivity.class);
        intent.putExtra("id", extras.getString("id"));
        intent.putExtra("status", extras.getString("status"));
        startActivity(intent);
    }

    public void askForTask(View view) {
        Bundle extras = getIntent().getExtras();
        startActivity(new Intent(FriendsTasksActivity.this, AskFriendForTaskActivity.class).putExtra("friend_id", extras.getString("id")));
    }
}
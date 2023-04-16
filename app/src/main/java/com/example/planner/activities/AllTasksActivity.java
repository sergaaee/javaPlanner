package com.example.planner.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.planner.R;
import com.example.planner.api.ApiUsage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class AllTasksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_all_tasks);
        viewBuilding();
    }

    @SuppressLint("SetTextI18n")
    private void viewBuilding(){
        LinearLayout layout = findViewById(R.id.linearLayoutTasksCategories);
        try {
            String Response = ApiUsage.getMethod("tasks",
                    getSharedPreferences("activities.AuthActivity", MODE_PRIVATE).getString("access_token", "null"),
                    new String[]{""},
                    new String[]{""});
            if (!Response.equals("401")) {
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
                                layoutNoStatus.setOnClickListener(v -> {
                                    // need it for build next activity
                                    Intent intent = new Intent(AllTasksActivity.this,
                                            EditTaskActivity.class);
                                    intent.putExtra("name", task[0]);
                                    intent.putExtra("stime", task[1]);
                                    intent.putExtra("etime", task[2]);
                                    intent.putExtra("desc", task[3]);
                                    startActivity(intent);

                                });
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
                                layoutToDo.setOnClickListener(v -> {
                                    // need it for build next activity
                                    Intent intent = new Intent(AllTasksActivity.this,
                                            EditTaskActivity.class);
                                    intent.putExtra("name", task[0]);
                                    intent.putExtra("stime", task[1]);
                                    intent.putExtra("etime", task[2]);
                                    intent.putExtra("desc", task[3]);
                                    startActivity(intent);

                                });
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
                                layoutInProgress.setOnClickListener(v -> {
                                    // need it for build next activity
                                    Intent intent = new Intent(AllTasksActivity.this,
                                            EditTaskActivity.class);
                                    intent.putExtra("name", task[0]);
                                    intent.putExtra("stime", task[1]);
                                    intent.putExtra("etime", task[2]);
                                    intent.putExtra("desc", task[3]);
                                    startActivity(intent);

                                });
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
                                layoutCompleted.setOnClickListener(v -> {
                                    // need it for build next activity
                                    Intent intent = new Intent(AllTasksActivity.this,
                                            EditTaskActivity.class);
                                    intent.putExtra("name", task[0]);
                                    intent.putExtra("stime", task[1]);
                                    intent.putExtra("etime", task[2]);
                                    intent.putExtra("desc", task[3]);
                                    startActivity(intent);

                                });
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
        } catch (JSONException | IOException e) {
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
                String taskSTime  = LocalDate.parse(task.getString("start_time").split("T")[0]).
                        format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)) + " " +
                        task.getString("start_time").split("T")[1].split("\\.")[0];
                String taskETime = LocalDate.parse(task.getString("end_time").split("T")[0]).
                        format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)) + " " +
                        task.getString("end_time").split("T")[1].split("\\.")[0];
                String taskDesc = task.getString("description");
                String taskStatus = task.getString("status");
                result[i] = new String[]{taskName, taskSTime, taskETime, taskDesc, taskStatus};
                SharedPreferences sharedPref = getSharedPreferences("activities.MainPageActivity", MODE_PRIVATE);
                sharedPref.edit()
                        .putString("name", taskName)
                        .putString("sTime", task.getString("start_time"))
                        .putString("eTime", task.getString("end_time"))
                        .putString("desc", taskDesc)
                        .putString("status", taskStatus)
                        .apply();
            }
        }
        else {
            result = new String[1][];
            result[0] = new String[]{"No"};
        }
        return result;
    }


    public void backToMainPageFromTasks(View view){

        Intent intent = new Intent(AllTasksActivity.this,
                MainPageActivity.class);
        startActivity(intent);

    }




}
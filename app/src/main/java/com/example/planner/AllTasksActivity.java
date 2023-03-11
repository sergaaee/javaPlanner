package com.example.planner;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
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
        LinearLayout layout = findViewById(R.id.linearLayoutTasks);
        try {
            String Response = ApiUsage.getMethod("tasks",
                    AuthActivity.getAccessToken(),
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
                        LinearLayout layout1 = new LinearLayout(this);
                        layout1.setOrientation(LinearLayout.VERTICAL);
                        layout1.addView(taskName);
                        layout1.addView(taskSTime);
                        layout1.addView(taskETime);
                        layout1.addView(taskDesc);
                        layout1.setOnClickListener(v -> {

                            Intent intent = new Intent(AllTasksActivity.this,
                                    EditTaskActivity.class);
                            intent.putExtra("name", task[0]);
                            intent.putExtra("stime", task[1]);
                            intent.putExtra("etime", task[2]);
                            intent.putExtra("desc", task[3]);
                            startActivity(intent);

                        });
                        layout.addView(layout1);
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
                String taskEtime = LocalDate.parse(task.getString("end_time").split("T")[0]).
                        format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)) + " " +
                        task.getString("end_time").split("T")[1].split("\\.")[0];
                String taskDesc = task.getString("description");
                result[i] = new String[]{taskName, taskSTime, taskEtime, taskDesc};
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
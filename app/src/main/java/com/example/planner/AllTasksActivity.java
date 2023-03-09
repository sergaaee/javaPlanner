package com.example.planner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

    @Nullable
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
                    for (int i = 0; i < tasks.length; i++) {
                        TextView taskName = new TextView(this);
                        taskName.setText(getString(R.string.task_name) + " " + tasks[i][0]);
                        TextView taskSTime = new TextView(this);
                        taskSTime.setText(getString(R.string.start_time) + " " + tasks[i][1]);
                        TextView taskETime = new TextView(this);
                        taskETime.setText(getString(R.string.end_time) + " " + tasks[i][2]);
                        TextView taskDesc = new TextView(this);
                        taskDesc.setText(getString(R.string.task_desc) + " " + tasks[i][3]);
                        layout.addView(taskName);
                        layout.addView(taskSTime);
                        layout.addView(taskETime);
                        layout.addView(taskDesc);
                        TextView taskSpacer = new TextView(this);
                        layout.addView(taskSpacer);
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
        result[0] = new String[]{"No"};
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
        return result;
    }


    public void backToMainPageFromTasks(View view){

        Intent intent = new Intent(AllTasksActivity.this,
                MainPageActivity.class);
        startActivity(intent);

    }

}
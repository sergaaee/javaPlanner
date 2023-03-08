package com.example.planner;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainPageActivity extends AppCompatActivity {

    private TextView welcomeField;
    private TextView soonestTaskField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_page);
        SharedPreferences mPrefs = getSharedPreferences("AuthActivity", MODE_PRIVATE);
        String token = mPrefs.getString("access_token", "None");
        try {
            String Response = ApiUsage.getMethod("users",
                    new String[]{"Authorization"},
                    new String[]{"Bearer " + token});
            welcomeField = findViewById(R.id.textWelcomeBack);
            soonestTaskField = findViewById(R.id.textSoonestTask);
            //welcomeText = findViewById(R.string.welcome_back).getContext().
            parseJSONResponse(new JSONArray(Response));
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
            soonestTaskField.setText(getString(R.string.soonest_task) + "\n" +
                    getString(R.string.task_name) + " " + soonestTask.getString("name") + "\n" +
                    getString(R.string.start_time) + " " + soonestTask.getString("start_time") + "\n" +
                    getString(R.string.end_time) + " " + soonestTask.getString("end_time") + "\n" +
                    getString(R.string.task_desc) + " " + soonestTask.getString("description")
                    );
        }
    }

}
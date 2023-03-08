package com.example.planner;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        SharedPreferences mPrefs = getSharedPreferences("AuthActivity", MODE_PRIVATE);
        String token = mPrefs.getString("access_token", "None");
        String response = "";

        try {
            String Response = ApiUsage.getMethod("users",
                    new String[]{"Authorization"},
                    new String[]{"Bearer " + token});
            JSONArray fullData = new JSONArray(Response);
            JSONObject userData = fullData.getJSONObject(0);
            JSONArray tasks = fullData.getJSONArray(1);
            System.out.println(tasks.getJSONObject(0));
        } catch (JSONException | IOException e) {
            throw new RuntimeException(e);
        }
        setContentView(R.layout.activity_main_page);

    }
}
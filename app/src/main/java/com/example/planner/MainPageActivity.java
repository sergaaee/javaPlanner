package com.example.planner;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

import java.io.IOException;

public class MainPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            ApiUsage.postMethod("tasks", new String[]{""}, new String[]{""}, new String[]{""}, new String[]{""});
        } catch (JSONException | IOException e) {
            throw new RuntimeException(e);
        }
        setContentView(R.layout.activity_main_page);
    }
}
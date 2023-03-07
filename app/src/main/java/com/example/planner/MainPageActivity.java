package com.example.planner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

import java.io.IOException;
import java.time.LocalDate;

public class MainPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences mPrefs = getSharedPreferences("AuthActivity", MODE_PRIVATE);
        String token = mPrefs.getString("access_token", "None");
        String response = "";
        if (!token.equals("None")){
            try {
                String Response = ApiUsage.getMethod("users",
                        new String[]{"Authorization"},
                        new String[]{"Bearer " + token});
                String email = Response.split(",")[0].split("\"")[3];
                LocalDate regDate = LocalDate.parse(Response.split(",")[1].split("\"")[3].split("T")[0]);
                String userId = Response.split(",")[2].split("\"")[2].split(":")[1];
                String username = Response.split(",")[3].split("\"")[3];
                System.out.println(Response.split(",")[5]);
            } catch (JSONException | IOException e) {
                throw new RuntimeException(e);
            }
            setContentView(R.layout.activity_main_page);
        }
        else {
            Intent intent = new Intent(MainPageActivity.this,
                AuthActivity.class);
            startActivity(intent);
        }
    }
}
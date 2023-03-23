package com.example.planner.api;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.example.planner.activities.AuthActivity;

import java.io.IOException;

public class LogIn {

    @NonNull
    public static String newSession(String username, String password, SharedPreferences sharedPref){
        Session session = new Session(username, password);
        String response = session.createNewSession();
        if ("401".equals(response) || "422".equals(response)) {
            return "Error";
        } else {
            String[] tokens = response.split("\"");
            String accessToken = tokens[3].strip();
            String refreshToken = tokens[7].strip();
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("refresh_token", refreshToken);
            editor.apply();
            editor.putString("access_token", accessToken);
            editor.apply();
            return "Success";
        }
    }

    @NonNull
    public static String updateAccessToken() throws IOException {
        SharedPreferences.Editor editor = AuthActivity.getSharedPref().edit();
        String response = new Session().refreshAccessToken();
        if ("Error".equals(response)) {
            editor.putString("access_token", "null");
            editor.putString("refresh_token", "null");
            editor.apply();
            return "Error";
        } else {
            String accessToken = response.split("\"")[3];
            editor.putString("access_token", accessToken);
            editor.apply();
            return accessToken;
        }

    }


}

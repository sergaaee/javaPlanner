package com.example.planner;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.IOException;

public class LogIn {

    @NonNull
    public static String newSession(String username, String password, EditText passwordField, View view, SharedPreferences sharedPref){
        Session session = new Session(username, password);
        String response = session.createNewSession();
        if ("401".equals(response)) {
            Toast errorToast = Toast.makeText(view.getContext(), "Incorrect username or password", Toast.LENGTH_LONG);
            errorToast.show();
            passwordField.setText("");
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

package com.example.planner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class AuthActivity extends AppCompatActivity {

    private static SharedPreferences sharedPref;
    private final Context context = this;
    private static String fingerprint = "null";
    private static String refreshToken = "null";
    public static String getFingerprint(){ return fingerprint; }

    public static String getRefreshToken(){
        return refreshToken = sharedPref.getString("refresh_token", "null");
    }

    public static SharedPreferences getSharedPref(){
        return sharedPref;
    }

    public static String getAccessToken(){
        return sharedPref.getString("access_token", "null");
    }

    public static void setAccessToken(String token){
        sharedPref.edit().putString("access_token", token).apply();
    }


    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        refreshToken = sharedPref.getString("refresh_token", "null");
        fingerprint = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        if (refreshToken.equals("null")) {setContentView(R.layout.activity_auth);}
        else {

            try {
                String response = LogIn.updateAccessToken();
                Intent intent;// Has to be activity_main_page
                if (response.equals("Error")){
                    intent = new Intent(AuthActivity.this,
                            AuthActivity.class);
                }
                else {
                    intent = new Intent(AuthActivity.this,
                            MainPageActivity.class);
                }
                startActivity(intent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


    }


    public void signIn(View view){

        EditText loginField = findViewById(R.id.loginField);
        EditText passwordField = findViewById(R.id.passField);
        String username = loginField.getText().toString();
        String password = passwordField.getText().toString();
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        LogIn.newSession(username, password, passwordField, view, sharedPref);
        Intent intent = new Intent(AuthActivity.this,
                MainPageActivity.class);
        startActivity(intent);

    }

    public void signUpView(View view) {

        Intent intent = new Intent(AuthActivity.this,
                RegActivity.class);
        startActivity(intent);


    }





}


package com.example.planner.activities;

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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.planner.R;
import com.example.planner.api.LogIn;
import com.example.planner.services.SoonestTaskService;

import java.io.IOException;

public class AuthActivity extends AppCompatActivity {

    private static SharedPreferences sharedPref;
    private final Context context = this;
    private static String fingerprint = "null";

    public static String getFingerprint(){ return fingerprint; }

    public static SharedPreferences getSharedPref(){
        return sharedPref;
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
        String refreshToken = sharedPref.getString("refresh_token", "null");
        fingerprint = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        if (refreshToken.equals("null")) {setContentView(R.layout.activity_auth);}
        else {
            try {
                String response = LogIn.updateAccessToken();
                if (!response.equals("Error")){
                    Intent intent = new Intent(AuthActivity.this,
                            MainPageActivity.class);
                    startActivity(intent);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        startForegroundService(new Intent(this, SoonestTaskService.class));

    }





    public void signIn(View view){

        EditText loginField = findViewById(R.id.loginField);
        EditText passwordField = findViewById(R.id.passField);
        String username = loginField.getText().toString();
        String password = passwordField.getText().toString();
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        String response = LogIn.newSession(username, password, sharedPref);
        if (response.equals("Success")){
            Intent intent = new Intent(AuthActivity.this,
                    MainPageActivity.class);
            startActivity(intent);
        }
        else {
            Toast errorToast = Toast.makeText(view.getContext(), getString(R.string.incorrect_UoP), Toast.LENGTH_LONG);
            errorToast.show();
            passwordField.setText("");
        }

    }

    public void signUpView(View view) {

        Intent intent = new Intent(AuthActivity.this,
                RegActivity.class);
        startActivity(intent);


    }





}


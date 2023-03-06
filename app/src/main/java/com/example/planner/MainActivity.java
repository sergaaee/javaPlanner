package com.example.planner;

import android.annotation.SuppressLint;
import android.content.Context;
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

public class MainActivity extends AppCompatActivity {

    private final Context context = this;
    private static String fingerprint = "null";
    private static String refreshToken = "null";
    public static String getFingerprint(){ return fingerprint; }

    public static String getRefreshToken(){
        return refreshToken;
    }


    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        refreshToken = sharedPref.getString("refresh_token", "null");
        fingerprint = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        if (refreshToken.equals("null")){

            setContentView(R.layout.activity_auth);

        }

        else {


            LogIn.refreshAccessToken(sharedPref);
            setContentView(R.layout.activity_auth); // Has to be activity_main_page

        }


    }


    public void signIn(View view){
        EditText loginField = findViewById(R.id.loginField);
        EditText passwordField = findViewById(R.id.passField);
        String username = loginField.getText().toString();
        String password = passwordField.getText().toString();
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        LogIn.newSession(username, password, passwordField, view, sharedPref);

    }

    public void signUpView(View view) {

        setContentView(R.layout.activity_registration);

    }

    public void signUp(View view) throws Exception {

        EditText loginReg = findViewById(R.id.regLogin);
        String login = loginReg.getText().toString();
        EditText passwordReg = findViewById(R.id.regPass);
        String password = passwordReg.getText().toString();
        EditText emailReg = findViewById(R.id.regEmail);
        String email = emailReg.getText().toString();
        EditText passwordField2 = findViewById(R.id.regPass2);
        String password2 = passwordField2.getText().toString();
        if (login.equals("") || password.equals("") || !password.equals(password2)){
            Toast errorToast = Toast.makeText(MainActivity.this, "Error: bad login or passwords aren't match.", Toast.LENGTH_LONG);
            errorToast.show();
        }
        User user = new User(login, password, email);
        if (user.postNewUser().equals("422")){

            Toast errorToast = Toast.makeText(MainActivity.this, "Error: that username or e-mail already registered!", Toast.LENGTH_LONG);
            errorToast.show();

        }
        else {
            Toast successToast = Toast.makeText(MainActivity.this, "Successfully registered!", Toast.LENGTH_LONG);
            setContentView(R.layout.activity_auth);
            successToast.show();
        }


    }

    public void backToLogIn(View view) {setContentView(R.layout.activity_auth);}



}


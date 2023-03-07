package com.example.planner;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_reg);
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
            Toast errorToast = Toast.makeText(RegActivity.this, "Username already registered or passwords aren't match.", Toast.LENGTH_LONG);
            errorToast.show();
        }
        User user = new User(login, password, email);
        if (user.postNewUser().equals("422")){

            Toast errorToast = Toast.makeText(RegActivity.this, "Error: that username or e-mail already registered!", Toast.LENGTH_LONG);
            errorToast.show();

        }
        else {
            Toast successToast = Toast.makeText(RegActivity.this, "Successfully registered!", Toast.LENGTH_LONG);
            setContentView(R.layout.activity_auth);
            successToast.show();
        }


    }


    public void backToLogIn(View view) {
        Intent intent = new Intent(RegActivity.this,
                AuthActivity.class);
        startActivity(intent);
    }
}
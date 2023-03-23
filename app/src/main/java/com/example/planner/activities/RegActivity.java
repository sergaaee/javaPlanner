package com.example.planner.activities;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.planner.R;
import com.example.planner.api.User;

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
        if (login.length() == 0){
            makeText(RegActivity.this, getString(R.string.invalid_username), LENGTH_LONG).show();
        }
        else if (password.length() == 0) {
            makeText(RegActivity.this, getString(R.string.invalid_password), LENGTH_LONG).show();
        }
        else if (!password.equals(password2)) {
            makeText(RegActivity.this, getString(R.string.password_rnt_match), LENGTH_LONG).show();
        }
        else {
            String response =  new User(login, password, email).postNewUser();
            if (response.equals("422"))
                makeText(RegActivity.this, getString(R.string.already_registered), LENGTH_LONG).show();
            else {
                makeText(RegActivity.this, getString(R.string.success_reg), LENGTH_LONG).show();
                setContentView(R.layout.activity_auth);
            }
        }

    }


    public void backToLogIn(View view) {
        Intent intent = new Intent(RegActivity.this,
                AuthActivity.class);
        startActivity(intent);
    }
}
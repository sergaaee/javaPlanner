package com.example.planner.activities;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Patterns;
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
        EditText passwordReg2 = findViewById(R.id.regPass2);
        String password2 = passwordReg2.getText().toString();
        System.out.println(login.matches("^[A-Za-z\\d_.]+$"));
        if (login.length() <= 4 || !login.matches("^[A-Za-z\\d_.]+$") ){
            loginReg.setError(getString(R.string.a_z_1_9_username));
        }
        else if (password.length() < 8 || !password.matches("^[A-Za-z\\d_.]+$")) {
            passwordReg.setError(getString(R.string.a_z_1_9_password));
        }
        else if (!password.equals(password2)) {
            passwordReg.setError(getString(R.string.password_rnt_match));
            passwordReg2.setError(getString(R.string.password_rnt_match));
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailReg.setError(getString(R.string.invalid_email));
        }
        else {
            String response =  new User(login, password, email).postNewUser();
            if (response.equals("422"))
                makeText(RegActivity.this, getString(R.string.already_registered), LENGTH_LONG).show();
            else {
                makeText(RegActivity.this, getString(R.string.success_reg), LENGTH_LONG).show();
                startActivity(new Intent(RegActivity.this, AuthActivity.class));
            }
        }

    }


    public void backToLogIn(View view) {
        startActivity(new Intent(RegActivity.this, AuthActivity.class));
    }
}
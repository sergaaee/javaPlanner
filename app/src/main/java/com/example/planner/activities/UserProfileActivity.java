package com.example.planner.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.planner.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class UserProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_profile);
        viewBuilder();
    }

    private void viewBuilder(){
        Bundle extras = getIntent().getExtras();
        int userId = extras.getInt("userId");
        TextView Id = findViewById(R.id.textViewId);
        Id.setText(String.format("%s %s", Id.getText().toString(), userId));
        String username = extras.getString("username");
        TextView name = findViewById(R.id.textViewUsername);
        name.setText(String.format("%s %s", name.getText().toString(), username));
        String email = extras.getString("email");
        TextView emailField = findViewById(R.id.textViewEmail);
        emailField.setText(String.format("%s %s", emailField.getText().toString(), email));
        String regDate = LocalDate.parse(extras.getString("regDate").split("T")[0]).
                format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
        TextView regDateField = findViewById(R.id.textViewRegDate);
        regDateField.setText(String.format("%s %s", regDateField.getText(), regDate));

    }

    public void addFriend(View view){
        Intent intent = new Intent(UserProfileActivity.this,
                FriendActivity.class);
        startActivity(intent);
    }

    public void backToMainPageFromProfile(View view){

        Intent intent = new Intent(UserProfileActivity.this,
                MainPageActivity.class);
        startActivity(intent);

    }

    public void logOut(View view){
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.title_log_out))
                .setMessage(getString(R.string.exit_confirmation))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.ok, (dialog, whichButton) -> {
                    getSharedPreferences("activities.AuthActivity", MODE_PRIVATE).edit().putString("refresh_token", "null").apply();
                    getSharedPreferences("activities.AuthActivity", MODE_PRIVATE).edit().putString("access_token", "null").apply();
                    Intent intent = new Intent(UserProfileActivity.this,
                            AuthActivity.class);
                    startActivity(intent);
                })
                .setNegativeButton(android.R.string.cancel, null).show();
    }

}
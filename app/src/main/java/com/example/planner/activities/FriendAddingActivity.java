package com.example.planner.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.planner.R;
import com.example.planner.api.Friends;

import java.time.LocalDateTime;

public class FriendAddingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_friend_adding);
    }

    public void addNewFriend(View view){
        EditText editTextFriendId = findViewById(R.id.editTextFriendId);
        String friendId = editTextFriendId.getText().toString();
        if (!friendId.matches("^\\d*$")){
            editTextFriendId.setError(getResources().getString(R.string.not_correct_friend_id));
        }
        else {
            String response = Friends.addFriend(getSharedPreferences("activities.AuthActivity", MODE_PRIVATE).getString("access_token", "null"), friendId, LocalDateTime.now().toString());
            switch (response) {
                case "404":
                    Toast.makeText(this, getResources().getString(R.string.friend_not_found), Toast.LENGTH_LONG).show();
                    break;
                case "409":
                    Toast.makeText(this, getResources().getString(R.string.friend_already_exists), Toast.LENGTH_LONG).show();
                    break;
                case "Success":
                    Toast.makeText(this, getResources().getString(R.string.success), Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    public void backToFriend(View view) {
        startActivity(new Intent(FriendAddingActivity.this, AllFriendsActivity.class));
    }
}
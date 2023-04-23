package com.example.planner.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.planner.R;
import com.example.planner.api.Friends;

public class FriendProfileActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_friend_profile);
        viewBuilder();
    }

    public void backToFriendList(View view) {

        startActivity(new Intent(FriendProfileActivity.this, AllFriendsActivity.class));

    }

    private void viewBuilder(){
        Bundle extras = getIntent().getExtras();
        String userId = extras.getString("id");
        String status = extras.getString("status");
        if (!status.equals("added")){
            Button delFriend = findViewById(R.id.buttonDelFriend);
            delFriend.setVisibility(View.GONE);
            Button friendTasks = findViewById(R.id.buttonFriendTasks);
            friendTasks.setVisibility(View.GONE);
        }
        TextView friendId = findViewById(R.id.textViewId);
        friendId.setText(String.format("%s %s", friendId.getText().toString(), userId));
    }

    public void deleteFriend(View view) {
        Bundle extras = getIntent().getExtras();
        String userId = extras.getString("id");
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.delete_friend))
                .setMessage(getString(R.string.sure_delete_friend))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.ok, (dialog, whichButton) -> {
                    Friends.delFriend(getSharedPreferences("activities.AuthActivity", MODE_PRIVATE).getString("access_token", "null"), userId);
                    Intent intent = new Intent(FriendProfileActivity.this,
                            AllFriendsActivity.class);
                    startActivity(intent);
                })
                .setNegativeButton(android.R.string.cancel, null).show();
    }

    public void friendsTasks(View view) {
        Bundle extras = getIntent().getExtras();
        String userId = extras.getString("id");
        Intent intent = new Intent(FriendProfileActivity.this, FriendsTasksActivity.class);
        intent.putExtra("id", userId);
        intent.putExtra("status", extras.getString("status"));
        startActivity(intent);
    }
}
package com.example.planner.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.planner.R;
import com.example.planner.api.Friends;

import org.json.JSONArray;
import org.json.JSONException;

import java.time.LocalDateTime;

public class FriendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_friend);
        viewBuilding();
    }

    @SuppressLint("SetTextI18n")
    private void viewBuilding() {
        TextView confirmedFriends = new TextView(this);
        TextView pendingToUser = new TextView(this);
        TextView pendingFromUser = new TextView(this);
        LinearLayout layout = findViewById(R.id.linearLayoutAllFriendsList);
        try {
            JSONArray friend_list = parseJSONResponse(new JSONArray(Friends.getFriendList(getSharedPreferences("activities.AuthActivity", MODE_PRIVATE).getString("access_token", "null"))));
            JSONArray confirmedFriendsJson = friend_list.getJSONArray(0);
            JSONArray pendingToUserJson = friend_list.getJSONArray(1);
            JSONArray pendingFromUserJson = friend_list.getJSONArray(2);
            LinearLayout layoutConfirmed = new LinearLayout(this);
            layoutConfirmed.setOrientation(LinearLayout.VERTICAL);
            LinearLayout layoutPendingTo = new LinearLayout(this);
            layoutPendingTo.setOrientation(LinearLayout.VERTICAL);
            LinearLayout layoutFrom = new LinearLayout(this);
            layoutFrom.setOrientation(LinearLayout.VERTICAL);
            if (confirmedFriendsJson.getString(0).equals("empty")){
                confirmedFriends.setText(getResources().getString(R.string.confirmed_friends) + " (0)");
                layoutConfirmed.addView(confirmedFriends);
            }
            else {
                confirmedFriends.setText(getResources().getString(R.string.confirmed_friends) + " (" + confirmedFriendsJson.length() + ")");
                layoutConfirmed.addView(confirmedFriends);
                for (int i = 0; i < confirmedFriendsJson.length(); i++) {
                    TextView friendId = new TextView(this);
                    layoutConfirmed.addView(new TextView(this));
                    String friendIdStr = confirmedFriendsJson.getJSONObject(i).getString("friend_id");
                    friendId.setText(getResources().getString(R.string.friend_id) + " " + friendIdStr);
                    layoutConfirmed.addView(friendId);
                }
            }
            if (pendingToUserJson.getString(0).equals("empty")){
                pendingToUser.setText(getResources().getString(R.string.pending_to_user) + " (0)");
                layoutPendingTo.addView(pendingToUser);
            }
            else {
                pendingToUser.setText(getResources().getString(R.string.pending_to_user) + " (" + pendingToUserJson.length() + ")");
                layoutPendingTo.addView(pendingToUser);
                for (int i = 0; i < pendingToUserJson.length(); i++) {
                    TextView friendId = new TextView(this);
                    layoutPendingTo.addView(new TextView(this));
                    Button confirmFriend = new Button(this);
                    Button cancelFriend = new Button(this);
                    String friendIdStr = pendingToUserJson.getJSONObject(i).getString("user_id");
                    confirmFriend.setText(getString(R.string.confirm));
                    confirmFriend.setOnClickListener(v -> new AlertDialog.Builder(this)
                            .setTitle("")
                            .setMessage(getString(R.string.sure_delete_friend))
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.ok, (dialog, whichButton) -> {
                                Friends.confirmFriend(getSharedPreferences("activities.AuthActivity", MODE_PRIVATE).getString("access_token", "null"), friendIdStr, LocalDateTime.now().toString());
                                Intent intent = new Intent(FriendActivity.this,
                                        FriendActivity.class);
                                startActivity(intent);
                            })
                            .setNegativeButton(android.R.string.cancel, null).show());
                    cancelFriend.setText(getString(com.google.ar.core.R.string.__arcore_cancel));
                    cancelFriend.setOnClickListener(v -> new AlertDialog.Builder(this)
                            .setTitle("")
                            .setMessage(getString(R.string.sure_delete_friend))
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.ok, (dialog, whichButton) -> {
                                Friends.delFriend(getSharedPreferences("activities.AuthActivity", MODE_PRIVATE).getString("access_token", "null"), friendIdStr);
                                Intent intent = new Intent(FriendActivity.this,
                                        FriendActivity.class);
                                startActivity(intent);
                            })
                            .setNegativeButton(android.R.string.cancel, null).show());
                    friendId.setText(getResources().getString(R.string.id) + " " + friendIdStr);
                    layoutPendingTo.addView(friendId);
                    layoutPendingTo.addView(confirmFriend);
                    layoutPendingTo.addView(cancelFriend);
                }
            }
            if (pendingFromUserJson.getString(0).equals("empty")){
                pendingFromUser.setText(getResources().getString(R.string.pending_from_user) + " (0)");
                layoutFrom.addView(pendingFromUser);
            }
            else {
                pendingFromUser.setText(getResources().getString(R.string.pending_from_user) + " (" + pendingFromUserJson.length() + ")");
                layoutFrom.addView(pendingFromUser);
                for (int i = 0; i < pendingFromUserJson.length(); i++) {
                    TextView friendId = new TextView(this);
                    layoutFrom.addView(new TextView(this));
                    Button cancelRequest = new Button(this);
                    String friendIdStr = pendingFromUserJson.getJSONObject(i).getString("friend_id");
                    cancelRequest.setText("cancel");
                    cancelRequest.setOnClickListener(v -> new AlertDialog.Builder(this)
                            .setTitle("")
                            .setMessage(getString(R.string.sure_delete_friend))
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.ok, (dialog, whichButton) -> {
                                Friends.delFriend(getSharedPreferences("activities.AuthActivity", MODE_PRIVATE).getString("access_token", "null"), friendIdStr);
                                Intent intent = new Intent(FriendActivity.this,
                                        FriendActivity.class);
                                startActivity(intent);
                            })
                            .setNegativeButton(android.R.string.cancel, null).show());
                    friendId.setText(getResources().getString(R.string.id) + " " + friendIdStr);
                    layoutFrom.addView(friendId);
                    layoutFrom.addView(cancelRequest);
                }
            }
            layout.addView(layoutConfirmed);
            layout.addView(new TextView(this));
            layout.addView(layoutPendingTo);
            layout.addView(new TextView(this));
            layout.addView(layoutFrom);
        } catch (JSONException e) {
            throw new RuntimeException(e);
            //Toast.makeText(this, getResources().getString(R.string.went_wrong), Toast.LENGTH_LONG).show();
        }

    }

    private JSONArray parseJSONResponse(JSONArray response){
        JSONArray friend_list = new JSONArray();
        JSONArray empty = new JSONArray();
        empty.put("empty");
        if (response.length() > 0) {
            for (int i = 0; i < response.length(); i++) {
                try {
                    if (response.getJSONArray(i).length() > 0){
                        friend_list.put(response.getJSONArray(i));
                    }
                    else {
                        friend_list.put(empty);
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

        }
        return friend_list;
    }

    public void backToMainPageFromFriend(View view) {
        startActivity(new Intent(FriendActivity.this, MainPageActivity.class));
    }

    public void toFriendAdd(View view) {
        startActivity(new Intent(FriendActivity.this, FriendAddingActivity.class));
    }
}
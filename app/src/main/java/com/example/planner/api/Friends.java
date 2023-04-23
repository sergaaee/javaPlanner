package com.example.planner.api;

import androidx.annotation.NonNull;

import org.json.JSONException;

import java.io.IOException;


public class Friends extends ApiUsage{

    @NonNull
    public static String addFriend(String token, String friendId, String createdAt){

        String check;

        try {
            check = postMethod("friend",
                    token,
                    new String[]{"friend_id", "created_at"},
                    new String[]{friendId, createdAt},
                    new String[]{""},
                    new String[]{""});
            switch (check){
                case "401":
                    return "401";
                case "409":
                    return "409";
                case "404":
                    return "404";
            }
        } catch (JSONException | IOException e) {
            throw new RuntimeException(e);
        }
        return "Success";

    }

    public static String getFriendList(String token){
        String check;

        try {
            check = getMethod("friend",
                    token,
                    new String[]{""},
                    new String[]{""});
        } catch (JSONException | IOException e) {
            throw new RuntimeException(e);
        }
        return check;
    }

    public static void confirmFriend(String token, String friendId, String createdAt){
        try {
            patchMethod("friend",
                    token,
                    new String[]{"friend_id", "created_at"},
                    new String[]{friendId, createdAt},
                    new String[]{""},
                    new String[]{""});
        } catch (JSONException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void delFriend(String token, String friendId){

        try {
            delMethod("friend",
                    token,
                    new String[]{"friend_id"},
                    new String[]{friendId},
                    new String[]{""},
                    new String[]{""});
        } catch (JSONException | IOException e) {
            throw new RuntimeException(e);
        }

    }


    public static String getFriendsTasks(String token, String friendId){



        String result = "";
        try {
             result = getMethod("friend/tasks",
                    token,
                    new String[]{"friend-id"},
                    new String[]{friendId});
        } catch (JSONException | IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

}

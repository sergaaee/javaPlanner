package com.example.planner.api;

import androidx.annotation.NonNull;


public class Friends{

    @NonNull
    public static String addFriend(String token, String friendId, String createdAt)
    {
        PostRequest postRequest = new PostRequest("friend",
                token,
                new String[]{"friend_id", "created_at"},
                new String[]{friendId, createdAt},
                new String[]{""},
                new String[]{""});
        String response = postRequest.execute();
        switch (response)
        {
            case "401":
                return "401";
            case "409":
                return "409";
            case "404":
                return "404";
        }
        return "Success";
    }

    public static String getFriendList(String token){
        GetRequest getRequest = new GetRequest("friend",
                token,
                new String[]{""},
                new String[]{""});
        return getRequest.execute();
    }

    public static void confirmFriend(String token, String friendId, String createdAt)
    {
        PatchRequest patchRequest = new PatchRequest("friend",
                token,
                new String[]{"friend_id", "created_at"},
                new String[]{friendId, createdAt},
                new String[]{""},
                new String[]{""});
        patchRequest.execute();
    }

    public static void delFriend(String token, String friendId)
    {
        DeleteRequest deleteRequest = new DeleteRequest("friend",
                token,
                new String[]{"friend_id"},
                new String[]{friendId},
                new String[]{""},
                new String[]{""});
        deleteRequest.execute();
    }


    public static String getFriendsTasks(String token, String friendId)
    {
        GetRequest getRequest = new GetRequest("friend/tasks",
                token,
                new String[]{"friend-id"},
                new String[]{friendId});
        return getRequest.execute();
    }

    public static String addTaskToFriend(String token, String friendId, String name, String stime, String etime, String desc, String status, String createdAt){
        PostRequest postRequest = new PostRequest("friend/tasks",
                token,
                new String[]{"name", "start_time", "end_time", "description", "status", "created_at", "sharing_to"},
                new String[]{name, stime, etime, desc, status, createdAt, "0"},
                new String[]{"friend-id"},
                new String[]{friendId});
        String response = postRequest.execute();
        if (response.equals("401")){ return "401"; }
        if (response.equals("409")){ return "409"; }
        return "Success";
    }

}

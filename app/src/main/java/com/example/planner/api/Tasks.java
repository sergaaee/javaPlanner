package com.example.planner.api;

import androidx.annotation.NonNull;

public class Tasks
{

    @NonNull
    public static String taskNew(String token, String name, String stime, String etime, String desc, String status, String createdAt, String sharing_to)
    {

        PostRequest postRequest = new PostRequest("tasks",
                token,
                new String[]{"name", "start_time", "end_time", "description", "status", "created_at", "sharing_to", "sharing_from"},
                new String[]{name, stime, etime, desc, status, createdAt, sharing_to, "0"},
                new String[]{""},
                new String[]{""});
        String response = postRequest.execute();
        if (response.equals("401")){ return "401"; }
        if (response.equals("409")){ return "409"; }
        return "Success";
    }


    public static String getAllTasks(String token)
    {
        GetRequest getRequest = new GetRequest("tasks",
                token,
                new String[]{""},
                new String[]{""});
        return getRequest.execute();
    }


    @NonNull
    public static String taskUpdate(String token, String prevName, String newName, String sTime, String eTime, String desc, String status)
    {
        PatchRequest patchRequest = new PatchRequest("tasks",
                token,
                new String[]{"name", "new_name", "new_stime", "new_etime", "new_desc", "new_status"},
                new String[]{prevName, newName, sTime, eTime, desc, status},
                new String[]{""},
                new String[]{""});
        String result = patchRequest.execute();
        if (result.equals("401")){ return "401"; }
        if (result.equals("409")){ return "409"; }
        return "Success";
    }


    public static void taskDel(String token, String name)
    {
        DeleteRequest deleteRequest = new DeleteRequest("tasks",
                token,
                new String[]{"name"},
                new String[]{name},
                new String[]{""},
                new String[]{""});
        deleteRequest.execute();
    }


}

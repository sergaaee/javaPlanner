package com.example.planner.api;

import androidx.annotation.NonNull;

import org.json.JSONException;

import java.io.IOException;

public class Tasks extends ApiUsage {

    @NonNull
    public static String taskNew(String token, String name, String stime, String etime, String desc, String status, String createdAt){

        String check;
        try {
            check = postMethod("tasks",
                    token,
                    new String[]{"name", "start_time", "end_time", "description", "status", "created_at"},
                    new String[]{name, stime, etime, desc, status, createdAt},
                    new String[]{""},
                    new String[]{""});
            if (check.equals("401")){ return "401"; }
            if (check.equals("409")){ return "409"; }
        } catch (JSONException | IOException e) {
            throw new RuntimeException(e);
        }
        return "Success";
    }


    @NonNull
    public static String taskUpdate(String token, String prevName, String newName, String sTime, String eTime, String desc, String status){
        String check;
        try {
            check = patchMethod("tasks",
                    token,
                    new String[]{"name", "new_name", "new_stime", "new_etime", "new_desc", "new_status"},
                    new String[]{prevName, newName, sTime, eTime, desc, status},
                    new String[]{""},
                    new String[]{""});
            if (check.equals("401")){ return "401"; }
            if (check.equals("409")){ return "409"; }
        } catch (JSONException | IOException e) {
            throw new RuntimeException(e);
        }
        return "Success";
    }


    public static void taskDel(String token, String name){

        try {
            delMethod("tasks",
                    token,
                    new String[]{"name"},
                    new String[]{name},
                    new String[]{""},
                    new String[]{""});
        } catch (JSONException | IOException e) {
            throw new RuntimeException(e);
        }
    }


}

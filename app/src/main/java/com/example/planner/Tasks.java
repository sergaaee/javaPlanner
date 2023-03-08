package com.example.planner;

import androidx.annotation.NonNull;

import org.json.JSONException;

import java.io.IOException;

public class Tasks extends ApiUsage {

    @NonNull
    public static String taskNew(String token, String name, String stime, String etime, String desc){

        String check = "";
        try {
            check = postMethod("tasks",
                    new String[]{"name", "start_time", "end_time", "description"},
                    new String[]{name, stime, etime, desc},
                    new String[]{"Authorization"},
                    new String[]{"Bearer " + token});
            if (check.equals("401")){ return "401"; }
        } catch (JSONException | IOException e) {
            throw new RuntimeException(e);
        }
        return "Success";
    }


    public static void taskDel(String token, String name){

        String check = "";
        try {
            check = postMethod("tasks",
                    new String[]{"name"},
                    new String[]{name},
                    new String[]{"Authorization"},
                    new String[]{"Bearer " + token});
        } catch (JSONException | IOException e) {
            throw new RuntimeException(e);
        }
    }


}

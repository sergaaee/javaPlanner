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
                    token,
                    new String[]{"name", "start_time", "end_time", "description"},
                    new String[]{name, stime, etime, desc},
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
    public static String taskUpdate(String token, String prevName, String newName, String stime, String etime, String desc){
        String check = "";
        try {
            check = patchMethod("tasks",
                    token,
                    new String[]{"name", "new_name", "new_stime", "new_etime", "new_desc"},
                    new String[]{prevName, newName, stime, etime, desc},
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

        String check = "";
        try {
            check = delMethod("tasks",
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

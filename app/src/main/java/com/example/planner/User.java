package com.example.planner;

import androidx.annotation.NonNull;

public class User extends ApiUsage{

    private final String username;
    private final String password;
    private final String email;

    public User(String username, String password, String email){
        this.username = username;
        this.password = password;
        this.email = email;
    }

    @NonNull
    public String postNewUser()
            throws Exception {

        return postMethod("users",
                "",
                new String[]{"username", "password", "email"},
                new String[]{username, password, email},
                new String[]{""},
                new String[]{""});

    }

}

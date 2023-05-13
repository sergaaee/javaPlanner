package com.example.planner.api;

import androidx.annotation.NonNull;

public class User
{

    private final String username;
    private final String password;
    private final String email;

    public User(String username, String password, String email)
    {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    @NonNull
    public String postNewUser()
    {
        PostRequest postRequest = new PostRequest("users",
                "",
                new String[]{"username", "password", "email"},
                new String[]{username, password, email},
                new String[]{""},
                new String[]{""});
        return postRequest.execute();
    }

}

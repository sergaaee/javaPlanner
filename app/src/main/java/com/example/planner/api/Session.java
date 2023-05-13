package com.example.planner.api;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.planner.activities.AuthActivity;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Session extends MethodBuilder{

    String username = "";
    String password = "";
    String refresh_token = AuthActivity.getSharedPref().getString("refresh_token", "null");
    String fingerprint = AuthActivity.getFingerprint();
    public Session(String username, String password) {
        this.username = username;
        this.password = password;
    }
    //for refresh token
    public Session(){
    }


    @NonNull
    public String createNewSession() {

        RequestBody formBody = new FormBody.Builder().add("username", username).add("password", password).build();
        Request request = new Request.Builder()
                .url(DEFAULT_URL + "tokens")
                .addHeader("accept", "application/json")
                .addHeader("fingerprint", fingerprint)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .post(formBody)
                .build();

        Callable<String> getSession = () ->
        {
            Response response = client.newCall(request).execute();
            if (response.code() != 200)
            {
                return String.valueOf(response.code());
            }
            return response.body() != null ? response.body().string() : null;
        };
        ExecutorService service = Executors.newFixedThreadPool(2);
        Future<String> response = service.submit(getSession);
        try {
            return response.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    @Nullable
    public String refreshAccessToken() throws ExecutionException, InterruptedException {
        if (refresh_token.equals("null")){
            return "Error";
        }
        Request request = new Request.Builder()
                .url(DEFAULT_URL + "tokens/refresh")
                .addHeader("Accept", "application/json")
                .addHeader("fingerprint", fingerprint)
                .addHeader("refresh-token", refresh_token)
                .post(new FormBody.Builder().build())
                .build();
        Callable<String> getSession = () ->
        {
            Response response = client.newCall(request).execute();
            if (response.code() != 200) {
                if (response.code() == 401 || response.code() == 422) {
                    return "Error";
                }
            }
            return response.body().string();
        };
        ExecutorService service = Executors.newFixedThreadPool(2);
        Future<String> response = service.submit(getSession);
        return response.get();

    }

    @Override
    protected String execute() {
        // TODO normal working method
        return null;
    }
}

package com.example.planner;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Session extends ApiUsage{

    String username = "";
    String password = "";
    String refresh_token = AuthActivity.getRefreshToken();
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
        try (Response response = client.newCall(request).execute()) {
            if (response.code() != 200){ return String.valueOf(response.code()); }
            return response.body() != null ? response.body().string() : null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    @Nullable
    public String refreshAccessToken() throws IOException {
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
        Response response = client.newCall(request).execute();
        if (response.code() != 200) {
            if (response.code() == 401 || response.code() == 422) {
                return "Error";
            }
        }
        return response.body().string();
    }
}

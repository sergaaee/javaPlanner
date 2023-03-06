package com.example.planner;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Session extends ApiUsage{

    String username = "";
    String password = "";
    String refresh_token = MainActivity.getRefreshToken();;
    String fingerprint = MainActivity.getFingerprint();;
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
    public String refreshAccessToken(){
        try {
            return postMethod("tokens/refresh",
                    new String[]{""},
                    new String[]{""},
                    new String[]{"fingerprint", "refresh-token"},
                    new String[]{fingerprint, refresh_token});
        } catch (JSONException | IOException e) {
            return "401";
        }
    }
}

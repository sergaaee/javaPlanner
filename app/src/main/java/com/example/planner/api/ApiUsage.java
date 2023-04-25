package com.example.planner.api;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiUsage {
    final static OkHttpClient client = new OkHttpClient();
    final static String DEFAULT_URL = "http://192.168.1.46:8000/";

    private static String methodBuilder(String path, String method, String token, @NonNull String[] bodyColumns, @NonNull String[] bodyData, @NonNull String[] headersColumns, @NonNull String[] headersData)
            throws JSONException, IOException, AssertionError {

        Request.Builder builder = new Request.Builder();
        builder.url(DEFAULT_URL + path);
        builder.addHeader("accept", "application/json");
        // if no body
        if (bodyColumns[0].equals("")) {
            if (method.equals("GET")){
                builder.get();
            }
            else {
                builder.method(method, new FormBody.Builder().build());
            }
        } // body exists
        else {
            JSONObject jsonBody = new JSONObject();
            for (int i = 0; i < bodyColumns.length; i++)
                jsonBody.put(bodyColumns[i], bodyData[i]);
            RequestBody requestJsonBody = RequestBody.create(
                    jsonBody.toString(),
                    MediaType.parse("application/json")
            );
            builder.method(method, requestJsonBody);
        }
        if (token.length() > 1){
            builder.addHeader("Authorization", "Bearer " + token);
        }
        // headers exist
        if (!headersColumns[0].equals(""))
            for (int i = 0; i < headersColumns.length; i++)
                builder.addHeader(headersColumns[i], headersData[i]);
        builder.addHeader("check", "check");
        // building request
        Request request = builder
                .build();
        Response response = client.newCall(request).execute();
        if (response.code() != 200) {
            if (response.code() == 401) {
                return "401";
            }
            return String.valueOf(response.code());
        }
        assert response.body() != null : "";
        return response.body().string();

    }

    @NonNull
    protected static String postMethod(String path, String token, @NonNull String[] bodyColumns, @NonNull String[] bodyData, @NonNull String[] headersColumns, @NonNull String[] headersData)
            throws JSONException, IOException, AssertionError {

        String response = methodBuilder(path, "POST", token, bodyColumns, bodyData, headersColumns, headersData);
        if (response.equals("401")){
            token = LogIn.updateAccessToken();
            if (!token.equals("Error")){
                return methodBuilder(path, "POST", token, bodyColumns, bodyData, headersColumns, headersData);
            }
        }
        return response;

    }

    @NonNull
    protected static String patchMethod(String path, String token, @NonNull String[] bodyColumns, @NonNull String[] bodyData, @NonNull String[] headersColumns, @NonNull String[] headersData)
            throws JSONException, IOException, AssertionError {

        String response = methodBuilder(path, "PATCH", token, bodyColumns, bodyData, headersColumns, headersData);
        if (response.equals("401")){
            token = LogIn.updateAccessToken();
            if (!token.equals("Error")){
                return methodBuilder(path, "PATCH", token, bodyColumns, bodyData, headersColumns, headersData);
            }
        }
        return response;

    }

    @NonNull
    public static String getMethod(String path, String token, @NonNull String[] headersColumns, @NonNull String[] headersData)
            throws JSONException, IOException, AssertionError {

        String response = methodBuilder(path, "GET", token, new String[]{""}, new String[]{""}, headersColumns, headersData);
        if (response.equals("401")){
            token = LogIn.updateAccessToken();
            if (!token.equals("Error")){
                return methodBuilder(path, "GET", token, new String[]{""}, new String[]{""}, headersColumns, headersData);
            }
        }
        return response;
    }

    @NonNull
    protected static String delMethod(String path, String token, @NonNull String[] bodyColumns, @NonNull String[] bodyData, @NonNull String[] headersColumns, @NonNull String[] headersData)
    throws JSONException, IOException, AssertionError {

        String response = methodBuilder(path, "DELETE", token, bodyColumns, bodyData, headersColumns, headersData);
        if (response.equals("401")){
            token = LogIn.updateAccessToken();
            if (!token.equals("Error")){
                return methodBuilder(path, "DELETE", token, bodyColumns, bodyData, headersColumns, headersData);
            }
        }
        return response;

    }
}
package com.example.planner;

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
    final static String DEFAULT_URL = "http://10.0.2.2:8000/";

    private static String methodBuilder(String path, String method, @NonNull String[] bodyColumns, @NonNull String[] bodyData, @NonNull String[] headersColumns, @NonNull String[] headersData)
            throws JSONException, IOException, AssertionError {

        Request.Builder builder = new Request.Builder();
        builder.url(DEFAULT_URL + path);
        builder.addHeader("accept", "application/json");
        // if no body
        if (bodyColumns[0].equals("")) {
            builder.method(method, new FormBody.Builder().build());
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
        // headers exist
        if (!headersColumns[0].equals(""))
            for (int i = 0; i < headersColumns.length; i++)
                builder.addHeader(headersColumns[i], headersData[i]);
        // building request
        Request request = builder
                .build();
        Response response = client.newCall(request).execute();
        if (response.code() != 200) {
            if (response.code() == 401) {
                String check = new Session().refreshAccessToken();
                if (!check.equals("401")) {
                    methodBuilder(method, path, bodyColumns, bodyData, headersColumns, headersData);
                } else {
                    return String.valueOf(response.code());
                }
            }
            return String.valueOf(response.code());
        }
        assert response.body() != null : "";
        return response.body().string();

    }

    @NonNull
    protected static String postMethod(String path, @NonNull String[] bodyColumns, @NonNull String[] bodyData, @NonNull String[] headersColumns, @NonNull String[] headersData)
            throws JSONException, IOException, AssertionError {

        return methodBuilder(path, "POST", bodyColumns, bodyData, headersColumns, headersData);

    }

    @NonNull
    protected static String patchMethod(String path, @NonNull String[] bodyColumns, @NonNull String[] bodyData, @NonNull String[] headersColumns, @NonNull String[] headersData)
            throws JSONException, IOException, AssertionError {

        return methodBuilder(path, "PATCH", bodyColumns, bodyData, headersColumns, headersData);

    }

    @NonNull
    protected static String getMethod(String path, @NonNull String[] bodyColumns, @NonNull String[] bodyData, @NonNull String[] headersColumns, @NonNull String[] headersData)
            throws JSONException, IOException, AssertionError {

        return methodBuilder(path, "GET", bodyColumns, bodyData, headersColumns, headersData);

    }

    @NonNull
    protected static String delMethod(String path, @NonNull String[] bodyColumns, @NonNull String[] bodyData, @NonNull String[] headersColumns, @NonNull String[] headersData)
    throws JSONException, IOException, AssertionError {

        return methodBuilder(path, "DELETE", bodyColumns, bodyData, headersColumns, headersData);

    }
}
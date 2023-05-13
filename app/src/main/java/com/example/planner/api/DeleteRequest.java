package com.example.planner.api;

import androidx.annotation.NonNull;

import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DeleteRequest extends MethodBuilder{



    public DeleteRequest(String path, String token, @NonNull String[] bodyColumns, @NonNull String[] bodyData, @NonNull String[] headersColumns, @NonNull String[] headersData)
    {
        this.path = path;
        this.token = token;
        this.bodyColumns = bodyColumns;
        this.bodyData = bodyData;
        this.headersColumns = headersColumns;
        this.headersData = headersData;
    }


    @Override
    protected String execute()
    {
        builder.url(DEFAULT_URL + path);
        builder.addHeader("accept", "application/json");
        // if no body
        if (bodyColumns[0].equals("")) {
            builder.method("DELETE", new FormBody.Builder().build());
        }
        // body exists
        else {
            for (int i = 0; i < bodyColumns.length; i++) {
                try {
                    jsonBody.put(bodyColumns[i], bodyData[i]);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
            RequestBody requestJsonBody = RequestBody.create(
                    jsonBody.toString(),
                    MediaType.parse("application/json")
            );
            builder.method("DELETE", requestJsonBody);
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
        Callable<String> getResponse = () ->
        {
            Response response = client.newCall(request).execute();
            if (response.code() != 200)
            {
                if (response.code() == 401)
                {
                    return "401";
                }
                return String.valueOf(response.code());
            }
            assert response.body() != null : "";
            return response.body().string();
        };
        ExecutorService service = Executors.newFixedThreadPool(2);
        Future<String> response = service.submit(getResponse);
        try
        {
            String result = response.get();
            if (result.equals("401")){
                token = LogIn.updateAccessToken();
                if (!token.equals("Error")){
                    return execute();
                }
            }
            return result;
        } catch (ExecutionException | IOException | InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }

}

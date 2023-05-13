package com.example.planner.api;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import okhttp3.Request;
import okhttp3.Response;

public class GetRequest extends MethodBuilder{


    public GetRequest(String path, String token, @NonNull String[] headersColumns, @NonNull String[] headersData)
    {
        this.path = path;
        this.token = token;
        this.headersColumns = headersColumns;
        this.headersData = headersData;
    }

    @Override
    public String execute() {
        builder.url(DEFAULT_URL + path);
        builder.get();
        if (token.length() > 1){
            builder.addHeader("Authorization", "Bearer " + token);
        }
        // headers exist
        if (!headersColumns[0].equals(""))
            for (int i = 0; i < headersColumns.length; i++)
                builder.addHeader(headersColumns[i], headersData[i]);
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
        } catch (ExecutionException | InterruptedException | IOException e)
        {
            throw new RuntimeException(e);
        }

    }
}

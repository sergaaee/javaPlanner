package com.example.planner.api;

import okhttp3.OkHttpClient;

public class OkHttpSingleton {

    private static OkHttpSingleton singletonInstance;

    // No need to be static; OkHttpSingleton is unique so is this.
    private final OkHttpClient client;

    // Private so that this cannot be instantiated.
    private OkHttpSingleton() {
        client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .build();
    }

    public static OkHttpSingleton getInstance() {
        if (singletonInstance == null) {
            singletonInstance = new OkHttpSingleton();
        }
        return singletonInstance;
    }

    public OkHttpClient getClient() {
        return client;
    }
}
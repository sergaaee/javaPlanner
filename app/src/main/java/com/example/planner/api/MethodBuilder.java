package com.example.planner.api;

import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public abstract class MethodBuilder {

    OkHttpClient client = OkHttpSingleton.getInstance().getClient();
    final String DEFAULT_URL = "http://192.168.1.46:8000/";
    Request.Builder builder = new Request.Builder();
    JSONObject jsonBody = new JSONObject();

    protected String path;
    protected String token;
    protected String[] headersColumns;
    protected String[] headersData;
    protected String[] bodyColumns;
    protected String[] bodyData;

    protected abstract String execute();

}

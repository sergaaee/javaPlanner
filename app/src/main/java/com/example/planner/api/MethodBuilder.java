package com.example.planner.api;

import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public abstract class MethodBuilder {

    OkHttpClient client = OkHttpSingleton.getInstance().getClient();
    final String DEFAULT_URL = "http://kamaliev.asuscomm.com/sergaee/";
    //final String DEFAULT_URL = "http://172.20.10.3:8000/";
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

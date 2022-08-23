package com.example.tracker3.util;

import android.content.Context;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpRequest {

    private static final String TAG = "HttpRequest";
    public static final String BASE_URL ="https://tcc-cco-tracker.herokuapp.com";
    public static final String RESEARCH_ENDPOINT = BASE_URL + "/pesquisas";
    public static final String LOGIN_ENDPOINT = BASE_URL + "/token";
    public static final String CREATE_ENDPOINT = "/create";


    public static Request getRequestBuilder(String endpoint, String token) {
        return new Request.Builder().url(endpoint).addHeader("Authorization",
                "Bearer " + token).build();
    }


    public static Request localAuthBuilder(String userName, String password) {
        RequestBody requestBody = new FormBody.Builder()
                .add("grant_type", "")
                .add("username", userName)
                .add("password", password )
                .add("scope", "")
                .add("client_id", "")
                .add("client_secret", "")
                .build();
        return new Request.Builder().url(LOGIN_ENDPOINT).post(requestBody).build();
    }

}

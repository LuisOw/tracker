package com.example.tracker3.util;


import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpRequest {

    private static final String TAG = "HttpRequest";
    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json");
    public static final String BASE_URL = "https://tcc-cco-tracker.herokuapp.com";
    public static final String SUBJECT = BASE_URL + "/participantes";
    public static final String RESEARCH_ENDPOINT = SUBJECT + "/pesquisas";
    public static final String NEW_RESEARCH_ENDPOINT = SUBJECT + "/pesquisas-filtradas";
    public static final String NEW_ACCOUNT_ENDPOINT = BASE_URL + "/participante";
    public static final String LOGIN_ENDPOINT = BASE_URL + "/participante/token";
    public static final String SUBJECT_QUESTIONNAIRES = SUBJECT + "/pesquisas/%s/questionarios";
    public static final String SUBJECT_QUESTION = SUBJECT_QUESTIONNAIRES + "/%s/questoes";


    public static Request getRequestBuilder(String endpoint, String token) {
        return new Request.Builder().url(endpoint).addHeader("Authorization",
                "Bearer " + token).build();
    }

    public static Request patchRequestBuilder(String endpoint, String token, String json) {
        return new Request.Builder()
                .url(endpoint)
                .addHeader("Authorization","Bearer " + token)
                .patch(RequestBody.create(MEDIA_TYPE_JSON, json))
                .build();
    }

    public static Request postRequestBuilder(String endpoint, String token, String json) {
        return new Request.Builder()
                .url(endpoint)
                .addHeader("Authorization","Bearer " + token)
                .post(RequestBody.create(MEDIA_TYPE_JSON, json))
                .build();
    }

    public static Request localAuthBuilder(String userName, String password, String url) {
        RequestBody requestBody = new FormBody.Builder()
                .add("grant_type", "")
                .add("username", userName)
                .add("password", password )
                .add("scope", "")
                .add("client_id", "")
                .add("client_secret", "")
                .build();
        return new Request.Builder().url(url).post(requestBody).build();
    }

}

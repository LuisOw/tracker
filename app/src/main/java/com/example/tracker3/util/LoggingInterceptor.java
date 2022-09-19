package com.example.tracker3.util;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;

public class LoggingInterceptor implements Interceptor {

    private static final String TAG = "Interceptor";

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        Buffer buffer = new Buffer();
        request.body().writeTo(buffer);
        Log.e(TAG, "Request = " + buffer.readUtf8());
        Response response = chain.proceed(request);
        Log.e(TAG, "logging " + response.headers());
        Log.e(TAG, "logging2 " + response.body());
        return response;
    }
}

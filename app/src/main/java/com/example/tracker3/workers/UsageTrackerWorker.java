package com.example.tracker3.workers;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.tracker3.util.HttpRequest;
import com.example.tracker3.util.SharedPreferencesUtils;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UsageTrackerWorker extends Worker {

    public UsageTrackerWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        uploadUsageTime();
        return Result.success();
    }

    void uploadUsageTime() {
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        UsageStatsManager mUsageStatsManager =
                (UsageStatsManager) getApplicationContext().getSystemService(Context.USAGE_STATS_SERVICE);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        long startMillis, endMillis;
        startMillis = cal.getTimeInMillis();
        endMillis = System.currentTimeMillis();
        Map<String, UsageStats> lUsageStatsMap = mUsageStatsManager.
                queryAndAggregateUsageStats(startMillis, endMillis);
        Map<String, String> answerMap = new HashMap<>();
        StringBuilder aws = new StringBuilder();
        Set<String> lUsageSet = lUsageStatsMap.keySet();
        for (String name : lUsageSet) {
            if (Objects.requireNonNull(lUsageStatsMap.get(name)).getTotalTimeInForeground() > 0) {
                aws.append(name).append(": ");
                aws.append(timeConvert(lUsageStatsMap.get(name).getTotalTimeInForeground())).append("\n");
            }
        }
        String collectedTime = gson.toJson(aws);
        answerMap.put("collected_time", collectedTime);

        SharedPreferences localSharesPreferences = getApplicationContext().getSharedPreferences("account" , Context.MODE_PRIVATE);
        String jwtToken = localSharesPreferences.getString(SharedPreferencesUtils.TOKEN_KEY, "");
        Request request = HttpRequest.postRequestBuilder(
                HttpRequest.SUBJECT_USAGE_TIME, jwtToken, gson.toJson(answerMap)
        );
        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response){
            }
        });
    }

    private String timeConvert(long totalTime) {

        long seconds = (totalTime/1000)%60;
        long minutes = (totalTime/(1000*60))%60;
        long hours = (totalTime/(1000*60*60))%24;

        return hours + ":" + minutes + ":" + seconds;
    }
}

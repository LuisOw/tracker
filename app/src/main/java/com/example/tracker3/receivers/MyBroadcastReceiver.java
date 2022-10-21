package com.example.tracker3.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

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

public class MyBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "MyBroadcastReceiver";
    private AlarmManager alarmManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "Broadcast received");
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Log.e(TAG, "Boot broadcast");
            Intent intentForAlarm = new Intent(context, MyBroadcastReceiver.class);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intentForAlarm, PendingIntent.FLAG_IMMUTABLE);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 24);
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
        } else {
            Log.e(TAG, "Alarm broadcast");
            Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create();
            UsageStatsManager mUsageStatsManager =
                    (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
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
                if (Objects.equals(name, "com.example.tracker3")) {
                    if (Objects.requireNonNull(lUsageStatsMap.get(name)).getTotalTimeInForeground() > 0) {
                        aws.append(name).append(": ");
                        aws.append(timeConvert(lUsageStatsMap.get(name).getTotalTimeInForeground())).append("\n");
                    }
                }
            }
            String collectedTime = gson.toJson(aws);
            answerMap.put("collected_time", collectedTime);

            Intent intentForAlarm = new Intent(context, MyBroadcastReceiver.class);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0,
                    intentForAlarm, PendingIntent.FLAG_IMMUTABLE);
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, 20);
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    alarmIntent);

            SharedPreferences localSharesPreferences = context.getSharedPreferences("account", Context.MODE_PRIVATE);
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
                public void onResponse(@NonNull Call call, @NonNull Response response) {
                }
            });
        }
    }

    private String timeConvert(long totalTime) {

        long seconds = (totalTime/1000)%60;
        long minutes = (totalTime/(1000*60))%60;
        long hours = (totalTime/(1000*60*60))%24;

        return hours + ":" + minutes + ":" + seconds;
    }
}

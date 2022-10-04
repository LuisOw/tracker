package com.example.tracker3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.example.tracker3.adapter.ResearchAdapter;
import com.example.tracker3.domain.Research;
import com.example.tracker3.domain.User;
import com.example.tracker3.util.ClickListener;
import com.example.tracker3.util.HttpRequest;
import com.example.tracker3.util.SharedPreferencesUtils;
import com.example.tracker3.workers.UsageTrackerWorker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ResearchActivity extends BaseActivity implements ClickListener {

    private static final String TAG = "ResearchActivity";
    private String UNIQUE_WORKER = "usageTrackerWorker";
    ArrayList<Research> researches;
    private User user;
    private String jwtToken;
    private SharedPreferences localSharesPreferences;
    private OkHttpClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Gson gson = new Gson();
        Intent intent = getIntent();
        String researchesAsJson = intent.getStringExtra("researches");
        Type type = new TypeToken<Collection<Research>>(){}.getType();
        Collection<Research> researchesCollection = gson.fromJson(researchesAsJson, type);
        this.researches = new ArrayList<>(researchesCollection);

        setContentView(R.layout.active_researches);
        RecyclerView rvResearches = findViewById(R.id.rv_research);
        ResearchAdapter adapter = new ResearchAdapter(this.researches, this);
        rvResearches.setAdapter(adapter);
        rvResearches.setLayoutManager(new LinearLayoutManager(this));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        localSharesPreferences = getSharedPreferences("account" ,Context.MODE_PRIVATE);
        this.jwtToken = localSharesPreferences.getString(SharedPreferencesUtils.TOKEN_KEY, "");
        client = new OkHttpClient();
        Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();
        PeriodicWorkRequest usageTimeWorkRequest =
                new PeriodicWorkRequest
                        .Builder(UsageTrackerWorker.class, 4, TimeUnit.HOURS)
                        .setConstraints(constraints)
                        .build();
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(UNIQUE_WORKER, ExistingPeriodicWorkPolicy.KEEP, usageTimeWorkRequest);
    }


    private void retrieveData(int researchId) {
        Log.e(TAG, "get questionnaires");
        Request request = HttpRequest.getRequestBuilder(
                String.format(HttpRequest.SUBJECT_QUESTIONNAIRES, researchId), this.jwtToken
        );
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                ResearchActivity.this.runOnUiThread(() -> Toast.makeText(ResearchActivity.this,
                        "Erro pegando questionarios.", Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.code() == 200) {
                    Intent intent = new Intent(ResearchActivity.this, QuestionnaireActivity.class);
                    intent.putExtra("questionnaires", Objects.requireNonNull(response.body()).string());
                    intent.putExtra("researchId", researchId);
                    startActivity(intent);
                } else {
                    Log.e(TAG, "actual code: " + response.code());
                }
            }
        });
    }

    public void newResearch(View view) {
        Log.e(TAG, "newResearch");
        Request request = HttpRequest.getRequestBuilder(HttpRequest.NEW_RESEARCH_ENDPOINT, this.jwtToken);
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                ResearchActivity.this.runOnUiThread(() -> Toast.makeText(ResearchActivity.this,
                        "Erro pegando pesquisas.", Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.code() == 200) {
                    Intent intent = new Intent(ResearchActivity.this, NewResearchActivity.class);
                    intent.putExtra("newResearches", Objects.requireNonNull(response.body()).string());
                    startActivity(intent);
                } else {
                    Log.e(TAG, "actual code: " + response.code());
                }
            }
        });
    }

    @Override
    public void onPositionClick(int position, View v) {
        //TODO the logic behind retrieve data.
        int id = researches.get(position).getId();
        this.retrieveData(id);
        Log.e(TAG, "Id to be retrieved: " + id);
    }

}

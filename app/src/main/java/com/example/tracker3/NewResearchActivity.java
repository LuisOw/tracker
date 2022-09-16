package com.example.tracker3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tracker3.util.ClickListener;
import com.example.tracker3.util.HttpRequest;
import com.example.tracker3.util.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NewResearchActivity extends BaseActivity implements ClickListener {
    private static final String TAG = "NewResearchActivity";
    ArrayList<Research> researches;
    private User user;
    private OkHttpClient client;
    private String jwtToken;
    private SharedPreferences localSharesPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Gson gson = new Gson();
        Intent intent = getIntent();
        String researchesAsJson = intent.getStringExtra("newResearches");
        Type type = new TypeToken<Collection<Research>>(){}.getType();
        Collection<Research> researchesCollection = gson.fromJson(researchesAsJson, type);
        this.researches = new ArrayList<>(researchesCollection);

        setContentView(R.layout.active_researches);
        ConstraintLayout layout = findViewById(R.id.constraint_layout);
        Button loginButton = findViewById(R.id.search_new_research);
        loginButton.setVisibility(View.INVISIBLE);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(layout);
        constraintSet.connect(R.id.rv_research, ConstraintSet.TOP, R.id.toolbar, ConstraintSet.BOTTOM);
        constraintSet.applyTo(layout);
        RecyclerView rvResearches = findViewById(R.id.rv_research);
        NewResearchAdapter adapter = new NewResearchAdapter(this.researches, this);
        rvResearches.setAdapter(adapter);
        rvResearches.setLayoutManager(new LinearLayoutManager(this));

        localSharesPreferences = getSharedPreferences("account" ,Context.MODE_PRIVATE);
        this.jwtToken = localSharesPreferences.getString(SharedPreferencesUtils.TOKEN_KEY, "");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        client = new OkHttpClient();
    }

    @Override
    public void onPositionClick(int position, View v) {
        Research researchOnPosition = researches.get(position);
        Log.e(TAG, "Id to be retrieved: " + researchOnPosition.getId());
        switch (v.getId()) {
            case R.id.rv_item_description_research_button:
                this.showDescription(researchOnPosition.getDescription());
                break;
            case R.id.rv_item_new_research_button:
                this.addResearch(researchOnPosition.getId());
                break;
        }
    }

    private void addResearch(int id) {
        Log.e(TAG, "addResearch");
        String url = HttpRequest.RESEARCH_ENDPOINT + "/" + id;
        Request request = HttpRequest.patchRequestBuilder(url, this.jwtToken, "");
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                NewResearchActivity.this.runOnUiThread(() -> Toast.makeText(NewResearchActivity.this,
                        "Erro pegando pesquisas.", Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.code() == 204) {
                  renderResearch();
                } else {
                    Log.e(TAG, "actual code: " + response.code());
                }
            }
        });
    }

    private void renderResearch() {
        Request request = HttpRequest.getRequestBuilder(HttpRequest.RESEARCH_ENDPOINT, this.jwtToken);
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                NewResearchActivity.this.runOnUiThread(() -> Toast.makeText(NewResearchActivity.this,
                        "Erro pegando pesquisas.", Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.code() == 200) {
                    Intent intent = new Intent(NewResearchActivity.this, ResearchActivity.class);
                    intent.putExtra("researches", Objects.requireNonNull(response.body()).string());
                    startActivity(intent);
                } else {
                    Log.e(TAG, "actual code: " + response.code());
                }
            }
        });
    }

    private void showDescription(String researchOnPosition) {
    }
}

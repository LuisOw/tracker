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

import com.example.tracker3.adapter.QuestionnaireAdapter;
import com.example.tracker3.domain.Questionnaire;
import com.example.tracker3.domain.User;
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

public class QuestionnaireActivity extends BaseActivity implements ClickListener {

    private static final String TAG = "QuestionnaireActivity";
    ArrayList<Questionnaire> questionnaires;
    private User user;
    private String jwtToken;
    private SharedPreferences localSharesPreferences;
    private OkHttpClient client;
    private int researchId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Gson gson = new Gson();
        Intent intent = getIntent();
        this.researchId = intent.getIntExtra("researchId", 0);
        String questionnairesAsJson = intent.getStringExtra("questionnaires");
        Type type = new TypeToken<Collection<Questionnaire>>(){}.getType();
        Collection<Questionnaire> questionnaireCollection = gson.fromJson(questionnairesAsJson, type);
        this.questionnaires = new ArrayList<>(questionnaireCollection);

        setContentView(R.layout.active_researches);
        ConstraintLayout layout = findViewById(R.id.constraint_layout);
        Button loginButton = findViewById(R.id.search_new_research);
        loginButton.setVisibility(View.INVISIBLE);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(layout);
        constraintSet.connect(R.id.rv_research, ConstraintSet.TOP, R.id.toolbar, ConstraintSet.BOTTOM);
        constraintSet.applyTo(layout);
        RecyclerView rvResearches = findViewById(R.id.rv_research);
        QuestionnaireAdapter adapter = new QuestionnaireAdapter(this.questionnaires, this);
        rvResearches.setAdapter(adapter);
        rvResearches.setLayoutManager(new LinearLayoutManager(this));

        localSharesPreferences = getSharedPreferences("account" , Context.MODE_PRIVATE);
        this.jwtToken = localSharesPreferences.getString(SharedPreferencesUtils.TOKEN_KEY, "");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        client = new OkHttpClient();
    }

    @Override
    public void onPositionClick(int position, View v) {
        int id = this.questionnaires.get(position).getId();
        this.retrieveData(id);
    }

    private void retrieveData(int questionnaireId) {
        Log.e(TAG, "get questionnaires");
        Request request = HttpRequest.getRequestBuilder(
                String.format(HttpRequest.SUBJECT_QUESTION, this.researchId, questionnaireId), this.jwtToken
        );
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                QuestionnaireActivity.this.runOnUiThread(() -> Toast.makeText(QuestionnaireActivity.this,
                        "Erro pegando questionarios.", Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.code() == 200) {
                    Intent intent = new Intent(QuestionnaireActivity.this, QuestionActivity.class);
                    intent.putExtra("questions", Objects.requireNonNull(response.body()).string());
                    startActivity(intent);
                } else {
                    Log.e(TAG, "actual code: " + response.code());
                }
            }
        });
    }
}

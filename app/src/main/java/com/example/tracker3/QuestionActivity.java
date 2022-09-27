package com.example.tracker3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.tracker3.domain.Alternative;
import com.example.tracker3.domain.Question;
import com.example.tracker3.domain.User;
import com.example.tracker3.fragments.DescriptiveAlternativeFragment;
import com.example.tracker3.fragments.MultipleAlternativeFragment;
import com.example.tracker3.util.HttpRequest;
import com.example.tracker3.util.QuestionType;
import com.example.tracker3.util.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class QuestionActivity extends BaseActivity implements MultipleAlternativeFragment.OnItemSelectedListener,
        DescriptiveAlternativeFragment.OnItemSelectedListener{

    private static final String TAG = "QuestionActivity";
    ArrayList<Question> questions;
    private User user;
    private String jwtToken;
    private SharedPreferences localSharesPreferences;
    private OkHttpClient client;
    private ArrayList<Map<String, Object>> selectedAlternatives = new ArrayList<>();
    private int listId = 0;
    private Intent srcIntent;
    private int researchId;
    private int questionnaireId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate question");

        client = new OkHttpClient();
        Gson gson = new Gson();
        srcIntent = getIntent();
        String questionsAsJson = srcIntent.getStringExtra("questions");
        researchId = srcIntent.getIntExtra("researchId", 0);
        questionnaireId = srcIntent.getIntExtra("questionnaireId", 0);
        Type type = new TypeToken<Collection<Question>>(){}.getType();
        Collection<Question> questionnaireCollection = gson.fromJson(questionsAsJson, type);
        this.questions = new ArrayList<>(questionnaireCollection);
        Log.e(TAG, "Questions size = " + questions.size());

        localSharesPreferences = getSharedPreferences("account" , Context.MODE_PRIVATE);
        this.jwtToken = localSharesPreferences.getString(SharedPreferencesUtils.TOKEN_KEY, "");

        setContentView(R.layout.base_fragment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.e(TAG, "adding fragment");
        Bundle bundle = new Bundle();
        bundle.putString("questions", questionsAsJson);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        String questionText;
        ArrayList<Alternative> alternatives = new ArrayList<>();
        Fragment fragment;
        if (this.questions.isEmpty()) {
            questionText = "Questionário não possui questões";
            fragment = MultipleAlternativeFragment
                    .newInstance(questionText, alternatives);
        } else {
            Question question = this.questions.get(listId);
            questionText = question.getQuery();
            alternatives = question.getAlternatives();
            Log.e(TAG, "Question: "+ question);
            if (question.getType().equals("descrivita")) {
                fragment = DescriptiveAlternativeFragment.newInstance(questionText,
                        alternatives.get(0).getId());
            } else {
                fragment = MultipleAlternativeFragment
                        .newInstance(questionText, alternatives);
            }
        }
        fragmentTransaction.replace(R.id.placeholder, fragment);
        fragmentTransaction.commit();
    }



    @Override
    public void addAlternativesToList(Alternative alternative) {
        Map<String, Object> alternativesMap = new HashMap<>();
        alternativesMap.put("text", alternative.getText());
        alternativesMap.put("alternative_chosen", String.valueOf(alternative.getValue()));
        alternativesMap.put("alternative_id", alternative.getId());
        selectedAlternatives.add(alternativesMap);
        processNextQuestion();
    }

    @Override
    public void addDescriptiveToList(String answer, int alternativeId) {
        Map<String, Object> alternativesMap = new HashMap<>();
        alternativesMap.put("text", "");
        alternativesMap.put("alternative_chosen", answer);
        alternativesMap.put("alternative_id", alternativeId);
        selectedAlternatives.add(alternativesMap);
        processNextQuestion();
    }

    private void processNextQuestion() {
        listId++;
        if (questions.size() > listId) {
            Question question = this.questions.get(listId);
            Log.e(TAG, "Question: "+ question);
            Fragment fragment;
            if (question.getType().equals("descritiva")) {
                fragment = DescriptiveAlternativeFragment
                        .newInstance(question.getQuery(), question.getAlternatives().get(0).getId());
            } else {
                fragment = MultipleAlternativeFragment
                        .newInstance(question.getQuery(), question.getAlternatives());
            }
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.placeholder, fragment);
            fragmentTransaction.commit();
        } else {
            Log.e(TAG, "post alternatives answers");
            Gson gson = new GsonBuilder().create();
            Map<String, Object> answer = new HashMap<>();
            answer.put("alternatives", selectedAlternatives);
            String json = gson.toJson(answer);
            Request request = HttpRequest.postRequestBuilder(
                    String.format(HttpRequest.SUBJECT_QUESTION, researchId, questionnaireId), this.jwtToken, json
            );
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    QuestionActivity.this.runOnUiThread(() -> Toast.makeText(QuestionActivity.this,
                            "Erro postando respostas.", Toast.LENGTH_LONG).show());
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response){
                    if (response.code() == 204) {
                        Intent intent = new Intent(QuestionActivity.this, QuestionnaireActivity.class);
                        intent.putExtras(srcIntent);
                        startActivity(intent);
                    } else {
                        Log.e(TAG, "actual code: " + response.code());
                    }
                }
            });
        }
    }
}

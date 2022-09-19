package com.example.tracker3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.example.tracker3.domain.Alternative;
import com.example.tracker3.domain.Question;
import com.example.tracker3.domain.User;
import com.example.tracker3.fragments.QuestionFragment;
import com.example.tracker3.util.ClickListener;
import com.example.tracker3.util.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import okhttp3.OkHttpClient;

public class QuestionActivity extends BaseActivity implements ClickListener {

    private static final String TAG = "QuestionActivity";
    ArrayList<Question> questions;
    private User user;
    private String jwtToken;
    private SharedPreferences localSharesPreferences;
    private OkHttpClient client;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate question");

        client = new OkHttpClient();
        Gson gson = new Gson();
        Intent intent = getIntent();
        String questionsAsJson = intent.getStringExtra("questions");
        Type type = new TypeToken<Collection<Question>>(){}.getType();
        Collection<Question> questionnaireCollection = gson.fromJson(questionsAsJson, type);
        this.questions = new ArrayList<>(questionnaireCollection);

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
        if (this.questions.isEmpty()) {
            questionText = "Questionário não possui questões";
        } else {
            Question question = this.questions.get(0);
            questionText = question.getQuery();
            alternatives = question.getAlternatives();
            Log.e(TAG, "Question: "+ question);
        }
        QuestionFragment questionFragment = QuestionFragment
                .newInstance(questionText, alternatives);
        fragmentTransaction.replace(R.id.placeholder, questionFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onPositionClick(int position, View v) {

    }
}

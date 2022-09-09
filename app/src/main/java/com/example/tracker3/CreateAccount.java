package com.example.tracker3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tracker3.util.HttpRequest;
import com.example.tracker3.util.SharedPreferencesUtils;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CreateAccount extends AppCompatActivity {

    private static final String TAG = "CreateAccount";

    private User user;
    private EditText username;
    private EditText password;
    private EditText chosenName;
    private SharedPreferences localSharesPreferences;
    private Gson gson;
    private OkHttpClient client;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localSharesPreferences = getSharedPreferences("account", Context.MODE_PRIVATE);
        setContentView(R.layout.account_creation);
        username = findViewById(R.id.et_username);
        password = findViewById(R.id.et_password);
        chosenName = findViewById(R.id.et_participant_name);
        client = new OkHttpClient();
        gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }

    public void create(View view) {
        String localUsername = username.getText().toString();
        String localPassword = password.getText().toString();
        this.createAccount(localUsername, localPassword);
    }

    public void cancel(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void createAccount(String username, String password){
        Request request = HttpRequest.localAuthBuilder(username, password);
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                CreateAccount.this.runOnUiThread(() -> Toast.makeText(CreateAccount.this,
                                "Erro validando usuário.", Toast.LENGTH_LONG)
                        .show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.code() == 200) {
                    Log.e(TAG, "200 code");
                    user = gson.fromJson(Objects.requireNonNull(response.body()).string(),
                            User.class);
                    Log.e(TAG, user.toString());
                    SharedPreferences.Editor editor = localSharesPreferences.edit();
                    editor.putString(SharedPreferencesUtils.TOKEN_KEY, user.getAccessToken());
                    editor.apply();
                    renderResearch(user.getAccessToken());
                } else {
                    Log.e(TAG, "not 200 code");
                }
            }
        });
    }

    private void renderResearch(String token) {
        Request request = HttpRequest.getRequestBuilder(HttpRequest.RESEARCH_ENDPOINT, token);
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                CreateAccount.this.runOnUiThread(() -> Toast.makeText(CreateAccount.this,
                        "Erro pegando pesquisas.", Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.code() == 200) {
                    Intent intent = new Intent(CreateAccount.this, ResearchActivity.class);
                    intent.putExtra("json", Objects.requireNonNull(response.body()).string());
                    startActivity(intent);
                } else {
                    Log.e(TAG, "actual code: " + response.code());
                }
            }
        });
    }
}

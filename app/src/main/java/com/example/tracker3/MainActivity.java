package com.example.tracker3;


import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
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


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private TextView textView;
    private EditText userName;
    private EditText password;
    private String jwtToken;
    private SharedPreferences localSharesPreferences;
    private OkHttpClient client;
    private Gson gson;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = new OkHttpClient();
        gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        localSharesPreferences = getSharedPreferences("account" ,Context.MODE_PRIVATE);
        this.jwtToken = localSharesPreferences.getString(SharedPreferencesUtils.TOKEN_KEY, "");
        if (jwtToken.isEmpty()) {
            setContentView(R.layout.login);
            userName = findViewById(R.id.et_username);
            password = findViewById(R.id.et_password);
            textView = findViewById(R.id.message);
            Button loginButton = findViewById(R.id.btn_login);
            loginButton.setOnClickListener(this);
            Button forgotButton = findViewById(R.id.btn_forgot);
            forgotButton.setOnClickListener(this);
            Button newAccountButton = findViewById(R.id.btn_new_account);
            newAccountButton.setOnClickListener(this);
        } else {
            //TODO this path should be conditional on token validity period.
            Log.e(TAG, "Shared found. Skipping login using token = " + jwtToken);
            this.renderResearch(jwtToken);
        }

    /*    if (!checkUsageStatsPermission()) {
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        }*/
    }

    public void access() {
        String localUsername = userName.getText().toString();
        String localPassword = password.getText().toString();
        this.verifyAccountInformation(localUsername, localPassword);
    }

    private void renderResearch(String token) {
        Request request = HttpRequest.getRequestBuilder(HttpRequest.RESEARCH_ENDPOINT, token);
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                MainActivity.this.runOnUiThread(() -> Toast.makeText(MainActivity.this,
                                "Erro pegando pesquisas.", Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.code() == 200) {
                    Intent intent = new Intent(MainActivity.this, ResearchActivity.class);
                    intent.putExtra("researches", Objects.requireNonNull(response.body()).string());
                    startActivity(intent);
                } else {
                    Log.e(TAG, "actual code: " + response.code());
                }
            }
        });
    }

    public void verifyAccountInformation(String userName, String password) {
        Request request = HttpRequest.localAuthBuilder("099", "123");
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                MainActivity.this.runOnUiThread(() -> Toast.makeText(MainActivity.this,
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

    public void passwordForgot() {
    }

    public void accountCreate() {
        Intent intent = new Intent(this, CreateAccount.class);
        startActivity(intent);
    }

    //TODO move based on active researches
    private boolean checkUsageStatsPermission() {
        AppOpsManager appOps = (AppOpsManager)
                getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                this.access();
                break;
            case R.id.btn_forgot:
                this.passwordForgot();
                break;
            case R.id.btn_new_account:
                this.accountCreate();
                break;
        }
    }
}
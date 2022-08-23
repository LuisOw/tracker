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
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private TextView textView;
    private EditText userName;
    private EditText password;
    private String jwtToken;
    private SharedPreferences localSharesPreferences;
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localSharesPreferences = getPreferences(Context.MODE_PRIVATE);
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
            client = new OkHttpClient();
        } else {
            this.renderResearch();
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

    private void renderResearch() {
        Log.e(TAG, "render");
        Request request = HttpRequest.getRequestBuilder(HttpRequest.RESEARCH_ENDPOINT, jwtToken);
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                MainActivity.this.runOnUiThread(() -> Toast.makeText(MainActivity.this,
                                "Erro pegando pesquisas.", Toast.LENGTH_LONG)
                        .show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.code() == 200) {
                    Log.e(TAG, "code 200");
                    Map<String, Object> responseMap = new ObjectMapper().readValue
                            (Objects.requireNonNull(response.body()).byteStream(), HashMap.class);
                    Log.e(TAG, "start for");
                    for (Map.Entry<String, Object> entry : responseMap.entrySet()) {
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        Log.e(TAG, "key:" + key + " value:" + value);
                    }
                    Log.e(TAG, "end for");
                } else {
                    Log.e(TAG, "actual code: " + response.code());
                }
            }
        });
 /*       Intent intent = new Intent(MainActivity.this, ResearchActivity.class);
        intent.putExtra("json", result);
        startActivity(intent);*/
    }

    public void verifyAccountInformation(String userName, String password) {
        Request request = HttpRequest.localAuthBuilder("test@gmail.com", "123");
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
                    //TODO add user parse logic. Keeping for debug purpose
                    Map<String, Object> responseMap = new ObjectMapper().readValue
                            (Objects.requireNonNull(response.body()).byteStream(), HashMap.class);
                    String accessToken = (String) responseMap.get("access_token");
                    Log.e(TAG, accessToken);
                    jwtToken = accessToken;
                    renderResearch();
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
package com.example.tracker3;


import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tracker3.util.HttpRequest;
import com.example.tracker3.util.VolleyCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private TextView textView;
    private EditText userName;
    private EditText password;
    private String jwtToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        userName = findViewById(R.id.et_username);
        password = findViewById(R.id.et_password);
        textView = findViewById(R.id.message);

    /*    if (!checkUsageStatsPermission()) {
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        }*/
    }

    public void access(View view) {
        String localUsername = userName.getText().toString();
        String localPassword = password.getText().toString();
     /*   this.verifyAccountInformation(localUsername, localPassword, result -> {
            Intent intent = new Intent(MainActivity.this, ResearchActivity.class);
            intent.putExtra("json", result);
            startActivity(intent);
        });*/
        /* TODO remove after validation it's complete */
        Intent intent = new Intent(MainActivity.this, ResearchActivity.class);
        startActivity(intent);
    }

    public void verifyAccountInformation(String userName, String password, final VolleyCallback callback) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpRequest.BASE_URL + "/login",
                callback::onSuccess,
                error -> textView.setText("Unable to login")) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("login", userName);
                params.put("password", password);
                return params;
            }
        };
        queue.add(stringRequest);
    }

    public void passForgot(View view) {
    }

    public void accountCreate(View view) {
        Intent intent = new Intent(this, CreateAccount.class);
        startActivity(intent);
    }

    private boolean checkUsageStatsPermission() {
        AppOpsManager appOps = (AppOpsManager)
                getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }
}
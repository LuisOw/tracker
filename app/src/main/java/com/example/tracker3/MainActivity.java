package com.example.tracker3;


import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tracker3.util.VolleyCallback;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String BASE_URL ="http://a0e6-2804-14d-baa2-9559-8e8c-2ca-ab51-eddd.ngrok.io";

    private String url;
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

        if (!checkUsageStatsPermission()) {
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        }
    }

    public void access(View view) {
        verifyAccountInformation(result -> {
            jwtToken = result;
            Intent intent = new Intent(MainActivity.this, ResearchActivity.class);
            startActivity(intent);
        });
        /* TODO remove after validation it's complete */
        Intent intent = new Intent(MainActivity.this, ResearchActivity.class);
        startActivity(intent);
    }

    public void verifyAccountInformation(final VolleyCallback callback) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL + "/login",
                callback::onSucess,
                error -> textView.setText("Unable to login")) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("login", userName.getText().toString());
                params.put("password", password.getText().toString());
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
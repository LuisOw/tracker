package com.example.tracker3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tracker3.util.VolleyCallback;

import java.util.HashMap;
import java.util.Map;

public class CreateAccount extends AppCompatActivity {
    private static final String BASE_URL = "http://a0e6-2804-14d-baa2-9559-8e8c-2ca-ab51-eddd.ngrok.io";

    private TextView textView;
    private String jwtToken;
    private EditText userName;
    private EditText password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_creation);
        userName = findViewById(R.id.et_username);
        password = findViewById(R.id.et_password);
        textView = findViewById(R.id.message);
    }

    public void create(View view) {
        createAccount(result -> {
            jwtToken = result;
            Toast.makeText(this, "Conta criada" + jwtToken, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(CreateAccount.this, MainActivity.class);
            startActivity(intent);
        });
    }

    public void cancel(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void createAccount(VolleyCallback callback){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL + "/create",
                callback::onSucess,
                error -> textView.setText("Unable to create account")) {
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
}

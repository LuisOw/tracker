package com.example.tracker3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tracker3.util.HttpRequest;
import com.example.tracker3.util.SharedPreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CreateAccount extends AppCompatActivity {

    private static final String TAG = "CreateAccount";

    private String jwtToken;
    private EditText userName;
    private EditText password;
    private SharedPreferences localSharesPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localSharesPreferences = getPreferences(Context.MODE_PRIVATE);
        setContentView(R.layout.account_creation);
        userName = findViewById(R.id.et_username);
        password = findViewById(R.id.et_password);
    }

    public void create(View view) {
        this.createAccount();
        this.renderResearch();
    }

    public void cancel(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void createAccount(){
        Map<String, String> params = new HashMap<>();
        params.put("login", userName.getText().toString());
        params.put("password", password.getText().toString());

 /*       jwtToken = result.getString("token");
        Toast.makeText(CreateAccount.this, "Erro parseando json",
                Toast.LENGTH_LONG).show();
        localSharesPreferences.edit()
                .putString(SharedPreferencesUtils.TOKEN_KEY, jwtToken).commit();
        renderResearch();*/

    }

    private void renderResearch() {
        /*Intent intent = new Intent(CreateAccount.this, ResearchActivity.class);
        intent.putExtra("json", result);
        startActivity(intent);*/
    }
}

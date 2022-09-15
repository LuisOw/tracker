package com.example.tracker3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tracker3.util.HttpRequest;
import com.example.tracker3.util.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FiltersActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "FiltersActivity";

    private EditText income;
    private OkHttpClient client;
    private String jwtToken;
    private Map<String, Object> responses = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filters);

        Spinner spinnerRace = findViewById(R.id.spinner_race);
        spinnerRace.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapterRace =
                ArrayAdapter.createFromResource(this, R.array.race_options, android.R.layout.simple_spinner_item);
        adapterRace.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRace.setAdapter(adapterRace);

        Spinner spinnerGender = findViewById(R.id.spinner_identity);
        spinnerGender.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapterGender =
                ArrayAdapter.createFromResource(this, R.array.idendity_options, android.R.layout.simple_spinner_item);
        adapterRace.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapterGender);

        Spinner spinnerSexuality = findViewById(R.id.spinner_sexuality);
        spinnerSexuality.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapterSexuality =
                ArrayAdapter.createFromResource(this, R.array.sexuality_options, android.R.layout.simple_spinner_item);
        adapterRace.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSexuality.setAdapter(adapterSexuality);

        income = findViewById(R.id.et_income);

        SharedPreferences localSharesPreferences = getSharedPreferences("account", Context.MODE_PRIVATE);
        this.jwtToken = localSharesPreferences.getString(SharedPreferencesUtils.TOKEN_KEY, "");
        client = new OkHttpClient();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position > 0) {
            switch (parent.getId()) {
                case (R.id.spinner_race):
                    Log.e(TAG, "Race value = " + parent.getItemAtPosition(position));
                    this.responses.put("race", parent.getItemAtPosition(position));
                    break;
                case (R.id.spinner_identity):
                    Log.e(TAG, "Gender value = " + parent.getItemAtPosition(position));
                    this.responses.put("gender", parent.getItemAtPosition(position));
                    break;
                case (R.id.spinner_sexuality):
                    Log.e(TAG, "Sexuality value = " + parent.getItemAtPosition(position));
                    this.responses.put("sexualOrientation", parent.getItemAtPosition(position));
                    break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    public void updateFilters(View view) {
        Log.e(TAG, "updateFilters");
        String incomeString = income.getText().toString();
        if (!incomeString.isEmpty()) {
            this.responses.put("income", incomeString);
        }
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(this.responses);
        Request request = HttpRequest.patchRequestBuilder(HttpRequest.SUBJECT, this.jwtToken, json);
        Call call = this.client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                FiltersActivity.this.runOnUiThread(() -> Toast.makeText(FiltersActivity.this,
                        "Erro atualizando filtros.", Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.code() == 200) {
                    Intent intent = new Intent(FiltersActivity.this, ResearchActivity.class);
                    startActivity(intent);
                } else {
                    Log.e(TAG, "actual code: " + response.code());
                }
            }
        });
    }
}

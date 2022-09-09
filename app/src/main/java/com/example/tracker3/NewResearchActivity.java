package com.example.tracker3;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tracker3.util.ClickListener;
import com.example.tracker3.util.HttpRequest;
import com.example.tracker3.util.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NewResearchActivity extends AppCompatActivity implements ClickListener {
    private static final String TAG = "NewResearchActivity";
    ArrayList<Research> researches;
    private User user;
    private OkHttpClient client;
    private String jwtToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Gson gson = new Gson();
        Intent intent = getIntent();
        String researchesAsJson = intent.getStringExtra("newResearches");
        Type type = new TypeToken<Collection<Research>>(){}.getType();
        Collection<Research> researchesCollection = gson.fromJson(researchesAsJson, type);
        this.researches = new ArrayList<>(researchesCollection);

        setContentView(R.layout.active_researches);
        ConstraintLayout layout = findViewById(R.id.constraint_layout);
        Button loginButton = findViewById(R.id.search_new_research);
        loginButton.setVisibility(View.INVISIBLE);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(layout);
        constraintSet.connect(R.id.rv_research, ConstraintSet.TOP, R.id.toolbar, ConstraintSet.BOTTOM);
        constraintSet.applyTo(layout);
        RecyclerView rvResearches = findViewById(R.id.rv_research);
        NewResearchAdapter adapter = new NewResearchAdapter(this.researches, this);
        rvResearches.setAdapter(adapter);
        rvResearches.setLayoutManager(new LinearLayoutManager(this));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onPositionClick(int position, View v) {
        //TODO the logic behind retrieve data.
        Research researchOnPosition = researches.get(position);
        Log.e(TAG, "Id to be retrieved: " + researchOnPosition.getId());
        switch (v.getId()) {
            case R.id.rv_item_description_research_button:
                this.showDescription(researchOnPosition.getDescription());
                break;
            case R.id.rv_item_new_research_button:
                this.addResearch(researchOnPosition.getId());
                break;
        }
    }

    private void addResearch(int id) {
        String url = HttpRequest.RESEARCH_ENDPOINT + "/" + id;
        Request request = HttpRequest.getRequestBuilder(url, this.jwtToken);
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                NewResearchActivity.this.runOnUiThread(() -> Toast.makeText(NewResearchActivity.this,
                        "Erro pegando pesquisas.", Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.code() == 204) {
                    Intent intent = new Intent(NewResearchActivity.this, ResearchActivity.class);
                    startActivity(intent);
                } else {
                    Log.e(TAG, "actual code: " + response.code());
                }
            }
        });
    }

    private void showDescription(String researchOnPosition) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.config:
                Toast.makeText(this, "Config selecionado", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.logout:
                Toast.makeText(this, "Logout selecionado", Toast.LENGTH_SHORT).show();
                SharedPreferences localSharesPreferences = getSharedPreferences("account", Context.MODE_PRIVATE);
                String token = localSharesPreferences.getString(SharedPreferencesUtils.TOKEN_KEY, "");
                Log.e(TAG, "token = " + token);
                SharedPreferences.Editor editor = localSharesPreferences.edit();
                editor.clear().commit();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.info:
                Toast.makeText(this, "Info selecionado", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

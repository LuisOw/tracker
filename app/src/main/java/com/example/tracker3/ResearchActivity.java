package com.example.tracker3;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tracker3.util.ClickListener;
import com.example.tracker3.util.HttpRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

public class ResearchActivity extends AppCompatActivity implements ClickListener {

    private static final String TAG = "ResearchActivity";
    ArrayList<Research> researches;
    private Toolbar toolbar;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ObjectMapper mapper = new ObjectMapper();
        Intent intent = getIntent();
        /*try {
            user = mapper.readValue(intent.getStringExtra("json"), User.class);
        } catch (JsonProcessingException e) {
            Toast.makeText(getApplicationContext(), "Unable to retrieve research with id",
                    Toast.LENGTH_SHORT).show();
        }*/

        setContentView(R.layout.active_researches);
        RecyclerView rvResearches = findViewById(R.id.rv_research);
        researches = Research.createResearchesList(20);
        ResearchAdapter adapter = new ResearchAdapter(researches, this);
        rvResearches.setAdapter(adapter);
        rvResearches.setLayoutManager(new LinearLayoutManager(this));

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    void getData(UUID researchId) {
        try {
            JSONObject retrievedData = new JSONObject(generateJson());
            /*this.retrieveData(user.jwtToken, researchId, callback);*/
            Intent intent = new Intent(ResearchActivity.this, PresentedResearch.class);
            intent.putExtra("json", retrievedData.toString());
            Toast.makeText(getApplicationContext(), "Http request with id = " + researchId,
                    Toast.LENGTH_SHORT).show();
            startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Deu ruim fi" +
                    researchId, Toast.LENGTH_SHORT).show();
        }
    }

    private void retrieveData(String jwtToken, UUID userId, UUID researchId) {
    }


    private String generateJson() {
        return "{" +
                "\"usageTimeCapture\": \"true\"," +
                "\"researches\": [\n" +
                "      {\n" +
                "        \"type\": \"multiple_choice\",\n" +
                "        \"question\": \"primeira pergunta\",\n" +
                "        \"answers\": [\n" +
                "          \"string1\",\n" +
                "          \"string2\",\n" +
                "          \"string3\"\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"type\": \"other\",\n" +
                "        \"question\": \"pergunta 14\"\n" +
                "      }" +
                "    ]\n" +
                "  }" +
                "}";
    }

    @Override
    public void onPositionClick(int position) {
        //TODO the logic behind retrieve data.
        getData(researches.get(position).getId());
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

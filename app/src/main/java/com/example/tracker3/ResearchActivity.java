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

import java.util.ArrayList;
import java.util.UUID;

public class ResearchActivity extends AppCompatActivity implements ClickListener {

    ArrayList<Research> researches;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_researches);

        RecyclerView rvResearches = findViewById(R.id.rv_research);
        researches = Research.createResearchesList(20);
        ResearchAdapter adapter = new ResearchAdapter(researches, this);
        rvResearches.setAdapter(adapter);
        rvResearches.setLayoutManager(new LinearLayoutManager(this));

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    //TODO
    void getData(UUID researchId, int position) {
        Toast.makeText(getApplicationContext(), "Research ID = " + researchId + "position = "
                + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPositionClick(int position) {
        //TODO the logic behind retrieve data.
        getData(researches.get(position).getId(), position);
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

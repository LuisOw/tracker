package com.example.tracker3;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ResearchActivity extends AppCompatActivity {

    ArrayList<Research> researches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_researches);

        RecyclerView rvResearches = findViewById(R.id.rv_research);

        researches = Research.createResearchesList(20);

        ResearchAdapter adapter = new ResearchAdapter(researches);

        rvResearches.setAdapter(adapter);
        rvResearches.setLayoutManager(new LinearLayoutManager(this));

    }
}

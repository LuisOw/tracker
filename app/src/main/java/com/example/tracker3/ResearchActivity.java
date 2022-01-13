package com.example.tracker3;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.UUID;

public class ResearchActivity extends AppCompatActivity implements ClickListener{

    ArrayList<Research> researches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_researches);

        RecyclerView rvResearches = findViewById(R.id.rv_research);

        researches = Research.createResearchesList(20);

        ResearchAdapter adapter = new ResearchAdapter(researches, this);

        rvResearches.setAdapter(adapter);
        rvResearches.setLayoutManager(new LinearLayoutManager(this));
    }

    //TODO
    void getData(UUID researchId) {
        Toast.makeText(getApplicationContext(), "Research ID = " + researchId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPositionClick(int position) {
        //TODO the logic behind retrieve data.
        getData(researches.get(position).getId());
    }
}

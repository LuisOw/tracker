package com.example.tracker3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tracker3.util.SharedPreferencesUtils;

public abstract class BaseActivity extends AppCompatActivity {

    private final String TAG = "BaseActivity";

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.config:
                Toast.makeText(this, "Filtro selecionado", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "starting new activity");
                Intent filterIntent = new Intent(this, FiltersActivity.class);
                startActivity(filterIntent);
                return true;
            case R.id.logout:
                Toast.makeText(this, "Logout selecionado", Toast.LENGTH_SHORT).show();
                SharedPreferences localSharesPreferences = getSharedPreferences("account", Context.MODE_PRIVATE);
                String token = localSharesPreferences.getString(SharedPreferencesUtils.TOKEN_KEY, "");
                Log.e(TAG, "token = " + token);
                SharedPreferences.Editor editor = localSharesPreferences.edit();
                editor.clear().commit();
                Intent mainIntent = new Intent(this, MainActivity.class);
                startActivity(mainIntent);
                return true;
            case R.id.info:
                Toast.makeText(this, "Info selecionado", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

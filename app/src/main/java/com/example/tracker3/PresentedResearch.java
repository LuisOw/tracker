package com.example.tracker3;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class PresentedResearch extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_fragment);

        Intent intent = getIntent();

        try {
            presentResearch(new JSONObject(intent.getStringExtra("json")));
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Unable to retrieve research with id",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void presentResearch(JSONObject retrievedData) throws JSONException {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        JSONArray array = retrievedData.getJSONArray("researches");
        JSONObject obj;
        for (int i = 0; i < array.length(); i++) {
            obj = array.getJSONObject(i);
            if (obj.getString("type").equals("multiple_choice")) {
                createMultipleChoiceView(obj, params, linearLayout);
            } else {
                createDiscursiveView(obj, params, linearLayout);
            }
        }
        if (retrievedData.getBoolean("usageTimeCapture")) {
            activateUsageTimeCapture(params, linearLayout);
        }
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        this.addContentView(linearLayout, layoutParams);
    }

    private void activateUsageTimeCapture(LinearLayout.LayoutParams params, LinearLayout linearLayout) {
        TextView textView = new TextView(this);
        textView.setText("Usage time: ");
        textView.setLayoutParams(params);
        linearLayout.addView(textView);

        TextView mUsageTime = new TextView(this);
        mUsageTime.setLayoutParams(params);

        UsageStatsManager mUsageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        long startMillis, endMillis;
        startMillis = cal.getTimeInMillis();
        endMillis = System.currentTimeMillis();
        Map<String, UsageStats> lUsageStatsMap = mUsageStatsManager.
                queryAndAggregateUsageStats(startMillis, endMillis);

        StringBuilder aws = new StringBuilder();
        Set<String> lUsageSet = lUsageStatsMap.keySet();
        for (String name : lUsageSet) {
            if (Objects.requireNonNull(lUsageStatsMap.get(name)).getTotalTimeInForeground() > 0) {
                aws.append(name).append(": ");
                aws.append(timeConvert(lUsageStatsMap.get(name).getTotalTimeInForeground())).append("\n");
            }
        }
        mUsageTime.setText(aws.toString());
        linearLayout.addView(mUsageTime);
    }

    private void createDiscursiveView(JSONObject obj, LinearLayout.LayoutParams params,
                                      LinearLayout linearLayout) throws JSONException {
        TextView textView = new TextView(this);
        textView.setText(obj.getString("question"));
        textView.setLayoutParams(params);

        EditText editText = new EditText(this);
        editText.setLayoutParams(params);
        linearLayout.addView(textView);
        linearLayout.addView(editText);
    }

    private void createMultipleChoiceView(JSONObject obj, LinearLayout.LayoutParams params,
                                          LinearLayout linearLayout) throws
            JSONException {
        TextView textView = new TextView(this);
        textView.setText(obj.getString("question"));
        textView.setLayoutParams(params);

        RadioGroup radioGroup = new RadioGroup(this);
        radioGroup.setLayoutParams(params);

        linearLayout.addView(textView);
        linearLayout.addView(radioGroup);

        JSONArray array = obj.getJSONArray("answers");
        for (int i = 0; i < array.length(); i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(array.getString(i));
            radioButton.setLayoutParams(params);
            linearLayout.addView(radioButton);
        }
    }

    private String timeConvert(long totalTime) {
        //Date date = new Date(totalTime);
        //SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss", Locale.ENGLISH);
        //String returnString = DateFormat.getDateInstance().format(totalTime);

        long seconds = (totalTime/1000)%60;
        long minutes = (totalTime/(1000*60))%60;
        long hours = (totalTime/(1000*60*60))%24;

        return hours + ":" + minutes + ":" + seconds;
    }
}

package com.example.tracker3.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tracker3.QuestionActivity;
import com.example.tracker3.R;
import com.example.tracker3.adapter.ResearchAdapter;
import com.example.tracker3.domain.Alternative;
import com.example.tracker3.domain.Research;
import com.example.tracker3.util.ClickListener;

import java.util.ArrayList;
import java.util.List;

public class QuestionFragment extends Fragment {

    private static final String TAG = "QuestionFragment";

    ArrayList<Alternative> alternatives;
    String questionText;

    public static QuestionFragment newInstance(String questionText, ArrayList<Alternative> alternatives) {
        QuestionFragment fragment = new QuestionFragment();
        Log.e(TAG, "question alternatives newInstance" + alternatives);
        Bundle args = new Bundle();
        args.putString("questionText", questionText);
        args.putParcelableArrayList("alternatives", alternatives);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.questionText = getArguments().getString("questionText");
        this.alternatives = getArguments().getParcelableArrayList("alternatives");
        Log.e(TAG, "Alternatives " + this.alternatives);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.question_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        TextView textView = view.findViewById(R.id.tv_question_title);
        textView.setText(this.questionText);
        Log.e(TAG, "Alternatives onViewCreated " + this.alternatives.size());
        RadioGroup radioGroup = view.findViewById(R.id.radio_group);
        radioGroup.setOrientation(LinearLayout.VERTICAL);
        for (int i = 0; i < this.alternatives.size(); i++) {
            RadioButton radioButton = new RadioButton(getContext());
            radioButton.setId(View.generateViewId());
            radioButton.setText(this.alternatives.get(i).getText());
            radioGroup.addView(radioButton);
        }
    }

}

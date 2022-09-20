package com.example.tracker3.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.tracker3.R;
import com.example.tracker3.domain.Alternative;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QuestionFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "QuestionFragment";

    ArrayList<Alternative> alternatives;
    String questionText;
    Alternative selectedAlternative;
    private OnItemSelectedListener listener;
    private Button nextButton;
    Map<Integer, Integer> idMap = new HashMap<>();

    public interface OnItemSelectedListener {
        void addAlternativesToList(Alternative alternative);
    }

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
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnItemSelectedListener) {
            listener = (OnItemSelectedListener) context;
        } else {
            throw new ClassCastException(context
                    + " must implement MyListFragment.OnItemSelectedListener");
        }
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
        this.nextButton = view.findViewById(R.id.btn_question_next);
        this.nextButton.setOnClickListener(this);
        TextView textView = view.findViewById(R.id.tv_question_title);
        textView.setText(this.questionText);
        Log.e(TAG, "Alternatives onViewCreated " + this.alternatives.size());
        RadioGroup radioGroup = view.findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(onCheckedChangeListener);
        radioGroup.setOrientation(LinearLayout.VERTICAL);
        for (int i = 0; i < this.alternatives.size(); i++) {
            RadioButton radioButton = new RadioButton(getContext());
            int generatedId = View.generateViewId();
            radioButton.setId(generatedId);
            idMap.put(generatedId, i);
            radioButton.setText(this.alternatives.get(i).getText());
            radioGroup.addView(radioButton);
        }
    }

   private final RadioGroup.OnCheckedChangeListener onCheckedChangeListener = (radioGroup, selectedId) -> {
        Log.e(TAG, "Selected alternative = " + selectedId);
        Log.e(TAG, "Id mapping = " + idMap.get(selectedId));
        this.selectedAlternative = this.alternatives.get(idMap.get(selectedId));
   };

    @Override
    public void onClick(View view) {
        listener.addAlternativesToList(selectedAlternative);
    }
}


package com.example.tracker3.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.tracker3.R;
import com.example.tracker3.domain.Alternative;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DescriptiveAlternativeFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "QuestionFragment";

    String questionText;
    private DescriptiveAlternativeFragment.OnItemSelectedListener listener;
    EditText editText;
    int alternativeId;

    public interface OnItemSelectedListener {
        void addDescriptiveToList(String answer, int alternativeId);
    }

    public static DescriptiveAlternativeFragment newInstance(String questionText, int alternativeId ) {
        Log.e(TAG, "newInstance");
        DescriptiveAlternativeFragment fragment = new DescriptiveAlternativeFragment();
        Bundle args = new Bundle();
        args.putString("questionText", questionText);
        args.putInt("alternativeId", alternativeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e(TAG, "onAttach");
        if (context instanceof DescriptiveAlternativeFragment.OnItemSelectedListener) {
            listener = (DescriptiveAlternativeFragment.OnItemSelectedListener) context;
        } else {
            throw new ClassCastException(context
                    + " must implement MyListFragment.OnItemSelectedListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate");
        questionText = getArguments().getString("questionText");
        alternativeId = getArguments().getInt("alternativeId");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView");
        return inflater.inflate(R.layout.descriptive_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.e(TAG, "onViewCreated");
        Button nextButton = view.findViewById(R.id.btn_question_next2);
        nextButton.setOnClickListener(this);
        TextView textView = view.findViewById(R.id.tv_question_title);
        textView.setText(this.questionText);
        editText = view.findViewById(R.id.et_descriptive);

    }

    @Override
    public void onClick(View view) {
        listener.addDescriptiveToList(editText.getText().toString(), alternativeId);
    }
}

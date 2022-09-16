package com.example.tracker3;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tracker3.util.ClickListener;

import java.util.List;

public class QuestionnaireAdapter extends RecyclerView.Adapter<QuestionnaireAdapter.ViewHolder> {

    private final List<Questionnaire> mQuestionnaires;
    private ClickListener mListener;
    // Pass in the contact array into the constructor
    public QuestionnaireAdapter(List<Questionnaire> questionnaires, ClickListener listener) {
        mQuestionnaires = questionnaires;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public QuestionnaireAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
            .inflate(R.layout.rv_research_item, parent, false), mListener);
    }

    @Override
    public void onBindViewHolder(QuestionnaireAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        Questionnaire questionnaire = mQuestionnaires.get(position);

        // Set item views based on your views and data model
        TextView textView = holder.nameTextView;
        textView.setText(questionnaire.getTitle());
        Button button = holder.questionnaireButton;
        button.setText("Responder");
        button.setEnabled(true);
    }

    @Override
    public int getItemCount() {
            return mQuestionnaires.size();
            }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView nameTextView;
        public Button questionnaireButton;
        ClickListener listener;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView, ClickListener listener) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            nameTextView = itemView.findViewById(R.id.rv_item_research_name);
            questionnaireButton = itemView.findViewById(R.id.rv_item_research_button);
            this.listener = listener;

            itemView.setOnClickListener(this);
            questionnaireButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onPositionClick(getAdapterPosition(), v);
        }
    }
}

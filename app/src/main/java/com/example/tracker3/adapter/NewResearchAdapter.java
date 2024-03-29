package com.example.tracker3.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tracker3.R;
import com.example.tracker3.domain.Research;
import com.example.tracker3.util.ClickListener;

import java.util.List;

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class NewResearchAdapter extends RecyclerView.Adapter<NewResearchAdapter.ViewHolder> {

    private final List<Research> mResearches;
    private ClickListener mListener;
    // Pass in the contact array into the constructor
    public NewResearchAdapter(List<Research> researches, ClickListener listener) {
        mResearches = researches;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public NewResearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_new_research_item, parent, false), mListener);
    }

    @Override
    public void onBindViewHolder(NewResearchAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        Research research = mResearches.get(position);

        // Set item views based on your views and data model
        TextView textView = holder.nameTextView;
        textView.setText(research.getTitle());
        Button addResearchButton = holder.researchButton;
        addResearchButton.setText("Participar");
        addResearchButton.setEnabled(true);
        Button descriptionButton = holder.descriptionButton;
        descriptionButton.setText("Descrição");
        descriptionButton.setEnabled(true);
    }

    @Override
    public int getItemCount() {
        return mResearches.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView nameTextView;
        public Button researchButton;
        public Button descriptionButton;
        ClickListener listener;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView, ClickListener listener) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            nameTextView = itemView.findViewById(R.id.rv_item_research_name);
            researchButton = itemView.findViewById(R.id.rv_item_new_research_button);
            descriptionButton = itemView.findViewById(R.id.rv_item_description_research_button);
            this.listener = listener;

            itemView.setOnClickListener(this);
            researchButton.setOnClickListener(this);
            descriptionButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onPositionClick(getAdapterPosition(), v);
        }
    }
}
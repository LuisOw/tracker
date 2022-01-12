package com.example.tracker3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class ResearchAdapter extends RecyclerView.Adapter<ResearchAdapter.ViewHolder> {
    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView nameTextView;
        public Button researchButton;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.rv_item_research_name);
            researchButton = (Button) itemView.findViewById(R.id.rv_item_research_button);
        }
    }

    private List<Research> mResearchs;

    // Pass in the contact array into the constructor
    public ResearchAdapter(List<Research> researchs) {
        mResearchs = researchs;
    }

    @Override
    public ResearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View researchView = inflater.inflate(R.layout.rv_research_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(researchView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ResearchAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        Research research = mResearchs.get(position);

        // Set item views based on your views and data model
        TextView textView = holder.nameTextView;
        textView.setText(research.getId().toString());
        Button button = holder.researchButton;
        button.setText("Simple test");
        button.setEnabled(true);
    }

    @Override
    public int getItemCount() {
        return mResearchs.size();
    }
}
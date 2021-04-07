package com.arcias.melocate.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arcias.melocate.R;

import java.util.List;

public class SuggestionAdapter extends RecyclerView.Adapter<SuggestionAdapter.ViewHolder> {
    private Context context;
    private List<String> mySuggestions;
    public SuggestionAdapter(Context context, List<String> mySuggestion)
    {
        this.context=context;
        this.mySuggestions=mySuggestion;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;


        view= LayoutInflater.from(context).inflate(R.layout.suggestion_item,parent
                ,false);
return new SuggestionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
String getText=mySuggestions.get(position);
holder.suggestionView.setText(getText);
    }

    @Override
    public int getItemCount() {
        return mySuggestions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView suggestionView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            suggestionView=itemView.findViewById(R.id.suggestion_text);
        }
    }

}

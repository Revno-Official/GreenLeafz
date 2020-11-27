package com.revno.greenleafz;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout containerView;
        public TextView textView;
        public Integer number;

        HistoryViewHolder(View view) {
            super(view);

            containerView = view.findViewById(R.id.history_row);
            textView = view.findViewById(R.id.history_row_name);

        }
    }

    public ArrayList<History> histories = new ArrayList<>();

    @NonNull
    @Override
    public HistoryAdapter.HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_row, parent, false);

        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.HistoryViewHolder holder, int position) {
        History current = histories.get(position);
        holder.containerView.setTag(current);
        holder.textView.setText(current.content);
    }

    @Override
    public int getItemCount() {
        return histories.size();
    }
    public void reload(){
        //histories = HistoryActivity.database.historyDao().getAll();
        notifyDataSetChanged();
    }
}

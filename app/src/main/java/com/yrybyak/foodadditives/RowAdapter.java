package com.yrybyak.foodadditives;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/** A list of single-line text rows that reports which row was clicked. */
final class RowAdapter extends RecyclerView.Adapter<RowAdapter.RowHolder> {

    interface OnRowClickListener {
        void onRowClick(int position);
    }

    private final List<String> labels;
    private final OnRowClickListener listener;

    RowAdapter(List<String> labels, OnRowClickListener listener) {
        this.labels = labels;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView view = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_row, parent, false);
        RowHolder holder = new RowHolder(view);
        view.setOnClickListener(v -> {
            int position = holder.getBindingAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                listener.onRowClick(position);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RowHolder holder, int position) {
        holder.text.setText(labels.get(position));
    }

    @Override
    public int getItemCount() {
        return labels.size();
    }

    static final class RowHolder extends RecyclerView.ViewHolder {

        final TextView text;

        RowHolder(TextView text) {
            super(text);
            this.text = text;
        }
    }
}

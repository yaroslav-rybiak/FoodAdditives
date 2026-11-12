package com.yrybiak.foodadditives;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/** A list of single-line text rows, optionally with a leading icon, that reports clicks. */
final class RowAdapter extends RecyclerView.Adapter<RowAdapter.RowHolder> {

    interface OnRowClickListener {
        void onRowClick(int position);
    }

    private final List<String> labels;
    @Nullable
    private final int[] icons;
    private final OnRowClickListener listener;

    /** Rows without icons. */
    RowAdapter(List<String> labels, OnRowClickListener listener) {
        this(labels, null, listener);
    }

    /** @param icons one drawable resource per label, or null for rows without icons */
    RowAdapter(List<String> labels, @Nullable @DrawableRes int[] icons,
               OnRowClickListener listener) {
        this.labels = labels;
        this.icons = icons;
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
        holder.text.setCompoundDrawablesRelativeWithIntrinsicBounds(
                icons == null ? 0 : icons[position], 0, 0, 0);
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

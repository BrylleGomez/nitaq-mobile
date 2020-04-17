package com.brylle.nitaq_mobapp;

import android.view.ViewGroup;
import android.widget.TextView;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;

import java.util.ArrayList;

public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.EventViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Package event);
    }

    // declaring some fields.
    private ArrayList<Package> list;
    private OnItemClickListener clickListener;

    public PackageAdapter(ArrayList<Package> arrayList, OnItemClickListener clickListener) {
        this.list = arrayList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public PackageAdapter.EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_courses_package_viewholder,parent,false);

        return new EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PackageAdapter.EventViewHolder holder, int position) {
        holder.bind(list.get(position), clickListener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // Inner ViewHolder Class for Package
    public class EventViewHolder extends RecyclerView.ViewHolder {
        TextView subjectView, moduleView, topicView;
        public EventViewHolder(View itemView) {
            super(itemView);
            subjectView = itemView.findViewById(R.id.fragment_courses_viewholder_subject);
            moduleView = itemView.findViewById(R.id.fragment_courses_viewholder_module);
            topicView = itemView.findViewById(R.id.fragment_courses_viewholder_topic);
        }

        public void bind(final Package pkg, final OnItemClickListener clickListener) {
            subjectView.setText(pkg.getSubject());
            moduleView.setText(pkg.getModule());
            topicView.setText(pkg.getTopic());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(pkg);
                }
            });
        }

    }

}

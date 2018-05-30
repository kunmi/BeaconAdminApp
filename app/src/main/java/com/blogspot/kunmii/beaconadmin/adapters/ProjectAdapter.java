package com.blogspot.kunmii.beaconadmin.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blogspot.kunmii.beaconadmin.R;
import com.blogspot.kunmii.beaconadmin.data.Project;

import java.util.List;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.RecyclerViewHolder>{

    List<Project> projects;
    public ProjectAdapter(List<Project> data){
        projects = data;
    }

    @NonNull
    @Override
    public ProjectAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.project_item_cardview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectAdapter.RecyclerViewHolder holder, int position) {
        Project project = projects.get(position);

        holder.titleTextView.setText(project.getName());
        holder.bodyTextView.setText(project.getDescription());
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }

    public void addItems(List<Project> data) {
        this.projects = data;
        notifyDataSetChanged();
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private TextView bodyTextView;

        RecyclerViewHolder(View view) {
            super(view);
            titleTextView = (TextView) view.findViewById(R.id.title_view);
            bodyTextView = (TextView) view.findViewById(R.id.body_view);
        }
    }
}

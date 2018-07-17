package com.blogspot.kunmii.beaconadmin.adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blogspot.kunmii.beaconadmin.R;
import com.blogspot.kunmii.beaconadmin.activities.MainActivity;
import com.blogspot.kunmii.beaconadmin.data.Project;
import com.blogspot.kunmii.beaconadmin.fragments.ProjectListFragment;

import java.util.List;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.RecyclerViewHolder>{

    List<Project> projects;
    ProjectListFragment.ProjectClickListener clickListener;
    public ProjectAdapter(List<Project> data, ProjectListFragment.ProjectClickListener listener){
        projects = data;
        clickListener = listener;
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
        holder.createdTextView.setText(project.getUpdated());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClick(project);
            }
        });

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
        private View itemView;
        private TextView titleTextView;
        private TextView bodyTextView;
        private TextView createdTextView;

        RecyclerViewHolder(View view) {
            super(view);
            itemView = view;
            titleTextView = (TextView) view.findViewById(R.id.title_view);
            bodyTextView = (TextView) view.findViewById(R.id.body_view);
            createdTextView = view.findViewById(R.id.created_view);
        }
    }
}

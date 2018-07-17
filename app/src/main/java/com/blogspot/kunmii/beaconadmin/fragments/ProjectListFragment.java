package com.blogspot.kunmii.beaconadmin.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.blogspot.kunmii.beaconadmin.ApplicationViewModel;
import com.blogspot.kunmii.beaconadmin.Helpers.Helpers;
import com.blogspot.kunmii.beaconadmin.R;
import com.blogspot.kunmii.beaconadmin.activities.LoginActivity;
import com.blogspot.kunmii.beaconadmin.activities.MainActivity;
import com.blogspot.kunmii.beaconadmin.adapters.ProjectAdapter;
import com.blogspot.kunmii.beaconadmin.data.Project;

import java.util.ArrayList;
import java.util.List;

public class ProjectListFragment extends Fragment{

    private ApplicationViewModel viewModel;
    private ProjectAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;

    static ProjectListFragment currentInstance = null;

    public static void getInstance()
    {
        if(currentInstance==null)
        {
            currentInstance = new ProjectListFragment();
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        setRetainInstance(true);

        View v = inflater.inflate(R.layout.fragment_list, container,false);

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerview);
        recyclerViewAdapter = new ProjectAdapter(new ArrayList<Project>(), new ProjectClickListener() {
            @Override
            public void onClick(Project project) {

                Bundle b = new Bundle();
                b.putString(FloorplanListFragment.PROJECT_ID_KEY, project.getObjectId());

                FloorplanListFragment frag = new FloorplanListFragment();
                frag.setArguments(b);

                ((MainActivity)getActivity()).switchFragment(frag, true);

            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.setAdapter(recyclerViewAdapter);




        return v;
    }



    @Override
    public void onResume() {
        super.onResume();

        ((MainActivity) getActivity()).toolbar.setSubtitle("Projects");


        if(Helpers.getUserToken(getActivity().getApplication()) == null)
        {
            startActivityForResult(new Intent(getActivity(), LoginActivity.class),1);
        }
        else
        {
            if(viewModel == null) {
                viewModel = ViewModelProviders.of(this).get(ApplicationViewModel.class);

                viewModel.getAllAsignedProjects().observe(this, new Observer<List<Project>>() {
                    @Override
                    public void onChanged(@Nullable List<Project> projects) {
                        recyclerViewAdapter.addItems(projects);
                    }
                });
            }
        }



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.logout:
                viewModel.LogOut();
                viewModel=null;
                getActivity().startActivityForResult(new Intent(getActivity(), LoginActivity.class),1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
    }

    public interface ProjectClickListener{
        public void onClick(Project project);
    }
}

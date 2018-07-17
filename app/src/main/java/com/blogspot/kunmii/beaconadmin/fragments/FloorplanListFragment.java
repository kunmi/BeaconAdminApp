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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.blogspot.kunmii.beaconadmin.ApplicationViewModel;
import com.blogspot.kunmii.beaconadmin.R;
import com.blogspot.kunmii.beaconadmin.activities.FloorplanActivity;
import com.blogspot.kunmii.beaconadmin.activities.MainActivity;
import com.blogspot.kunmii.beaconadmin.adapters.FloorplanAdapter;
import com.blogspot.kunmii.beaconadmin.data.FloorPlan;
import com.blogspot.kunmii.beaconadmin.data.FloorplanWithBeacons;

import java.util.ArrayList;
import java.util.List;

public class FloorplanListFragment extends Fragment{
    public static final String PROJECT_ID_KEY = "123";
    private ApplicationViewModel viewModel;
    private FloorplanAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;
    String projectId = null;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View v = inflater.inflate(R.layout.fragment_list, container, false);

        try {
            ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException eexp) {
            eexp.printStackTrace();
        }

        Bundle b = this.getArguments();
        if(b!=null) {
            projectId = b.getString(PROJECT_ID_KEY);
        }

            recyclerView = (RecyclerView) v.findViewById(R.id.recyclerview);
            recyclerViewAdapter = new FloorplanAdapter(new ArrayList<FloorplanWithBeacons>(), new FloorplanClickListener() {
                @Override
                public void onClick(FloorPlan plan) {

                    Intent i = new Intent(getActivity(), FloorplanActivity.class);
                    i.putExtra(FloorplanActivity.FLOORPLAN_ID, plan.getObjectId());
                    i.putExtra(FloorplanActivity.PROJECT_ID, projectId);

                    getActivity().startActivity(i);
                }
            });
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            recyclerView.setAdapter(recyclerViewAdapter);


        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(viewModel == null) {
            viewModel = ViewModelProviders.of(this).get(ApplicationViewModel.class);

            viewModel.getFloorplanForProject(projectId).observe(this, new Observer<List<FloorplanWithBeacons>>() {
                @Override
                public void onChanged(@Nullable List<FloorplanWithBeacons> floorPlans) {
                    recyclerViewAdapter.addItems(floorPlans);
                }
            });

            viewModel.getProject(projectId).observe(this, (project)->{
                if(project.getName()!=null){
                    if(getActivity()!=null)
                        ((MainActivity)getActivity()).toolbar.setSubtitle(project.getName());
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                ((MainActivity)getActivity()).popBackStack();
        }

        return super.onOptionsItemSelected(item);
    }

    public interface FloorplanClickListener {
        public void onClick(FloorPlan plan);
    }
}

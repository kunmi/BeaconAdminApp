package com.blogspot.kunmii.beaconadmin.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.blogspot.kunmii.beaconadmin.ApplicationViewModel;
import com.blogspot.kunmii.beaconadmin.R;
import com.blogspot.kunmii.beaconadmin.adapters.FloorplanAdapter;
import com.blogspot.kunmii.beaconadmin.adapters.ProjectAdapter;
import com.blogspot.kunmii.beaconadmin.data.FloorPlan;
import com.blogspot.kunmii.beaconadmin.data.FloorplanWithBeacons;
import com.blogspot.kunmii.beaconadmin.data.Project;

import java.util.ArrayList;
import java.util.List;

public class FloorPlanListActivity extends AppCompatActivity
{

    public static final String PROJECT_ID_KEY = "123";

    AppBarLayout layout;
    Toolbar toolbar;


    private ApplicationViewModel viewModel;
    private FloorplanAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;

    String projectId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        layout = findViewById(R.id.appbar_layout);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setSubtitle("Projects");

        projectId = getIntent().getStringExtra(PROJECT_ID_KEY);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerViewAdapter = new FloorplanAdapter(new ArrayList<FloorplanWithBeacons>(), new FloorplanClickListener() {
            @Override
            public void onClick(FloorPlan plan) {

            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(recyclerViewAdapter);


    }

    @Override
    protected void onResume() {
        super.onResume();
        if(viewModel == null) {
            viewModel = ViewModelProviders.of(this).get(ApplicationViewModel.class);

            viewModel.getFloorplanForProject(projectId).observe(FloorPlanListActivity.this, new Observer<List<FloorplanWithBeacons>>() {
                @Override
                public void onChanged(@Nullable List<FloorplanWithBeacons> floorPlans) {
                    recyclerViewAdapter.addItems(floorPlans);
                }
            });

            viewModel.getProject(projectId).observe(FloorPlanListActivity.this, (project)->{
                if(project.getName()!=null)
                toolbar.setSubtitle(project.getName());
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
        }


        return super.onOptionsItemSelected(item);
    }

    public interface FloorplanClickListener {
        public void onClick(FloorPlan plan);
    }
}

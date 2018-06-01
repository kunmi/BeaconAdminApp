package com.blogspot.kunmii.beaconadmin.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;

import com.blogspot.kunmii.beaconadmin.AppDatabase;
import com.blogspot.kunmii.beaconadmin.ApplicationViewModel;
import com.blogspot.kunmii.beaconadmin.Helpers.Helpers;
import com.blogspot.kunmii.beaconadmin.R;
import com.blogspot.kunmii.beaconadmin.adapters.ProjectAdapter;
import com.blogspot.kunmii.beaconadmin.data.Project;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    AppBarLayout layout;
    Toolbar toolbar;


    private ApplicationViewModel viewModel;
    private ProjectAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        layout = findViewById(R.id.appbar_layout);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        toolbar.setSubtitle("Projects");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerViewAdapter = new ProjectAdapter(new ArrayList<Project>(), new ProjectClickListener() {
            @Override
            public void onClick(Project project) {
                Intent i = new Intent(MainActivity.this, FloorPlanListActivity.class);
                i.putExtra(FloorPlanListActivity.PROJECT_ID_KEY, project.getObjectId());
                startActivity(i);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(recyclerViewAdapter);

    }


    @Override
    protected void onResume() {
        super.onResume();

        if(Helpers.getUserToken(getApplication()) == null)
        {
            startActivityForResult(new Intent(this, LoginActivity.class),1);
        }
        else
        {
            if(viewModel == null) {
                viewModel = ViewModelProviders.of(this).get(ApplicationViewModel.class);

                viewModel.getAllAsignedProjects().observe(MainActivity.this, new Observer<List<Project>>() {
                    @Override
                    public void onChanged(@Nullable List<Project> projects) {
                        recyclerViewAdapter.addItems(projects);
                    }
                });
            }
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public interface ProjectClickListener{
        public void onClick(Project project);
    }
}

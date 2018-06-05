package com.blogspot.kunmii.beaconadmin.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.blogspot.kunmii.beaconadmin.ApplicationViewModel;
import com.blogspot.kunmii.beaconadmin.Config;
import com.blogspot.kunmii.beaconadmin.Helpers.BeaconHelper;
import com.blogspot.kunmii.beaconadmin.Helpers.FloorImageView;
import com.blogspot.kunmii.beaconadmin.R;
import com.blogspot.kunmii.beaconadmin.data.Beacon;
import com.blogspot.kunmii.beaconadmin.data.FloorPlan;
import com.blogspot.kunmii.beaconadmin.data.FloorplanWithBeacons;
import com.blogspot.kunmii.beaconadmin.data.Project;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FloorplanActivity extends AppCompatActivity{

    AppBarLayout layout;
    Toolbar toolbar;

    public static final String FLOORPLAN_ID = "floorplan";
    public static final String PROJECT_ID = "project";

    String floorplanId;
    String projectId;

    private ApplicationViewModel viewModel;

    FloorPlan floorPlan = null;

    List<Beacon> beacons = null;
    List<Beacon> newBeacons = new ArrayList<>();

    FloorImageView imageView;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_floorplan);


        layout = findViewById(R.id.appbar_layout);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        catch (NullPointerException eexp)
        {
            eexp.printStackTrace();
        }

        floorplanId = getIntent().getStringExtra(FLOORPLAN_ID);
        projectId = getIntent().getStringExtra(PROJECT_ID);


        imageView = (FloorImageView)findViewById(R.id.floorplan_view);


    }

    @Override
    protected void onResume() {
        super.onResume();
        if(viewModel == null) {
            viewModel = ViewModelProviders.of(this).get(ApplicationViewModel.class);

            viewModel.getFloorplanWithId(floorplanId).observe(this, new Observer<FloorplanWithBeacons>() {
                @Override
                public void onChanged(@Nullable FloorplanWithBeacons floorplanWithBeacons) {

                    if(floorplanWithBeacons.getFloorPlan()!=null) {

                        floorPlan = floorplanWithBeacons.getFloorPlan();
                        beacons = floorplanWithBeacons.getBeacons();

                        toolbar.setSubtitle(String.valueOf(floorPlan.getName()));

                        Picasso.get().load(Config.generateImageUrl(floorPlan.getFileurl())).into(imageView);
                        //imageView.initialize
                        floorPlan = floorplanWithBeacons.getFloorPlan();
                        beacons = floorplanWithBeacons.getBeacons();
                        imageView.setBeacons(beacons);
                    }
                }
            });

            viewModel.getProject(projectId).observe(this, (project -> {

                    if(project!=null)
                    {
                        toolbar.setTitle(String.valueOf(project.getName()));
                    }

            }));
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;

            case R.id.addbeacon:
                // Create and show the dialog.
                Intent i = new Intent(this, ActivityBeaconList.class);
                startActivityForResult(i,ActivityBeaconList.RC);
                break;

        }



        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.floormenu, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ActivityBeaconList.RC)
        {

        }



    }
}

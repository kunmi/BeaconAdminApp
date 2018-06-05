package com.blogspot.kunmii.beaconadmin.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.blogspot.kunmii.beaconadmin.ApplicationViewModel;
import com.blogspot.kunmii.beaconadmin.Helpers.BeaconHelper;
import com.blogspot.kunmii.beaconadmin.R;
import com.blogspot.kunmii.beaconadmin.adapters.CustomAdapter;
import com.blogspot.kunmii.beaconadmin.adapters.EddyListAdapter;
import com.blogspot.kunmii.beaconadmin.adapters.FloorplanAdapter;
import com.blogspot.kunmii.beaconadmin.adapters.IBeaconAdapter;
import com.blogspot.kunmii.beaconadmin.data.FloorPlan;
import com.blogspot.kunmii.beaconadmin.data.FloorplanWithBeacons;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ActivityBeaconList extends AppCompatActivity{

    public static final int RC = 123;

    AppBarLayout layout;
    Toolbar toolbar;


    private ApplicationViewModel viewModel;

    TabLayout tabLayout;
    ViewPager viewPager;

    TabFragment.IBeaconFragment iBeaconFragment;
    TabFragment.EddyFragment eddyFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_beacon);


        layout = findViewById(R.id.appbar_layout);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.masterViewPager);
        viewPager.setOffscreenPageLimit(3);

        iBeaconFragment = TabFragment.IBeaconFragment.getInstance(new IBeaconAdapter.IbeaconListClickListener() {
            @Override
            public void onClick(BeaconHelper.IBeaconWrapper item) {
                JSONObject
            }
        });

        eddyFragment = TabFragment.EddyFragment.getInstance(new EddyListAdapter.EddyListClickListener() {
            @Override
            public void onClick(BeaconHelper.EddystoneWrapper item) {

            }
        });



        CustomAdapter adapter = new CustomAdapter(getSupportFragmentManager());
        adapter.addFragment("Ibeacon", iBeaconFragment);


        adapter.addFragment("Eddystone", eddyFragment);

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    protected void onResume() {
        super.onResume();

        if(viewModel == null) {
            viewModel = ViewModelProviders.of(this).get(ApplicationViewModel.class);


            viewModel.getIbeaconDevices().observe(this, new Observer<HashMap<String, BeaconHelper.IBeaconWrapper>>() {
                @Override
                public void onChanged(@Nullable HashMap<String, BeaconHelper.IBeaconWrapper> stringIBeaconWrapperHashMap) {
                    Log.d("","");
                    iBeaconFragment.setItems(new java.util.ArrayList<>(stringIBeaconWrapperHashMap.values()));

                    //adapter.addItems(new java.util.ArrayList<>(stringIBeaconWrapperHashMap.values()));
                }
            });

            viewModel.getEddystoneDevices().observe(this, new Observer<HashMap<String, BeaconHelper.EddystoneWrapper>>() {
                @Override
                public void onChanged(@Nullable HashMap<String, BeaconHelper.EddystoneWrapper> stringEddyWrapperHashMap) {

                    eddyFragment.setItems(new java.util.ArrayList<>(stringEddyWrapperHashMap.values()));

                    //adapter.addItems(new java.util.ArrayList<>(stringEddyWrapperHashMap.values()));
                }
            });


        }


    }
}

package com.blogspot.kunmii.beaconadmin.activities;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.blogspot.kunmii.beaconadmin.AppDatabase;
import com.blogspot.kunmii.beaconadmin.ApplicationViewModel;
import com.blogspot.kunmii.beaconadmin.Helpers.Helpers;
import com.blogspot.kunmii.beaconadmin.R;
import com.blogspot.kunmii.beaconadmin.adapters.ProjectAdapter;
import com.blogspot.kunmii.beaconadmin.data.Project;
import com.blogspot.kunmii.beaconadmin.fragments.ProjectListFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

   AppBarLayout layout;
   public Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = findViewById(R.id.appbar_layout);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        toolbar.setSubtitle("Projects");

        checkPermissionAndStart();

        switchFragment(new ProjectListFragment(), false);
    }


    @Override
    protected void onResume() {
        super.onResume();

        if(Helpers.getUserToken(getApplication()) == null)
        {
            startActivityForResult(new Intent(this, LoginActivity.class),1);
        }
    }


    public void switchFragment(Fragment f, boolean addToBackStack){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();


        if(addToBackStack){
            transaction.add(R.id.fragmentLayout, f);
            transaction.addToBackStack(f.getClass().getName());
        }
        else
        {
            transaction.add(R.id.fragmentLayout, f);
        }


        transaction.commit();
    }

    public void popBackStack()
    {
        FragmentManager fm = getSupportFragmentManager();

        fm.popBackStack();
    }

    private void checkPermissionAndStart() {
        int checkSelfPermissionResult = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (PackageManager.PERMISSION_GRANTED == checkSelfPermissionResult) {
            //already granted
            //startScan();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                //we should show some explanation for user here
                //showExplanationDialog();
            } else {
                //request permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (100 == requestCode) {
                //same request code as was in request permission

            }

        } else {
            //not granted permission
            //show some explanation dialog that some features will not work
        }
    }




}

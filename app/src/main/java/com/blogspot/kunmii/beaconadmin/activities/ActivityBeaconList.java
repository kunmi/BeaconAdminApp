package com.blogspot.kunmii.beaconadmin.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.blogspot.kunmii.beaconadmin.ApplicationViewModel;
import com.blogspot.kunmii.beaconadmin.Config;
import com.blogspot.kunmii.beaconadmin.Dialog.DialogAddBeacon;
import com.blogspot.kunmii.beaconadmin.Dialog.DialogBeacon;
import com.blogspot.kunmii.beaconadmin.Dialog.TabbedFragment;
import com.blogspot.kunmii.beaconadmin.Helpers.BeaconHelper;
import com.blogspot.kunmii.beaconadmin.Helpers.Helpers;
import com.blogspot.kunmii.beaconadmin.R;
import com.blogspot.kunmii.beaconadmin.adapters.CustomAdapter;
import com.blogspot.kunmii.beaconadmin.adapters.EddyListAdapter;
import com.blogspot.kunmii.beaconadmin.adapters.IBeaconAdapter;
import com.kontakt.sdk.android.common.profile.IBeaconDevice;
import com.kontakt.sdk.android.common.profile.IEddystoneDevice;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ActivityBeaconList extends AppCompatActivity{

    public static final int RC = 123;
    public static final String BEACON_JSON = "beaconjson";

    AppBarLayout layout;
    Toolbar toolbar;


    private ApplicationViewModel viewModel;

    TabLayout tabLayout;
    ViewPager viewPager;

    TabbedFragment.IBeaconFragment iBeaconFragment;
    TabbedFragment.EddyFragment eddyFragment;

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

        iBeaconFragment = TabbedFragment.IBeaconFragment.getInstance(new IBeaconAdapter.IbeaconListClickListener() {
            @Override
            public void onClick(IBeaconDevice beaconWrapper) {

                Helpers.showDialog(ActivityBeaconList.this, "Add beacon", "Add beacon to Floorplan","Add Beacon",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                JSONObject iBeacon = Helpers.createIBeaconJSON(getApplication());

                                if(iBeacon!=null)
                                {
                                    try {
                                        iBeacon.put(Config.NETWORK_JSON_NODE.IBEACON_UUID, beaconWrapper.getProximityUUID().toString());
                                        iBeacon.put(Config.NETWORK_JSON_NODE.IBEACON_MAJOR, beaconWrapper.getMajor());
                                        iBeacon.put(Config.NETWORK_JSON_NODE.IBEACON_MINOR, beaconWrapper.getMinor());

                                        iBeacon.put(Config.NETWORK_JSON_NODE.BEACON_TXPOWER, beaconWrapper.getTxPower());

                                        submitResult(iBeacon.toString());

                                    }
                                    catch (JSONException exp)
                                    {
                                        exp.printStackTrace();
                                        Helpers.showDialog(ActivityBeaconList.this, "Error", exp.getMessage(), "Close", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });
                                    }
                                }
                            }
                        });

            }
        });

        eddyFragment = TabbedFragment.EddyFragment.getInstance(new EddyListAdapter.EddyListClickListener() {
            @Override
            public void onClick(IEddystoneDevice beaconWrapper) {
                Helpers.showDialog(ActivityBeaconList.this, "Add beacon", "Add beacon to Floorplan","Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                JSONObject eddyBeacon = Helpers.createEddystoneJson(getApplication());

                                if(eddyBeacon!=null)
                                {
                                    try {
                                        eddyBeacon.put(Config.NETWORK_JSON_NODE.EDDY_NAMESPACEID, beaconWrapper.getNamespace());
                                        eddyBeacon.put(Config.NETWORK_JSON_NODE.EDDY_INSTANCEID, beaconWrapper.getInstanceId());

                                        if(beaconWrapper.getTelemetry()!=null)
                                            eddyBeacon.put(Config.NETWORK_JSON_NODE.EDDY_TELEMETRY, beaconWrapper.getTelemetry().toString());
                                        else
                                            eddyBeacon.put(Config.NETWORK_JSON_NODE.EDDY_TELEMETRY, null);

                                        eddyBeacon.put(Config.NETWORK_JSON_NODE.BEACON_TXPOWER, beaconWrapper.getTxPower());
                                        submitResult(eddyBeacon.toString());

                                    }
                                    catch (JSONException exp)
                                    {
                                        exp.printStackTrace();
                                        Helpers.showDialog(ActivityBeaconList.this, "Error", exp.getMessage(), "Close", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });
                                    }
                                }
                            }
                        });
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


            viewModel.getIbeaconDevices().observe(this, new Observer<HashMap<String, IBeaconDevice>>() {
                @Override
                public void onChanged(@Nullable HashMap<String, IBeaconDevice> stringIBeaconWrapperHashMap) {
                    Log.d("","");
                    iBeaconFragment.setItems(new java.util.ArrayList<>(stringIBeaconWrapperHashMap.values()));

                    //adapter.addItems(new java.util.ArrayList<>(stringIBeaconWrapperHashMap.values()));
                }
            });

            viewModel.getEddystoneDevices().observe(this, new Observer<HashMap<String, IEddystoneDevice>>() {
                @Override
                public void onChanged(@Nullable HashMap<String, IEddystoneDevice> stringEddyWrapperHashMap) {

                    eddyFragment.setItems(new java.util.ArrayList<>(stringEddyWrapperHashMap.values()));

                    //adapter.addItems(new java.util.ArrayList<>(stringEddyWrapperHashMap.values()));
                }
            });


        }


    }

    void submitResult(String result)
    {
        if(result!=null) {
            Intent i = new Intent();
            i.putExtra(BEACON_JSON, result);
            setResult(RC, i);
        }
        finish();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_beacon_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;

            case R.id.addmanual:
                showManualBeaconDialog();
                break;


        }

        return super.onOptionsItemSelected(item);
    }


    void showManualBeaconDialog(){

        DialogAddBeacon dialogBeacon = new DialogAddBeacon();

        dialogBeacon.setSaveListener(this::submitResult);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);



        // Create and show the dialog.
        dialogBeacon.show(ft,"dialog");
    }
}

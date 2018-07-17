package com.blogspot.kunmii.beaconadmin.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.blogspot.kunmii.beaconadmin.ApplicationViewModel;
import com.blogspot.kunmii.beaconadmin.Config;
import com.blogspot.kunmii.beaconadmin.Dialog.DialogBeacon;
import com.blogspot.kunmii.beaconadmin.Dialog.DialogSendMessage;
import com.blogspot.kunmii.beaconadmin.Helpers.BeaconHelper;
import com.blogspot.kunmii.beaconadmin.Helpers.FloorImageView;
import com.blogspot.kunmii.beaconadmin.Helpers.Helpers;
import com.blogspot.kunmii.beaconadmin.R;
import com.blogspot.kunmii.beaconadmin.data.Beacon;
import com.blogspot.kunmii.beaconadmin.data.FloorPlan;
import com.blogspot.kunmii.beaconadmin.data.FloorplanWithBeacons;
import com.kontakt.sdk.android.ble.device.EddystoneDevice;
import com.kontakt.sdk.android.common.Proximity;
import com.kontakt.sdk.android.common.profile.IBeaconDevice;
import com.kontakt.sdk.android.common.profile.IEddystoneDevice;
import com.kontakt.sdk.android.common.profile.RemoteBluetoothDevice;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FloorplanActivity extends AppCompatActivity {

    AppBarLayout layout;
    Toolbar toolbar;

    public static final String FLOORPLAN_ID = "floorplan";
    public static final String PROJECT_ID = "project";

    String floorplanId;
    String projectId;

    private ApplicationViewModel viewModel;

    FloorPlan floorPlan = null;

    List<Beacon> beacons = new ArrayList<>();
    List<Beacon> newBeacons = new ArrayList<>();

    FloorImageView imageView;
    Beacon newBeacon = null;

    ProgressBar progressBar = null;

    boolean selectingForMessage = false;
    HashMap<String, Beacon> selectedForMessage = new HashMap<>();



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_floorplan);


        layout = findViewById(R.id.appbar_layout);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException eexp) {
            eexp.printStackTrace();
        }

        progressBar = findViewById(R.id.progressbar);

        floorplanId = getIntent().getStringExtra(FLOORPLAN_ID);
        projectId = getIntent().getStringExtra(PROJECT_ID);


        imageView = (FloorImageView) findViewById(R.id.floorplan_view);
        final GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {

                if (imageView.isReady()) {

                    PointF tappedPoint = new PointF(e.getX(), e.getY());

                    for (Beacon b : beacons) {

                        PointF beaconCoords = imageView.sourceToViewCoord(b.getCoordsAsPixel(imageView.getImageWidth(), imageView.getImageHeight()));
                        Bitmap pin = imageView.getPin(b);

                        Float pictureStartX = beaconCoords.x - (pin.getWidth() / 2);
                        Float pictureEndX = beaconCoords.x + (pin.getWidth() / 2);

                        Float pictureStartY = beaconCoords.y;
                        Float pictureEndY = beaconCoords.y + (pin.getHeight());

                        if (tappedPoint.x >= pictureStartX &&
                                tappedPoint.x <= pictureEndX &&
                                tappedPoint.y >= pictureStartY &&
                                tappedPoint.y <= pictureEndY) {

                            if(!selectingForMessage) {
                                showBeaconDetailDialog(b);
                            }
                            else
                            {
                                imageView.select(b);
                            }

                            break;

                        }

                    }


                }


                return super.onSingleTapConfirmed(e);
            }

            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);

                if (imageView.isReady()) {
                    PointF sCoord = imageView.viewToSourceCoord(e.getX(), e.getY());

                    if (newBeacon != null) {
                        int imageWidth = imageView.getImageWidth();
                        int imageheight = imageView.getImageHeight();

                        if (sCoord != null) {
                            newBeacon.setX((sCoord.x / imageWidth) * 100);
                            newBeacon.setY((sCoord.y / imageheight) * 100);
                        }


                        try {
                            JSONObject jsonBeacon = new JSONObject(newBeacon.getBeaconData());

                            JSONObject map = jsonBeacon.getJSONObject(Config.NETWORK_JSON_NODE.BEACON_MAP);
                            map.put(Config.NETWORK_JSON_NODE.BEACON_MAP_X, String.valueOf(newBeacon.getX()));
                            map.put(Config.NETWORK_JSON_NODE.BEACON__MAP_Y, String.valueOf(newBeacon.getY()));

                            jsonBeacon.put(Config.NETWORK_JSON_NODE.BEACON_MAP, map);
                            newBeacon.setBeaconData(jsonBeacon.toString());

                            newBeacons.add(newBeacon);
                            newBeacon = null;

                            imageView.setBeacons(beacons, newBeacons);

                            imageView.invalidate();
                            invalidateOptionsMenu();

                        } catch (Exception exp) {
                            exp.printStackTrace();
                            Helpers.showDialog(FloorplanActivity.this, "An Error Occured", exp.getMessage());

                        }

                    }


                }

            }

        });

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (viewModel == null) {
            viewModel = ViewModelProviders.of(this).get(ApplicationViewModel.class);

            viewModel.getFloorplanWithId(floorplanId).observe(this, new Observer<FloorplanWithBeacons>() {
                @Override
                public void onChanged(@Nullable FloorplanWithBeacons floorplanWithBeacons) {

                    if (floorplanWithBeacons.getFloorPlan() != null) {

                        floorPlan = floorplanWithBeacons.getFloorPlan();
                        beacons = floorplanWithBeacons.getBeacons();

                        toolbar.setSubtitle(String.valueOf(floorPlan.getName()));

                        if (!imageView.hasImageAlready())
                            Picasso.get().load(Config.generateImageUrl(floorPlan.getFileurl())).into(imageView);

                        //imageView.initialize
                        floorPlan = floorplanWithBeacons.getFloorPlan();
                        beacons = floorplanWithBeacons.getBeacons();

                        newBeacons.clear();
                        imageView.setBeacons(beacons, newBeacons);

                        progressBar.setVisibility(View.GONE);

                    }
                }
            });

            viewModel.getProject(projectId).observe(this, (project -> {

                if (project != null) {
                    toolbar.setTitle(String.valueOf(project.getName()));
                }

            }));

            viewModel.getUpdatedIbeacon().observe(this, (List<IBeaconDevice> devices) -> {

                List<Beacon> allBeacons = new ArrayList<>();
                allBeacons.addAll(beacons);
                allBeacons.addAll(newBeacons);

                boolean needsUpdate = false;

                for (IBeaconDevice device : devices) {
                    if (device.getProximityUUID() != null) {
                        Beacon beacon = retrieveBeacon(device, allBeacons);

                        if(beacon != null)
                        {
                            Beacon.Proximity proximity = resolveProximity(device.getProximity());
                            if (beacon.proximity != proximity) {
                                needsUpdate = true;
                                beacon.proximity = proximity;
                            }
                        }

                    }
                }


                if(needsUpdate)
                    imageView.invalidate();

            });

            viewModel.getUpdatedEddystone().observe(this, (List<IEddystoneDevice> devices) -> {


                List<Beacon> allBeacons = new ArrayList<>();
                allBeacons.addAll(beacons);
                allBeacons.addAll(newBeacons);

                boolean needsUpdate = false;

                if(devices!=null) {

                    for (IEddystoneDevice device : devices) {
                        if (device.getNamespace() != null) {

                            Beacon beacon = retrieveBeacon(device, allBeacons);
                            if(beacon!=null)
                            {
                                Beacon.Proximity proximity = resolveProximity(device.getProximity());
                                if (beacon.proximity != proximity) {
                                    needsUpdate = true;
                                    beacon.proximity = proximity;
                                }

                            }
                        }

                    }
                    if(needsUpdate)
                        imageView.invalidate();
                }

            });

            viewModel.getLostIBeacon().observe(this, (IBeaconDevice device) -> {


                Beacon b = retrieveBeacon(device,beacons);
                if(b!=null)
                {
                    if (b.proximity != Beacon.Proximity.OUT_OF_RANGE) {
                        b.proximity = Beacon.Proximity.OUT_OF_RANGE;
                        imageView.invalidate();
                    }
                }

            });

            viewModel.getLostEddyBeacon().observe(this, (IEddystoneDevice device) -> {
                Beacon b = retrieveBeacon(device,beacons);
                if(b!=null)
                {
                    if (b.proximity != Beacon.Proximity.OUT_OF_RANGE) {
                        b.proximity = Beacon.Proximity.OUT_OF_RANGE;
                        imageView.invalidate();
                    }
                }
            });


        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            case R.id.addbeacon:
                // Create and show the dialog.
                Intent i = new Intent(this, ActivityBeaconList.class);
                startActivityForResult(i, ActivityBeaconList.RC);
                break;


            case R.id.savebeacons:
                progressBar.setVisibility(View.VISIBLE);

                viewModel.saveBeacons(
                        (boolean success) -> {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                        },
                        newBeacons,
                        projectId,
                        floorplanId);
                break;
            case R.id.sendMessage:
                selectingForMessage = true;
                imageView.setSelectable();
                invalidateOptionsMenu();
                break;

            case R.id.cancel:

                selectingForMessage = false;
                imageView.finishSelection();
                invalidateOptionsMenu();

                break;
            case R.id.done:

                List<Beacon> selectedBeacons = new ArrayList<>(imageView.getSelectedBeacons().values());
                showSendMessageDialog(selectedBeacons);
                imageView.finishSelection();
                selectingForMessage = false;
                invalidateOptionsMenu();

                break;

        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(!selectingForMessage) {
            menu.findItem(R.id.addbeacon).setVisible(true);

            menu.findItem(R.id.done).setVisible(false);
            menu.findItem(R.id.cancel).setVisible(false);

            if (newBeacons.size() > 0) {
                menu.findItem(R.id.savebeacons).setVisible(true);
                menu.findItem(R.id.sendMessage).setVisible(false);
            } else {
                menu.findItem(R.id.savebeacons).setVisible(false);
                menu.findItem(R.id.sendMessage).setVisible(true);
            }
        }
        else
        {
            menu.findItem(R.id.savebeacons).setVisible(false);
            menu.findItem(R.id.addbeacon).setVisible(false);
            menu.findItem(R.id.sendMessage).setVisible(false);

            menu.findItem(R.id.done).setVisible(true);
            menu.findItem(R.id.cancel).setVisible(true);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.floormenu, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.stopBeaconScan();
        viewModel.destroyBeaconManager();
        viewModel = null;

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ActivityBeaconList.RC) {


            if(data!=null)
                try {
                    newBeacon = new Beacon();

                    JSONObject json = new JSONObject(data.getStringExtra(ActivityBeaconList.BEACON_JSON));
                    newBeacon.setTxpower(json.getString(Config.NETWORK_JSON_NODE.BEACON_TXPOWER));
                    newBeacon.setType(json.getString(Config.NETWORK_JSON_NODE.BEACON_TYPE));
                    newBeacon.setBeaconData(json.toString());

                    Toast.makeText(this, "Long press on location to place on the map", Toast.LENGTH_SHORT).show();
                    imageView.setDropPinMode(true);


                } catch (Exception exp) {
                    exp.printStackTrace();
                    Helpers.showDialog(this, "Error", exp.getMessage(), "Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                }
        }
    }


    Beacon.Proximity resolveProximity(Proximity proximity)
    {
        switch (proximity)
        {
            case IMMEDIATE:
                return Beacon.Proximity.IMMEDIATE;
            case NEAR:
                return  Beacon.Proximity.NEAR;
            case FAR:
                return Beacon.Proximity.FAR;
        }
        return Beacon.Proximity.OUT_OF_RANGE;
    }


    Beacon retrieveBeacon(RemoteBluetoothDevice beaconDevice, List<Beacon> beaconCollections)
    {

        if(beaconDevice instanceof IBeaconDevice) {

            IBeaconDevice device = (IBeaconDevice) beaconDevice;
            for(Beacon b : beaconCollections)
            {

                if(!b.getType().equals("iBeacon"))
                    continue;

                try {
                    JSONObject jsonObject = new JSONObject(b.getBeaconData());

                    if (device.getProximityUUID().toString().equals(jsonObject.getString(Config.NETWORK_JSON_NODE.IBEACON_UUID))) {


                        if (device.getMajor() == Integer.parseInt(jsonObject.getString(Config.NETWORK_JSON_NODE.IBEACON_MAJOR)))
                        {
                            if(device.getMinor() == Integer.parseInt(jsonObject.getString(Config.NETWORK_JSON_NODE.IBEACON_MINOR))){
                                return b;
                            }

                        }
                    }

                } catch (JSONException exp) {
                    exp.printStackTrace();
                }
            }

        }
        else
        {
            IEddystoneDevice device = (IEddystoneDevice) beaconDevice;

            for(Beacon b : beaconCollections) {

                if(b.getType().equals("iBeacon"))
                    continue;

                try {
                    JSONObject jsonObject = new JSONObject(b.getBeaconData());

                    if (device.getNamespace().equals(jsonObject.getString(Config.NETWORK_JSON_NODE.EDDY_NAMESPACEID))) {

                        if (device.getInstanceId().equals(jsonObject.getString(Config.NETWORK_JSON_NODE.EDDY_INSTANCEID))) {

                            return b;

                        }
                    }

                } catch (JSONException exp) {
                    exp.printStackTrace();
                }
            }

        }

        return null;
    }

    void showBeaconDetailDialog(Beacon b)
    {

        DialogBeacon dialogBeacon = new DialogBeacon();
        dialogBeacon.setBeacon(b, (beac) -> {
            if (beac != null)
                viewModel.updateBeacon(beac, (successful) -> {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (successful)
                                Toast.makeText(getApplicationContext(), "Update successful", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(getApplicationContext(), "Update failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                });

        });

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        dialogBeacon.show(ft, "dialog");
    }


    void showSendMessageDialog(List<Beacon> selectedBeacons)
    {
        if(selectedBeacons.size()>0) {
            DialogSendMessage dialogBeacon = new DialogSendMessage();

            dialogBeacon.setData(new DialogSendMessage.DialogResultListener() {
                @Override
                public void dialogResult(List<Beacon> beacons, String title, String body) {


                    runOnUiThread(()->{
                        Toast.makeText(FloorplanActivity.this, "Sending Message", Toast.LENGTH_SHORT).show();
                    });

                    viewModel.sensMessage(title, body, beacons ,projectId, floorplanId, (result)->{

                        runOnUiThread(()->{
                            if(result){
                                Toast.makeText(FloorplanActivity.this, "Sent successfully", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(FloorplanActivity.this, "Sending Failed :(", Toast.LENGTH_SHORT).show();
                            }

                        });


                    });

                }
            }, selectedBeacons);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);

            // Create and show the dialog.
            dialogBeacon.show(ft, "dialog");
        }
        else
        {
            Helpers.showDialog(this, "No Beacon selected", "Okay");
        }
    }


}
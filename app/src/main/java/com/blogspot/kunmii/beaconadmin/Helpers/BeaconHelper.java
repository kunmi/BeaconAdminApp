package com.blogspot.kunmii.beaconadmin.Helpers;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.kontakt.sdk.android.ble.connection.OnServiceReadyListener;
import com.kontakt.sdk.android.ble.device.EddystoneDevice;
import com.kontakt.sdk.android.ble.manager.ProximityManager;
import com.kontakt.sdk.android.ble.manager.ProximityManagerFactory;
import com.kontakt.sdk.android.ble.manager.listeners.EddystoneListener;
import com.kontakt.sdk.android.ble.manager.listeners.IBeaconListener;
import com.kontakt.sdk.android.ble.manager.listeners.simple.SimpleEddystoneListener;
import com.kontakt.sdk.android.ble.manager.listeners.simple.SimpleIBeaconListener;
import com.kontakt.sdk.android.common.KontaktSDK;
import com.kontakt.sdk.android.common.profile.IBeaconDevice;
import com.kontakt.sdk.android.common.profile.IBeaconRegion;
import com.kontakt.sdk.android.common.profile.IEddystoneDevice;
import com.kontakt.sdk.android.common.profile.IEddystoneNamespace;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

public class BeaconHelper {

    private BeaconManager beaconManager;
    private ProximityManager proximityManager;

    HashMap<String, IBeaconWrapper> ibeacons = new HashMap<>();
    HashMap<String, EddystoneWrapper> eddystones = new HashMap<>();


    MutableLiveData<HashMap<String, IBeaconWrapper>> beaconDeviceLiveData = new MutableLiveData<>();
    MutableLiveData<HashMap<String, EddystoneWrapper>> eddystoneDeviceLiveData = new MutableLiveData<>();

    public BeaconHelper(Application context) {

        KontaktSDK.initialize(context);

        proximityManager = ProximityManagerFactory.create(context);
        proximityManager.setIBeaconListener(createIBeaconListener());
        proximityManager.setEddystoneListener(createEddystoneListener());
    }


    public void onStart(){
        startScanning();
    }

    public void onStop(){
        proximityManager.stopScanning();
    }

    public void onDestroy(){
        proximityManager.disconnect();
        proximityManager = null;
    }

    private void startScanning() {
        proximityManager.connect(new OnServiceReadyListener() {
            @Override
            public void onServiceReady() {
                proximityManager.startScanning();
            }
        });
    }

    private IBeaconListener createIBeaconListener() {
        return new SimpleIBeaconListener() {
            @Override
            public void onIBeaconDiscovered(IBeaconDevice ibeacon, IBeaconRegion region) {
                Log.i("Sample", "IBeacon discovered: " + ibeacon.toString());

                String key = ibeacon.getAddress();


                ibeacons.put(key, new IBeaconWrapper(region,ibeacon));
                beaconDeviceLiveData.setValue(ibeacons);


            }

            @Override
            public void onIBeaconsUpdated(List<IBeaconDevice> ibeacons, IBeaconRegion region) {

            }

            @Override
            public void onIBeaconLost(IBeaconDevice ibeacon, IBeaconRegion region) {
                super.onIBeaconLost(ibeacon, region);


                String key = ibeacon.getAddress();

                if(ibeacons.containsKey(key))
                {
                    ibeacons.remove(key);
                    beaconDeviceLiveData.setValue(ibeacons);
                }


            }
        };
    }

    private EddystoneListener createEddystoneListener() {
        return new SimpleEddystoneListener() {
            @Override
            public void onEddystoneDiscovered(IEddystoneDevice eddystone, IEddystoneNamespace namespace) {
                Log.i("Sample", "Eddystone discovered: " + eddystone.toString());

                String key = eddystone.getAddress();

                eddystones.put(key, new EddystoneWrapper(namespace,eddystone));
                eddystoneDeviceLiveData.setValue(eddystones);
            }

            @Override
            public void onEddystonesUpdated(List<IEddystoneDevice> eddystones, IEddystoneNamespace namespace) {
                super.onEddystonesUpdated(eddystones, namespace);
            }

            @Override
            public void onEddystoneLost(IEddystoneDevice eddystone, IEddystoneNamespace namespace) {
                super.onEddystoneLost(eddystone, namespace);

                String key = eddystone.getAddress();

                if(eddystones.containsKey(key))
                {
                    eddystones.remove(key);
                    eddystoneDeviceLiveData.setValue(eddystones);
                }
            }
        };
    }


    public MutableLiveData<HashMap<String, IBeaconWrapper>> getIBeaconDeviceLiveData() {
        return beaconDeviceLiveData;
    }

    public MutableLiveData<HashMap<String, EddystoneWrapper>> getEddystoneDeviceLiveData() {
        return eddystoneDeviceLiveData;
    }







    public class IBeaconWrapper{

        public IBeaconRegion region;
        public IBeaconDevice device;

        public IBeaconWrapper(IBeaconRegion reg, IBeaconDevice dev)
        {
            this.region = reg;
            this.device = dev;
        }

    }

    public class EddystoneWrapper{

        public IEddystoneNamespace namespace;
        public IEddystoneDevice device;


        public EddystoneWrapper(IEddystoneNamespace namespace, IEddystoneDevice dev)
        {
            this.namespace = namespace;
            this.device = dev;

        }
    }



}

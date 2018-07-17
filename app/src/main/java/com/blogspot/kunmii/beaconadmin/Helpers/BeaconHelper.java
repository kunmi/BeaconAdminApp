package com.blogspot.kunmii.beaconadmin.Helpers;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.kontakt.sdk.android.BuildConfig;
import com.kontakt.sdk.android.ble.configuration.ScanMode;
import com.kontakt.sdk.android.ble.configuration.ScanPeriod;
import com.kontakt.sdk.android.ble.connection.OnServiceReadyListener;
import com.kontakt.sdk.android.ble.device.EddystoneDevice;
import com.kontakt.sdk.android.ble.exception.ScanError;
import com.kontakt.sdk.android.ble.manager.ProximityManager;
import com.kontakt.sdk.android.ble.manager.ProximityManagerFactory;
import com.kontakt.sdk.android.ble.manager.listeners.EddystoneListener;
import com.kontakt.sdk.android.ble.manager.listeners.IBeaconListener;
import com.kontakt.sdk.android.ble.manager.listeners.ScanStatusListener;
import com.kontakt.sdk.android.ble.manager.listeners.simple.SimpleEddystoneListener;
import com.kontakt.sdk.android.ble.manager.listeners.simple.SimpleIBeaconListener;
import com.kontakt.sdk.android.ble.spec.EddystoneFrameType;
import com.kontakt.sdk.android.common.KontaktSDK;
import com.kontakt.sdk.android.common.log.LogLevel;
import com.kontakt.sdk.android.common.profile.IBeaconDevice;
import com.kontakt.sdk.android.common.profile.IBeaconRegion;
import com.kontakt.sdk.android.common.profile.IEddystoneDevice;
import com.kontakt.sdk.android.common.profile.IEddystoneNamespace;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BeaconHelper {





    private ProximityManager proximityManager;

    MutableLiveData<HashMap<String, IBeaconDevice>> beaconDeviceLiveData = new MutableLiveData<>();
    MutableLiveData<HashMap<String, IEddystoneDevice>> eddystoneDeviceLiveData = new MutableLiveData<>();


    HashMap<String, IBeaconDevice> ibeacons = new HashMap<>();
    HashMap<String, IEddystoneDevice> eddystones = new HashMap<>();



    //ForUpdating - LiveMap
    MutableLiveData<List<IBeaconDevice>> updatedIbeacon = new MutableLiveData<>();
    MutableLiveData<List<IEddystoneDevice>> updatedEddystone = new MutableLiveData<>();

    //Marked for lost
    MutableLiveData<IBeaconDevice> lostIBeacon = new MutableLiveData<>();
    MutableLiveData<IEddystoneDevice> lostEddyBeacon = new MutableLiveData<>();


    static BeaconHelper currentInstance = null;


    public static BeaconHelper getInstance(Application context){

        if(currentInstance == null)
        {
            currentInstance = new BeaconHelper(context);
        }

        return currentInstance;

    }

    private BeaconHelper(Application context) {

        KontaktSDK.initialize("123").setDebugLoggingEnabled(true).setLogLevelEnabled(LogLevel.DEBUG, true);

        proximityManager = ProximityManagerFactory.create(context);


        proximityManager.setIBeaconListener(createIBeaconListener());
        proximityManager.setEddystoneListener(createEddystoneListener());


        proximityManager.configuration()
                .scanMode(ScanMode.BALANCED)
                .scanPeriod(ScanPeriod.create(TimeUnit.SECONDS.toMillis(60), TimeUnit.SECONDS.toMillis(2)))
                .monitoringEnabled(false)
                .eddystoneFrameTypes(EnumSet.of(EddystoneFrameType.UID))
                .deviceUpdateCallbackInterval(TimeUnit.SECONDS.toMillis(3));


        proximityManager.setScanStatusListener(new ScanStatusListener() {
            @Override
            public void onScanStart() {
                Log.d("Kunmi", "onStart" );
            }

            @Override
            public void onScanStop() {
                Log.d("Kunmi", "onStop" );
            }

            @Override
            public void onScanError(ScanError scanError) {
                Log.d("Kunmi", "onScanError" );
            }

            @Override
            public void onMonitoringCycleStart() {
                Log.d("Kunmi", "onMonitoringStart" );
            }

            @Override
            public void onMonitoringCycleStop() {
                Log.d("Kunmi", "onMonitoringStopped" );
            }
        });
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
        currentInstance = null;

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

                ibeacons.put(key, ibeacon);
                beaconDeviceLiveData.setValue(ibeacons);


            }

            @Override
            public void onIBeaconsUpdated(List<IBeaconDevice> ibeacons, IBeaconRegion region) {
                Log.d("","");

                updatedIbeacon.setValue(ibeacons);


                HashMap<String, IBeaconDevice> devices = beaconDeviceLiveData.getValue();

                for(IBeaconDevice device: ibeacons){
                    if(devices.containsKey(device.getAddress()))
                    {
                        devices.put(device.getAddress(), device);
                    }
                }

                beaconDeviceLiveData.setValue(devices);
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

                lostIBeacon.setValue(ibeacon);
            }
        };
    }

    private EddystoneListener createEddystoneListener() {
        return new SimpleEddystoneListener() {
            @Override
            public void onEddystoneDiscovered(IEddystoneDevice eddystone, IEddystoneNamespace namespace) {
                Log.i("Sample", "Eddystone discovered: " + eddystone.toString());

                String key = eddystone.getAddress();

                eddystones.put(key, eddystone);
                eddystoneDeviceLiveData.setValue(eddystones);
            }

            @Override
            public void onEddystonesUpdated(List<IEddystoneDevice> eddystones, IEddystoneNamespace namespace) {
                super.onEddystonesUpdated(eddystones, namespace);

                updatedEddystone.setValue(eddystones);

                HashMap<String, IEddystoneDevice> devices = eddystoneDeviceLiveData.getValue();

                for(IEddystoneDevice device: eddystones){
                    if(devices.containsKey(device.getAddress()))
                    {
                        devices.put(device.getAddress(), device);
                    }
                }

                eddystoneDeviceLiveData.setValue(devices);
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

                lostEddyBeacon.setValue(eddystone);
            }
        };
    }


    public MutableLiveData<HashMap<String, IBeaconDevice>> getIBeaconDeviceLiveData() {
        return beaconDeviceLiveData;
    }

    public MutableLiveData<HashMap<String, IEddystoneDevice>> getEddystoneDeviceLiveData() {
        return eddystoneDeviceLiveData;
    }

    public MutableLiveData<IEddystoneDevice> getLostEddyBeacon() {
        return lostEddyBeacon;
    }

    public MutableLiveData<IBeaconDevice> getLostIBeacon() {
        return lostIBeacon;
    }

    public MutableLiveData<List<IBeaconDevice>> getUpdatedIbeacon() {
        return updatedIbeacon;
    }

    public MutableLiveData<List<IEddystoneDevice>> getUpdatedEddystone() {
        return updatedEddystone;
    }

    public interface BeaconWrapper{}


}

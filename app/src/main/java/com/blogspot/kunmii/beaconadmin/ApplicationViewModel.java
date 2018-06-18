package com.blogspot.kunmii.beaconadmin;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.blogspot.kunmii.beaconadmin.Helpers.BeaconHelper;
import com.blogspot.kunmii.beaconadmin.Helpers.Helpers;
import com.blogspot.kunmii.beaconadmin.Helpers.ISaveBeaconLResultListener;
import com.blogspot.kunmii.beaconadmin.data.Beacon;
import com.blogspot.kunmii.beaconadmin.data.FloorPlan;
import com.blogspot.kunmii.beaconadmin.data.FloorplanWithBeacons;
import com.blogspot.kunmii.beaconadmin.data.Project;
import com.kontakt.sdk.android.ble.manager.listeners.EddystoneListener;
import com.kontakt.sdk.android.ble.manager.listeners.IBeaconListener;
import com.kontakt.sdk.android.common.profile.IBeaconDevice;
import com.kontakt.sdk.android.common.profile.IEddystoneDevice;

import java.util.HashMap;
import java.util.List;

public class ApplicationViewModel extends AndroidViewModel{


    AppRepository repository;
    private LiveData<List<Project>> projects = null;

    BeaconHelper beaconHelper = null;


    public ApplicationViewModel(Application application)
    {
        super(application);
        repository = new AppRepository(application);
        projects = repository.projecDao.getAll();


        new Thread(new Runnable() {
            @Override
            public void run() {
                projects = repository.getAssignedProjects();

            }
        }).start();
    }


    public LiveData<List<Project>> getAllAsignedProjects() {
        return projects;
    }

    public LiveData<List<FloorplanWithBeacons>> getFloorplanForProject(String projectId) {
        return repository.getFloorplanForProject(projectId);
    }

    public LiveData<Project> getProject(String id)
    {
        return repository.projecDao.getProjectById(id);
    }

    public LiveData<FloorplanWithBeacons> getFloorplanWithId(String id) {
        return repository.getFLoorplanWithId(id);
    }


    public LiveData<HashMap<String, BeaconHelper.IBeaconWrapper>> getIbeaconDevices(){
        return getBeaconHelper().getIBeaconDeviceLiveData();
    }

    public LiveData<HashMap<String, BeaconHelper.EddystoneWrapper>> getEddystoneDevices(){
        return getBeaconHelper().getEddystoneDeviceLiveData();
    }


    public LiveData<IEddystoneDevice> getLostEddyBeacon() {
        if(beaconHelper==null)
            beaconHelper = getBeaconHelper();

        return beaconHelper.getLostEddyBeacon();
    }

    public LiveData<IBeaconDevice> getLostIBeacon() {
        if(beaconHelper==null)
            beaconHelper = getBeaconHelper();
        return beaconHelper.getLostIBeacon();
    }

    public LiveData<List<IBeaconDevice>> getUpdatedIbeacon() {
        if(beaconHelper==null)
            beaconHelper = getBeaconHelper();
        return beaconHelper.getUpdatedIbeacon();
    }

    public LiveData<List<IEddystoneDevice>> getUpdatedEddystone() {
        if(beaconHelper==null)
            beaconHelper = getBeaconHelper();
        return beaconHelper.getUpdatedEddystone();
    }

    public void saveBeacons(@Nullable ISaveBeaconLResultListener resultListener, List<Beacon> data, String projectId, String flooarplanId)
    {

        repository.uploadBeacons(
                resultListener,
                data,
                projectId,
                flooarplanId
                );

    }


    public void LogOut()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Helpers.clearALlData(getApplication());
                repository.db.CLEAR_ALL();
            }
        }).start();
    }



    public BeaconHelper getBeaconHelper(){

        if(beaconHelper == null)
        {
            beaconHelper = new BeaconHelper(getApplication());
        }

        beaconHelper.onStart();

        return beaconHelper;
    }



    public void updateBeacon(Beacon beacon, ISaveBeaconLResultListener resultListener){
        repository.updateBeacon(getApplication(), beacon, resultListener);
    }

}

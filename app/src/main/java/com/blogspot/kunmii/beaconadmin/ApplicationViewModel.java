package com.blogspot.kunmii.beaconadmin;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.blogspot.kunmii.beaconadmin.Helpers.BeaconHelper;
import com.blogspot.kunmii.beaconadmin.data.FloorPlan;
import com.blogspot.kunmii.beaconadmin.data.FloorplanWithBeacons;
import com.blogspot.kunmii.beaconadmin.data.Project;
import com.kontakt.sdk.android.ble.manager.listeners.EddystoneListener;
import com.kontakt.sdk.android.ble.manager.listeners.IBeaconListener;

import java.util.HashMap;
import java.util.List;

public class ApplicationViewModel extends AndroidViewModel{


    AppRepository repository;
    private LiveData<List<Project>> projects;

    BeaconHelper beaconHelper = null;


    public ApplicationViewModel(Application application)
    {
        super(application);
        repository = new AppRepository(application);
        projects = repository.getAssignedProjects();
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
        return getBeaconHelper(getApplication()).getIBeaconDeviceLiveData();
    }

    public LiveData<HashMap<String, BeaconHelper.EddystoneWrapper>> getEddystoneDevices(){
        return getBeaconHelper(getApplication()).getEddystoneDeviceLiveData();
    }




    public BeaconHelper getBeaconHelper(Application application){

        if(beaconHelper == null)
        {
            beaconHelper = new BeaconHelper(getApplication());
        }

        beaconHelper.onStart();

        return beaconHelper;
    }

}

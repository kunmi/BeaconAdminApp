package com.blogspot.kunmii.beaconadmin;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.blogspot.kunmii.beaconadmin.data.FloorPlan;
import com.blogspot.kunmii.beaconadmin.data.FloorplanWithBeacons;
import com.blogspot.kunmii.beaconadmin.data.Project;

import java.util.List;

public class ApplicationViewModel extends AndroidViewModel{


    AppRepository repository;
    private LiveData<List<Project>> projects;

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

    public LiveData<List<String>> getBeaconsNearMe() {



        return null;
    }
}

package com.blogspot.kunmii.beaconadmin;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.blogspot.kunmii.beaconadmin.data.BeaconDAO;
import com.blogspot.kunmii.beaconadmin.data.FloorplanDAO;
import com.blogspot.kunmii.beaconadmin.data.Project;
import com.blogspot.kunmii.beaconadmin.data.ProjectDAO;

import java.util.List;

public class AppRepository {

    Application mContext;
    AppDatabase db;

    ProjectDAO projecDao;
    FloorplanDAO floorplanDao;
    BeaconDAO beaconDAO;

    LiveData<List<Project>> projects;

    public AppRepository(Application application)
    {
        mContext = application;
        db = AppDatabase.getInstance(mContext);

        projecDao = db.projectDAO();
        floorplanDao = db.floorplanDAO();
        beaconDAO = db.beaconDAO();

        projects = projecDao.getAll();

    }

    public LiveData<List<Project>> getAssignedProjects(){

        if(projects.getValue().size() == 0)
        {

        }
        return projects;
    }
}

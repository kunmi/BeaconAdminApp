package com.blogspot.kunmii.beaconadmin;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.blogspot.kunmii.beaconadmin.data.Beacon;
import com.blogspot.kunmii.beaconadmin.data.BeaconDAO;
import com.blogspot.kunmii.beaconadmin.data.FloorPlan;
import com.blogspot.kunmii.beaconadmin.data.FloorplanDAO;
import com.blogspot.kunmii.beaconadmin.data.Project;
import com.blogspot.kunmii.beaconadmin.data.ProjectDAO;

@Database(entities = {Project.class, Beacon.class, FloorPlan.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase{

    public static final String DATABASE_NAME = "beaconadmindb";

    private static AppDatabase mInstance;


    public abstract BeaconDAO beaconDAO();
    public abstract ProjectDAO projectDAO();
    public abstract FloorplanDAO floorplanDAO();

    public static AppDatabase getInstance(Context context)
    {
        if(mInstance == null)
        {
            mInstance = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return mInstance;
    }



}

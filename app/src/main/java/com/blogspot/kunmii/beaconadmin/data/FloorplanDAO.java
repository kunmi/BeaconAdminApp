package com.blogspot.kunmii.beaconadmin.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.List;

@Dao
public interface FloorplanDAO {


    @Query("SELECT * FROM floorplan WHERE projectid = :projectId")
    LiveData<List<FloorPlan>> getFloorPlanFromProjectWithProjectId(String projectId);


    @Transaction
    @Query("SELECT * FROM floorplan WHERE projectid = :projectId")
    public LiveData<List<FloorplanWithBeacons>> loadFloorPlansWithBeaconsForProjects(String projectId);

    @Transaction
    @Query("SELECT * FROM floorplan  WHERE objectid = :floorplanId LIMIT 1")
    public  LiveData<FloorplanWithBeacons> getFloorplansBeacons(String floorplanId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FloorPlan floorPlan);

    @Insert
    void insertAll(FloorPlan... users);

    @Delete
    void delete(FloorPlan user);




}

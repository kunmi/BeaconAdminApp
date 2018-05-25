package com.blogspot.kunmii.beaconadmin.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface FloorplanDAO {


    @Query("SELECT * FROM floorplan WHERE projectid = :projectId")
    List<FloorPlan> getFloorPlanFromProjectWithProjectId(String projectId);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FloorPlan floorPlan);

    @Insert
    void insertAll(FloorPlan... users);

    @Delete
    void delete(FloorPlan user);


}

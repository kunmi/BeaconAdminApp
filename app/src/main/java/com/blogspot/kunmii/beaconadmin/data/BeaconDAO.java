package com.blogspot.kunmii.beaconadmin.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface BeaconDAO {


    @Query("SELECT * FROM beacon WHERE projectid = :projectId AND floorplanid = :floorplanId")
    List<Beacon> getBeaconFromProjectWithProjectId(String projectId, String floorplanId);




    @Query("UPDATE beacon SET beacon = :beaconData, type = :type WHERE projectid = :projectId AND floorplanid = :floorplanId")
    int UpdateBeacon(String projectId, String floorplanId, String type,String beaconData);



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBeacon(Beacon beacon);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Beacon... beacons);

    @Delete
    void delete(Beacon beacon);

    @Query("DELETE FROM beacon WHERE projectid = :projectId AND floorplanid = :floorplanId")
    void nukeAll(String projectId, String floorplanId);

    @Query("DELETE FROM  beacon")
    void nukeAll();


}

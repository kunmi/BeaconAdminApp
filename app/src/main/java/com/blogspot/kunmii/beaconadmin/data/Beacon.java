package com.blogspot.kunmii.beaconadmin.data;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "beacon")
public class Beacon {

    @PrimaryKey
    int id;

    @ColumnInfo(name  = "objectid")
    String objectId;

    @ColumnInfo(name  = "floorplanid")
    String floorPlanId;

    @ColumnInfo(name = "projectid")
    String projectId;

    @ColumnInfo(name = "type")
    String type;

    @ColumnInfo(name  = "beacon")
    String beaconData;

    public String getBeaconData() {
        return beaconData;
    }
}

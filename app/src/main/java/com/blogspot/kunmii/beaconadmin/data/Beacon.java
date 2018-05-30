package com.blogspot.kunmii.beaconadmin.data;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.blogspot.kunmii.beaconadmin.Helpers.DateConverter;

import java.util.Date;

@Entity(tableName = "beacon")
public class Beacon {

    @PrimaryKey(autoGenerate = true)
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

    @TypeConverters(DateConverter.class)
    @ColumnInfo(name  = "updated")
    Date updated;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getFloorPlanId() {
        return floorPlanId;
    }

    public void setFloorPlanId(String floorPlanId) {
        this.floorPlanId = floorPlanId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBeaconData() {
        return beaconData;
    }

    public void setBeaconData(String beaconData) {
        this.beaconData = beaconData;
    }
}

package com.blogspot.kunmii.beaconadmin.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "floorplan")
public class FloorPlan {

    @PrimaryKey
    int id;

    @ColumnInfo(name  = "objectid")
    String objectId;

    @ColumnInfo(name  = "projectid")
    String projectObjectId;

    @ColumnInfo(name  = "name")
    String name;

    @ColumnInfo(name  = "url")
    String fileurl;

    @ColumnInfo(name  = "updated")
    Date updated;




}

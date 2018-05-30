package com.blogspot.kunmii.beaconadmin.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.blogspot.kunmii.beaconadmin.Helpers.DateConverter;

import java.util.Date;

@Entity(tableName = "floorplan")
public class FloorPlan {

    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name  = "objectid")
    String objectId;

    @ColumnInfo(name  = "projectid")
    String projectObjectId;

    @ColumnInfo(name  = "name")
    String name;

    @ColumnInfo(name  = "url")
    String fileurl;

    @TypeConverters(DateConverter.class)
    @ColumnInfo(name  = "updated")
    Date updated;




}

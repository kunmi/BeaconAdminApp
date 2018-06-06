package com.blogspot.kunmii.beaconadmin.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.blogspot.kunmii.beaconadmin.AppDatabase;
import com.blogspot.kunmii.beaconadmin.Helpers.DateConverter;

import java.util.Date;
import java.util.List;

@Entity(tableName = "floorplan")
public class FloorPlan {

    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name  = "objectid")
    String objectId;

    @ForeignKey(entity = Project.class, parentColumns = "objectid", childColumns = "projectid")
    @ColumnInfo(name  = "projectid")
    String projectObjectId;

    @ColumnInfo(name  = "name")
    String name;

    @ColumnInfo(name  = "url")
    String fileurl;

    @ColumnInfo(name = "updated")
    String updated;


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

    public String getProjectObjectId() {
        return projectObjectId;
    }

    public void setProjectObjectId(String projectObjectId) {
        this.projectObjectId = projectObjectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileurl() {
        return fileurl;
    }

    public void setFileurl(String fileurl) {
        this.fileurl = fileurl;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

}

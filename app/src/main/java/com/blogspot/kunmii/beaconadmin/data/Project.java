package com.blogspot.kunmii.beaconadmin.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "projects")
public class Project {

 @PrimaryKey
 private int id;

 @ColumnInfo(name  = "objectid")
 private String objectId;

 @ColumnInfo(name  = "name")
 private String name;

 @ColumnInfo(name  = "email")
 private String email;

 @ColumnInfo(name  = "description")
 private String description;

 @ColumnInfo(name  = "updated")
 private Date updated;


}

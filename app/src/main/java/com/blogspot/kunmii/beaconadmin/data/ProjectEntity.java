package com.blogspot.kunmii.beaconadmin.data;

import android.arch.persistence.room.Entity;

import java.util.Date;

@Entity(tableName = "projects")
public class ProjectEntity {

 private int id;
 private String objectId;
 private String name;
 private String email;
 private String description;
 private Date updated;


}

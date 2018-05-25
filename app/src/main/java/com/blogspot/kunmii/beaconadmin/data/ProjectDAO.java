package com.blogspot.kunmii.beaconadmin.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ProjectDAO {


    @Query("SELECT * FROM projects")
    List<Project> getAll();

    @Query("SELECT * FROM projects WHERE objectid = :proId")
    Project getProjectById(String proId);

    @Insert
    void insertAll(Project... projects);

    @Delete
    void delete(Project project);

}

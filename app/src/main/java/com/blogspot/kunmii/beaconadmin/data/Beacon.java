package com.blogspot.kunmii.beaconadmin.data;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.PointF;
import android.support.annotation.NonNull;


@Entity(tableName = "beacon")
public class Beacon {


    @NonNull
    @PrimaryKey()
    @ColumnInfo(name  = "objectid")
    String objectId;

    @ForeignKey(entity = FloorPlan.class, parentColumns = "objectid", childColumns = "floorplanid")
    @ColumnInfo(name  = "floorplanid")
    String floorPlanId;

    @ForeignKey(entity = Project.class, parentColumns = "objectid", childColumns = "projectid")
    @ColumnInfo(name = "projectid")
    String projectId;

    @ColumnInfo(name = "type")
    String type;

    @ColumnInfo(name = "x")
    double x;

    @ColumnInfo(name = "y")
    double y;

    @ColumnInfo(name = "ref")
    String ref;

    @ColumnInfo(name = "txpower")
    String txpower;

    @ColumnInfo(name  = "beacon")
    String beaconData;

    @ColumnInfo(name = "updated")
    String updated;


    @Ignore
    public Proximity proximity = Proximity.OUT_OF_RANGE;



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

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getType() {
        return type;
    }


    public void setX(double x) {
        this.x = x;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getTxpower() {
        return txpower;
    }

    public void setTxpower(String txpower) {
        this.txpower = txpower;
    }

    public String getBeaconData() {
        return beaconData;
    }

    public void setBeaconData(String beaconData) {
        this.beaconData = beaconData;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }




    public boolean isIbeacon(){
        if(type.equals("iBeacon"))
            return true;
        else
            return false;
    }

    @Ignore
    //Image is stored as percentage relative to the image size.
    public PointF getCoordsAsPixel(int width, int height){
        return new PointF((float) (x/100)*width, (float) (y/100) * height);
    }

    public enum Proximity{

        // 0 - 0.5m
        IMMEDIATE,

        // 0.5 - 3m
        NEAR,

        // 3m
        FAR,

        // ??m
        OUT_OF_RANGE

    }

}

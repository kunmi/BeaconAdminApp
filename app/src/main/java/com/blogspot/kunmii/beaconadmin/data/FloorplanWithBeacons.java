package com.blogspot.kunmii.beaconadmin.data;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

public class FloorplanWithBeacons {
    @Embedded
    FloorPlan floorPlan;

    @Relation(parentColumn = "objectid", entityColumn = "floorplanid", entity = Beacon.class)
    List<Beacon> beacons;

    public FloorPlan getFloorPlan() {
        return floorPlan;
    }

    public List<Beacon> getBeacons() {
        return beacons;
    }
}

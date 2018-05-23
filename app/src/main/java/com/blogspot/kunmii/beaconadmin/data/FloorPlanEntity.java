package com.blogspot.kunmii.beaconadmin.data;

public class FloorPlanEntity {

    int id;
    /*
    name : {
        type : String,
        default : "Unnamed"
    },
    filename : {
        type: String,
        required : true
    },
    created : Date,
    uploadedBy:
    {
            type: mongoose.Schema.Types.ObjectId,
            ref : User
    },
    size : String,
    mimeType: String,
    path: String,
    beacons: [BeaconPlanSchema],
    areas: [ContentAreaSchema]
     */

    String name;
    String fileurl;

    String projectObjectId;



}

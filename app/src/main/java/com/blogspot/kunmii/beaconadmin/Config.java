package com.blogspot.kunmii.beaconadmin;

public class Config {


    public static float ICON_HEIGHT = 50;
    public static float ICON_WIDTH = 50;


    public static final String SERVER = "http://192.168.0.100:3000/";

//    public static final String SERVER = "http://10.0.2.2:3000/";
    public static final String SERVER_URL = SERVER+"api/";
    static final String LOGIN_URL = "authenticate";
    static final String GET_PROJECT_URL = "projects";


    public static final String USER_TOKEN = "user_token";


    public static final String NAME = "name_user";
    public static final String USERNAME = "username";
    public static final String USER_EMAIL = "user_email";
    public static final String IS_ADMIN = "is_admin";



    public static String getLoginUrl() {
        return LOGIN_URL;
    }

    public static String getGetProjectUrl() {
        return GET_PROJECT_URL;
    }

    public static String generateImageUrl(String path){
        return SERVER + path;
    }


    public interface NETWORK_JSON_NODE{
           String OBJECT_ID = "_id";
           String UPDATED = "updated";
           String CREATED = "created";

        String PROJECT_NAME = "name";
        String PROJECT_EMAIL = "email";
        String PROJECT_DESCRIPTION = "description";
        String PROJECT_FLOORPLANS = "floorPlans";

        String FLOORPLAN_NAME = "name";
        String FLOORPLAN_URL = "url";
        String FLOORPLAN_BEACONS = "beacons";

        String BEACON_TYPE = "type";
        String BEACON_REF = "ref";
        String BEACON_TXPOWER = "txPower";
        String BEACON_MAP = "map";
        String BEACON_MAP_X = "x";
        String BEACON__MAP_Y = "y";

        String IBEACON_UUID = "uuid";
        String IBEACON_MAJOR = "major";
        String IBEACON_MINOR = "minor";


        String EDDY_NAMESPACEID = "nameSpaceId";
        String EDDY_INSTANCEID = "instanceId";
        String EDDY_TELEMETRY = "telemetry";





    }

}

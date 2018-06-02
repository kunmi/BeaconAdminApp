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
        public static final String OBJECT_ID = "_id";
        public static final String UPDATED = "updated";
        public static final String CREATED = "created";

        public static final String PROJECT_NAME = "name";
        public static final String PROJECT_EMAIL = "email";
        public static final String PROJECT_DESCRIPTION = "description";
        public static final String PROJECT_FLOORPLANS = "floorPlans";

        public static final String FLOORPLAN_NAME = "name";
        public static final String FLOORPLAN_URL = "url";
        public static final String FLOORPLAN_BEACONS = "beacons";

        public static final String BEACON_TYPE = "type";
        public static final String BEACON_REF = "ref";
        public static final String BEACON_TXPOWER = "txPower";
        public static final String BEACON_MAP = "map";
        public static final String BEACON_MAP_X = "x";
        public static final String BEACON__MAP_Y = "y";


    }

}

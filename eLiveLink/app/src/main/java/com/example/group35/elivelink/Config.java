package com.example.group35.elivelink;

/**
 * Created by Haram on 2016-05-17.
 */
public final class Config {

    private Config() {
    }

    public static final String YOUTUBE_API_KEY = "AIzaSyB426dqWxl3tYB7RfdUBtHF5JMNR2q-WSs";

    public static final String DEFAULT_VIDEO = "WVP3fUzQHcg";

    //Database connection values
    public static final String DB_USER_CONTROL_URL = "http://frontlineventure.com/db_control/user_control.php";
    public static final String DB_BROADCAST_CONTROL_URL = "http://frontlineventure.com/db_control/broadcast_control.php";
    public static final String DB_QUERY_PASSWORD = "9e3d1f6e3b75eda9922844ca8b0d88b3";
    public static final String DB_QUERY_TYPE_USER_LOGIN = "login";
    public static final String DB_QUERY_TYPE_GET_BROADCASTS = "getBroadcasts";
    public static final String DB_QUERY_TYPE_CREATE_BROADCAST = "createBroadcast";
    public static final String DB_QUERY_TYPE_UPDATE_BROADCAST = "updateBroadcast";
    public static final String DB_QUERY_TYPE_TOGGLE_BROADCAST = "toggleBroadcast";
    public static final String DB_QUERY_TYPE_DELETE_BROADCAST = "deleteBroadcast";
    public static final String DB_QUERY_TYPE_GET_BROADCASTS_WITH_USERID = "getUserBroadcasts"; //for broadcasters

}

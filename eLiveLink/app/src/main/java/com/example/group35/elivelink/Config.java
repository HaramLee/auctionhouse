package com.example.group35.elivelink;

/**
 * Created by Haram on 2016-05-17.
 */
public final class Config {

    private Config() {
    }

    public static final String YOUTUBE_API_KEY = "AIzaSyB426dqWxl3tYB7RfdUBtHF5JMNR2q-WSs";

    public static final String DEFAULT_VIDEO = "WVP3fUzQHcg";
    public static final String DEFAULT_NEWS = "There are no news at this time.";

    //Database connection values
    public static final String DB_USER_CONTROL_URL = "http://frontlineventure.com/db_control/user_control.php";
    public static final String DB_BROADCAST_CONTROL_URL = "http://frontlineventure.com/db_control/broadcast_control.php";
    public static final String DB_BILLING_CONTROL_URL = "http://frontlineventure.com/db_control/billing_control.php";
    public static final String DB_DEFAULT_CONTROL_URL = "http://frontlineventure.com/db_control/default_control.php";

    //Must sync with the php files
    public static final String DB_QUERY_PASSWORD = "9e3d1f6e3b75eda9922844ca8b0d88b3";
    public static final String DB_QUERY_TYPE_USER_LOGIN = "login";
    public static final String DB_QUERY_TYPE_GET_BROADCASTS = "getBroadcasts";
    public static final String DB_QUERY_TYPE_CREATE_BROADCAST = "createBroadcast";
    public static final String DB_QUERY_TYPE_UPDATE_BROADCAST = "updateBroadcast";
    public static final String DB_QUERY_TYPE_TOGGLE_BROADCAST = "toggleBroadcast";
    public static final String DB_QUERY_TYPE_DELETE_BROADCAST = "deleteBroadcast";
    public static final String DB_QUERY_TYPE_GET_BROADCASTS_WITH_USERID = "getUserBroadcasts"; //for broadcasters
    public static final String DB_QUERY_TYPE_GET_BILLING = "getPaymentDetails";
    public static final String DB_QUERY_TYPE_SET_BILLING = "setPaymentDetails";
    public static final String DB_QUERY_TYPE_GET_USER_DETAILS = "getUserDetails";
    public static final String DB_QUERY_TYPE_GET_DEFAULTS = "getDefaults";

}

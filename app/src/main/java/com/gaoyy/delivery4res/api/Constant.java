package com.gaoyy.delivery4res.api;

public class Constant
{
    public static final String TAG="Sam Delivery";
    public static final String API_BASE = "http://www.menualliance.com/jeeplus25/";
//    public static final String API_BASE = "http://47.89.184.140/order/";
//    public static final String API_BASE = "http://192.168.1.49:8080/order/";

    public static final String GOOGLE_MAP_API_BASE = "https://maps.googleapis.com/maps/api/";
    public static final String GOOGLE_MAP_KEY = "AIzaSyAFhhAGX_9cW87jrdz06uDo96iweddwBl4";

    public static final String DOWNLOAD_RES_APK_URL = "http://47.89.184.140/order/static/delivery/delivery4Res.apk";
    public static final String DOWNLOAD_DRIVER_APK_URL = "http://47.89.184.140/order/static/delivery/delivery4Driver.apk";
    public static final String RES_APK_NAME = "delivery4Res.apk";
    public static final String DRIVER_APK_NAME = "delivery4Driver.apk";

    public static final String BUGLY_APP_ID = "7bdfe7827e";


    public static final int MSG_TO_ACT_ORDER_LIST = 101;
    public static final int MSG_TO_ACT_NEW_ORDER = 102;
    public static final int REQUEST_ACCESS_COARSE_LOCATION = 103;
    public static final int REQUEST_BLUETOOTH_ON = 104;


    //下拉刷新标识
    public static final int PULL_TO_REFRESH = 400;
    //上拉加载更多标识
    public static final int UP_TO_LOAD_MORE = 500;



    public static final int CANCLE_AFTER_DELIVERY= 105;
    public static final int DELIVERY= 106;
    public static final int RESUBMIT= 107;
    public static final int CANCLE= 108;
    public static final int MAKING_FINISH= 109;

}
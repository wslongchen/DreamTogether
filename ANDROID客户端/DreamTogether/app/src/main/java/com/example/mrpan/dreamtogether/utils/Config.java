package com.example.mrpan.dreamtogether.utils;

import android.os.Environment;

/**
 * Created by mrpan on 15/12/6.
 */
public class Config {

    /**
     * 是否只在wifi 下使用 的key
     *
     * values 1 表示 仅在wifi下使用 values 0 表示 都可以使用
     */
    public static String TYPE_CONN = "TYPE_CONN";
    public static int TYPE_ALL = 0;
    public static int TYPE_WIFI = 1;
    public static String DIR_CACHE_PATH = Environment.getExternalStorageDirectory()
            .toString()  +"/Dream/Cache/";
    public static String DIR_IMAGE_PATH = Environment.getExternalStorageDirectory()
            .toString() + "/Dream/Image/";
    public static String DIR_DATABASE_PATH=Environment.getExternalStorageDirectory()
            .toString() +"Dream/DataBase/";

    public static final int TAKE_PICTURE = 0x000000;

    public static final int DATA_SHOW=0012;
    public static final int NET_ERROR=0013;
    public static final int SHOW_NEXT = 0011;
    public static final int CACHE_DATA_SHOW=0014;
    public static final int SHOW_AD=0015;
    public static final int FEEDKEY_START =0016;
    public static final int CLEAR_CACHE=0017;

    public static final int HTTP_REQUEST_SUCCESS=1001;
    public static final int HTTP_REQUEST_ERROR=1002;


    public static final int RESULT_RET_SUCCESS=0;
    public static final int RESULT_RET_FAILD=1;

    public static final String REQUEST_ALL_DREAM="http://dream.mrpann.com/index.php?action=list&type=dream";
    public static final String REQUEST_ALL_USER="http://dream.mrpann.com/index.php?action=list&type=user";
    public static final String CREATE_DREAM="http://dream.mrpann.com/index.php?action=post&type=dream";
    public static final String CREATE_USER="http://dream.mrpann.com/index.php?action=post&type=user";
    private static final String LOGIN_Str="http://dream.mrpann.com/index.php?action=list&type=loginUser&name=";
    private static final String LOGIN_Str2="&password=";
    public static String Login_User(String name,String pass){
       return LOGIN_Str+name+LOGIN_Str2+pass;
    }
    public static final int ALL_DREAM=1003;


    public static final int REGISTER_TYPE=1010;
    public static final int TIMELINE_TYPE=1011;
    public static final int POST_TYPE=1012;

}

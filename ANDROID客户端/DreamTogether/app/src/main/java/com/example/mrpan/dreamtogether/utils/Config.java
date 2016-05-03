package com.example.mrpan.dreamtogether.utils;

import android.os.Environment;

import com.example.mrpan.dreamtogether.R;

/**
 * Created by mrpan on 15/12/6.
 */
public class Config {

    /**
     * 是否只在wifi 下使用 的key
     *
     * values 1 表示 仅在wifi下使用 values 0 表示 都可以使用
     */

    public static boolean IS_DEBUG=true;

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

    public static final String REQUEST_ALL_DREAM="http://dream.mrpann.com/index.php?action=list&type=dream&page=1";
    public static final String REQUEST_ALL_USER="http://dream.mrpann.com/index.php?action=list&type=user";
    public static final String CREATE_DREAM="http://dream.mrpann.com/index.php?action=post&type=dream";//"http://dream.mrpann.com/allpost.php?action=postDreamImg"
    public static final String CREATE_USER="http://dream.mrpann.com/index.php?action=post&type=user";
    public static final String CREATE_DREAM_WITH_IMG="http://dream.mrpann.com/allpost.php?action=postDreamImg";
    public static final String CREATE_COMMENT="http://dream.mrpann.com/index.php?action=comment&type=postComment";
    private static final String LOGIN_Str="http://dream.mrpann.com/index.php?action=list&type=loginUser&name=";
    private static final String LOGIN_Str2="&password=";
    private static final String REQUEST_ALL_AUTHOR_DREAM="http://dream.mrpann.com/index.php?action=list&type=getDreamByAuthor&id=";

    private static final String REQUEST_USE_WITH_ID="http://dream.mrpann.com/index.php?action=list&type=getUserByID&id=";
    public static final String REQUEST_RADOM_DREAM="http://dream.mrpann.com/index.php?action=list&type=getRandomDream";
    private static final String REQUEST_COMMENT="http://dream.mrpann.com/index.php?action=comment&type=getComment&id=";
    public static String GetCommentByID(int id){
        return REQUEST_COMMENT+id;
    }


    public static String GetUserByID(int id){
        return REQUEST_USE_WITH_ID+id;
    }

    public static String GetNextDream(int size){
        int recentPage=size%10==0?size/10:size/10+1;
        int page=recentPage+1;
        return "http://dream.mrpann.com/index.php?action=list&type=dream&page="+page;
    }

    public static String GetDreamByAuthor(int id){
        return REQUEST_ALL_AUTHOR_DREAM+id;
    }

    public static String Login_User(String name,String pass){
       return LOGIN_Str+name+LOGIN_Str2+pass;
    }
    public static final int ALL_DREAM=1003;


    public static final int REGISTER_TYPE=1010;
    public static final int TIMELINE_TYPE=1011;
    public static final int POST_TYPE=1012;
    public static final int PHOTO_TYPE=1013;
    public static final int XIUXIU_TYPE=1014;
    public static final int DREAM_DETAILS_TYPE=1015;


    public static final int REQUEST_CODE_PERMISSIONS=123;

    public static final String KEY = "image";

    public static int [] images = {R.mipmap.bg_search,R.mipmap.refresh2,R.mipmap.refresh3,R.mipmap.refresh4};



}

package com.example.mrpan.dreamtogether.utils;

import android.os.Environment;

import com.example.mrpan.dreamtogether.R;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

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

    public static final String APP_ID="wx01301b546bb77b67";//微信
    public static final String APP_KEY="";
    public static final String PARTNER_ID="";
    public static final String PARTNER_KEY = "8934e7d15453e97507ef794cf7b0519d";

    public static final String APP_SECRET="20feef8cc7a101a908ec45700893cf21";

    //微博
    public static final String SINA_APP_KEY = "3292895821"; // 应用的APP_KEY
    public static final String SINA_APP_SECRET="ceb1fcbe5d0862c9e55d65ba731c0cb3";
    public static final String SINA_REDIRECT_URL = "http://www.sina.com";// 应用的回调页
     public static final String SINA_SCOPE = "email,direct_messages_read,direct_messages_write,"
        + "friendships_groups_read,friendships_groups_write,statuses_to_me_read," + "follow_app_official_microblog," + "invitation_write";// 应用申请的高级权限
    public static Oauth2AccessToken SINA_TOKEN=null;


    public static String TYPE_CONN = "TYPE_CONN";
    public static int TYPE_ALL = 0;
    public static int TYPE_WIFI = 1;
    public static String DIR_CACHE_PATH = Environment.getExternalStorageDirectory()
            .toString()  +"/Dream/Cache/";
    public static String DIR_IMAGE_PATH = Environment.getExternalStorageDirectory()
            .toString() + "/Dream/Image/";
    public static String DIR_DATABASE_PATH=Environment.getExternalStorageDirectory()
            .toString() +"/Dream/DataBase/";
    public final static String SAVE_SOUND_PATH = Environment.getExternalStorageDirectory()
            .toString() + "/Dream/Sounds/";

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

    public static final String GET_WEATHER(String city){
        return "http://wthrcdn.etouch.cn/weather_mini?city="+city;
    }

    public static final String REQUEST_ALL_DREAM="http://dream.mrpann.com/index.php?action=list&type=dream&page=1";
    public static final String REQUEST_ALL_USER="http://dream.mrpann.com/index.php?action=list&type=user";
    public static final String CREATE_DREAM="http://dream.mrpann.com/index.php?action=post&type=dream";//"http://dream.mrpann.com/allpost.php?action=postDreamImg"
    public static final String CREATE_USER="http://dream.mrpann.com/index.php?action=post&type=user";
    public static final String CREATE_DREAM_WITH_IMG="http://dream.mrpann.com/allpost.php?action=postDreamImg";
    public static final String UPDATE_USER_IMG="http://dream.mrpann.com/allpost.php?action=updateUserImg";
    public static final String UPDATE_SIGN="http://dream.mrpann.com/index.php?action=update&type=sign";
    public static final String CREATE_COMMENT="http://dream.mrpann.com/index.php?action=comment&type=postComment";
    public static final String DELETE_DREAM="http://dream.mrpann.com/index.php?action=delete&type=dream&id=";
    private static final String LOGIN_Str="http://dream.mrpann.com/index.php?action=list&type=loginUser&name=";
    private static final String LOGIN_Str2="&password=";
    private static final String REQUEST_ALL_AUTHOR_DREAM="http://dream.mrpann.com/index.php?action=list&type=getDreamByAuthor&id=";

    public static final String QUERY_IS_HAVE_USER="http://dream.mrpann.com/index.php?action=post&type=isHaveUser";

    private static final String REQUEST_USE_WITH_ID="http://dream.mrpann.com/index.php?action=list&type=getUserByID&id=";
    public static final String REQUEST_RADOM_DREAM="http://dream.mrpann.com/index.php?action=list&type=getRandomDream";
    private static final String REQUEST_COMMENT="http://dream.mrpann.com/index.php?action=comment&type=getComment&id=";
    public static String GetCommentByID(int id){
        return REQUEST_COMMENT+id;
    }
    public static String DeleteDreamByID(int id){
        return DELETE_DREAM+id;
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
    public static final int BROWSER_TYPE=1016;
    public static final int MESSAGE_TYPE=1017;
    public static final int EDIT_TYPE=1018;
    public static final int SHARE_TYPE=1019;
    public static final int DREAM_MAPS=1020;
    public static final int DREAM_QRCODE=1021;


    public static final int SHARE=1019;


    public static final int REQUEST_CODE_PERMISSIONS=123;

    public static final String KEY = "image";

    public static int [] images = {R.mipmap.bg_search,R.mipmap.refresh2,R.mipmap.refresh3,R.mipmap.refresh4};


    public static final String XMPP_HOST = "114.215.113.48";
    public static final String XMPP_HOSTNAME="iz28dbl1lqiz";
    public static final int XMPP_PORT = 5222;
    /**
     * 登录状态广播
     */
    public static final String ACTION_IS_LOGIN_SUCCESS = "com.android.qq.is_login_success";
    /**
     *好友在线状态更新广播
     */
    public static final String ACTION_FRIENDS_ONLINE_STATUS_CHANGE= "com.android.qq.friends_online_status_change";
    /**
     * 消息记录操作广播
     */
    public static final String ACTION_MSG_OPER= "com.android.qq.msgoper";
    /**
     * 新消息广播
     */
    public static final String ACTION_NEW_MSG= "com.android.qq.newmsg";
    /**
     * 添加好友请求广播
     */
    public static final String ACTION_ADDFRIEND= "com.android.qq.addfriend";
    public static final String MSG_TYPE_ADD_FRIEND="msg_type_add_friend";//添加好友
    public static final String MSG_TYPE_ADD_FRIEND_SUCCESS="msg_type_add_friend_success";//同意添加好友

    public static final int NOTIFY_ID=0x90;
    public static final String MSG_TYPE_TEXT="msg_type_text";//文本消息
    public static final String MSG_TYPE_IMG="msg_type_img";//图片
    public static final String MSG_TYPE_VOICE="msg_type_voice";//语音
    public static final String MSG_TYPE_LOCATION="msg_type_location";//位置
    public static final String SPLIT="卍";


    //音乐进度条广播

    public static final String ACTION_PROGRESSBAR="com.android.communication.progressbar";
    public static final String ACTION_CHAGE_MUSIC="com.android.communication.chagemusic";

    public static final int PLAY_MSG = 1;                      //开始播放
    public static final int PAUSE = 2;                         //暂停播放
    public static final int PREVIOUS_MUSIC = 3;                //上一首
    public static final int NEXT_MUSIC = 4;                    //下一首
    public static final int LOOP_MODE = 5;                     //循环播放
    public static final int RANDOM_MODE = 6;                   //随机播放




}

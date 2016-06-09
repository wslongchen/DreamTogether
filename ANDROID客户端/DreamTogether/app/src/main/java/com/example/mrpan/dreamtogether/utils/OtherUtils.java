package com.example.mrpan.dreamtogether.utils;

import android.content.Context;
import android.content.Entity;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Build;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.NumberPicker;

import com.example.mrpan.dreamtogether.R;
import com.example.mrpan.dreamtogether.entity.Comment;
import com.example.mrpan.dreamtogether.entity.Dream;
import com.example.mrpan.dreamtogether.entity.Meta;
import com.example.mrpan.dreamtogether.entity.User;
import com.google.zxing.WriterException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mrpan on 16/3/16.
 */
public class OtherUtils {
    //转换实体与NameValuePair
    public static List<NameValuePair> UserToNameValuePair(User user,String methodName) {
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("name", user.getUser_login()));
        nameValuePairs.add(new BasicNameValuePair("pass", user.getUser_pass()));
        nameValuePairs.add(new BasicNameValuePair("nickname", user.getUser_nickname()));
        nameValuePairs.add(new BasicNameValuePair("img", user.getUser_img()));
        nameValuePairs.add(new BasicNameValuePair("phone", user.getUser_phone()));
        nameValuePairs.add(new BasicNameValuePair("email", user.getUser_email()));
        nameValuePairs.add(new BasicNameValuePair("url", user.getUser_url()));
        nameValuePairs.add(new BasicNameValuePair("registeredate", user.getUser_registered()));
        nameValuePairs.add(new BasicNameValuePair("activationkey", user.getUser_activation_key()));
        nameValuePairs.add(new BasicNameValuePair("status", user.getUser_status()));
        nameValuePairs.add(new BasicNameValuePair("displayname", user.getUser_display_name()));
        long TimeStamp = System.currentTimeMillis();
        String appkey = "dream,.*-app" + TimeStamp + methodName.toLowerCase();;
        String sign = Md5Utils.StrToMd5(appkey);
        nameValuePairs.add(new BasicNameValuePair("signkey", sign));
        nameValuePairs.add(new BasicNameValuePair("timestamp", String.valueOf(TimeStamp)));
        return nameValuePairs;
    }

    public static List<NameValuePair> DreamToNameValuePair(Dream dream,String methodName) {
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        int ID = dream.getPost_author().getID();
        nameValuePairs.add(new BasicNameValuePair("author", String.valueOf(ID)));
        nameValuePairs.add(new BasicNameValuePair("date", dream.getPost_date()));
        nameValuePairs.add(new BasicNameValuePair("content", dream.getPost_content()));
        nameValuePairs.add(new BasicNameValuePair("title", dream.getPost_titile()));
        nameValuePairs.add(new BasicNameValuePair("status", dream.getPost_status()));
        nameValuePairs.add(new BasicNameValuePair("password", dream.getPost_password()));
        nameValuePairs.add(new BasicNameValuePair("guid", dream.getPost_guid()));
        nameValuePairs.add(new BasicNameValuePair("type", dream.getPost_type()));
        nameValuePairs.add(new BasicNameValuePair("commentstatus", dream.getPost_comment_status()));
        nameValuePairs.add(new BasicNameValuePair("commentcount", dream.getPost_comment_count()));
        long TimeStamp = System.currentTimeMillis();
        String appkey = "dream,.*-app" + TimeStamp + methodName.toLowerCase();;
        String sign = Md5Utils.StrToMd5(appkey);
        nameValuePairs.add(new BasicNameValuePair("signkey", sign));
        nameValuePairs.add(new BasicNameValuePair("timestamp", String.valueOf(TimeStamp)));
        List<Meta> metas=dream.getMetas();
        if(metas.size()>0){
            String jsonStr= GsonUtils.getJsonStr(metas);
            nameValuePairs.add(new BasicNameValuePair("metas", jsonStr));
        }
        return nameValuePairs;
    }

    public static Map<String, String> DreamToMap(Dream dream,String methodName) {
        Map<String, String> map = new HashMap<>();
        int ID = dream.getPost_author().getID();
        map.put("author", String.valueOf(ID));
        map.put("date", dream.getPost_date().toString());
        map.put("content", dream.getPost_content().toString());
        map.put("title", dream.getPost_titile().toString());
        map.put("status", dream.getPost_status().toString());
        map.put("password", dream.getPost_password().toString());
        map.put("guid", dream.getPost_guid().toString());
        map.put("type", dream.getPost_type().toString());
        map.put("commentstatus", dream.getPost_comment_status().toString());
        map.put("commentcount", dream.getPost_comment_count().toString());
        long TimeStamp = System.currentTimeMillis();
        String appkey = "dream,.*-app" + TimeStamp + methodName.toLowerCase();;
        String sign = Md5Utils.StrToMd5(appkey);
        map.put("signkey", sign);
        map.put("timestamp", String.valueOf(TimeStamp));

        List<Meta> metas=dream.getMetas();
        if(metas.size()>0){
            String jsonStr= GsonUtils.getJsonStr(metas);
            map.put("metas", jsonStr);
        }
        return map;
    }

    public static List<NameValuePair> CommentToMap(Comment comment,String methodName) {
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        int ID = comment.getComment_user_id().getID();
        nameValuePairs.add(new BasicNameValuePair("author", String.valueOf(ID)));
        nameValuePairs.add(new BasicNameValuePair("userid", String.valueOf(ID)));
        nameValuePairs.add(new BasicNameValuePair("dreamid", comment.getPost_id()));
        nameValuePairs.add(new BasicNameValuePair("content", comment.getComment_content()));
        nameValuePairs.add(new BasicNameValuePair("time", comment.getComment_time()));
        long TimeStamp = System.currentTimeMillis();
        String appkey = "dream,.*-app" + TimeStamp + methodName.toLowerCase();;
        String sign = Md5Utils.StrToMd5(appkey);
        nameValuePairs.add(new BasicNameValuePair("signkey", sign));
        nameValuePairs.add(new BasicNameValuePair("timestamp", String.valueOf(TimeStamp)));
        return nameValuePairs;
    }
    public static List<NameValuePair> signToNameValuePair(String signed,int id,String methodName) {
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("sign", signed));
        nameValuePairs.add(new BasicNameValuePair("id", String.valueOf(id)));
        long TimeStamp = System.currentTimeMillis();
        String appkey = "dream,.*-app" + TimeStamp + methodName.toLowerCase();;
        String sign = Md5Utils.StrToMd5(appkey);
        nameValuePairs.add(new BasicNameValuePair("signkey", sign));
        nameValuePairs.add(new BasicNameValuePair("timestamp", String.valueOf(TimeStamp)));
        return nameValuePairs;
    }

    public static Map<String, String> StringToMap(int id) {
        Map<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(id));
        return map;
    }
    public static String getDeviceInfo() {
        return Build.BRAND;
    }


    public static String addSignValidate(String url,String methodName){
        long TimeStamp = System.currentTimeMillis();
        String appkey = "dream,.*-app" + TimeStamp + methodName.toLowerCase();;
        String sign = Md5Utils.StrToMd5(appkey);
        String get_url=url+"&timestamp="+TimeStamp+"&signkey="+sign;
        return get_url;
    }

    public static int revertWeatherToImg(String weather){
        int ResId=0;
        switch (weather){
            case "晴":
                ResId= R.mipmap.weather_sunny;
                break;
            case "多云":
                ResId=R.mipmap.weather_cloudy;
                break;
            case "中雨":
                ResId=R.mipmap.weather_moderate_rain;
                break;
            case "小雨":
                ResId=R.mipmap.weather_light_rain;
                break;
            case "阴":
                ResId=R.mipmap.weather_overcast;
                break;
            case "雷阵雨":
                ResId=R.mipmap.weather_thundershower;
                break;
//            case "雷阵雨":
//                ResId=R.mipmap.weather_thundershower;
//                break;
//            case "雷阵雨":
//                ResId=R.mipmap.weather_thundershower;
//                break;
//            case "雷阵雨":
//                ResId=R.mipmap.weather_thundershower;
//                break;
            default:
                ResId=R.mipmap.weather_unknown;
                break;
        }
        return ResId;
    }

    //设置二维码
    public static void calculateView(final Context context,final ImageView qrcode,final String content) {
        final ViewTreeObserver vto = qrcode.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                if (vto.isAlive()) {
                    vto.removeOnPreDrawListener(this);
                }
                int height = qrcode.getMeasuredHeight();
                int width = qrcode.getMeasuredWidth();
                Bitmap logo = MakeQRCodeUtils.gainBitmap(context, R.mipmap.ic_launcher);
                Bitmap   background = MakeQRCodeUtils.gainBitmap(context, R.mipmap.bg_search);
                Bitmap markBMP = MakeQRCodeUtils.gainBitmap(context, R.mipmap.app_icon);
                try {
                    //获得二维码图片
                    Bitmap bitmap = MakeQRCodeUtils.makeQRImage(logo,
                            content,
                            width, height);
                    //给二维码加背景
                    bitmap = MakeQRCodeUtils.addBackground(bitmap, background);
                    //加水印
                    bitmap = MakeQRCodeUtils.composeWatermark(bitmap, markBMP);
                    //设置二维码图片
                    qrcode.setImageBitmap(bitmap);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
    }

}

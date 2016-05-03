package com.example.mrpan.dreamtogether.utils;

import android.content.Entity;
import android.widget.NumberPicker;

import com.example.mrpan.dreamtogether.entity.Comment;
import com.example.mrpan.dreamtogether.entity.Dream;
import com.example.mrpan.dreamtogether.entity.User;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

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
    public static List<NameValuePair> UserToNameValuePair(User user){
        List<NameValuePair> nameValuePairs=new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("name",user.getUser_login()));
        nameValuePairs.add(new BasicNameValuePair("pass",user.getUser_pass()));
        nameValuePairs.add(new BasicNameValuePair("nickname",user.getUser_nickname()));
        nameValuePairs.add(new BasicNameValuePair("img",user.getUser_img()));
        nameValuePairs.add(new BasicNameValuePair("phone",user.getUser_phone()));
        nameValuePairs.add(new BasicNameValuePair("email",user.getUser_email()));
        nameValuePairs.add(new BasicNameValuePair("url",user.getUser_url()));
        nameValuePairs.add(new BasicNameValuePair("registeredate",user.getUser_registered()));
        nameValuePairs.add(new BasicNameValuePair("activationkey", user.getUser_activation_key()));
        nameValuePairs.add(new BasicNameValuePair("status", user.getUser_status()));
        nameValuePairs.add(new BasicNameValuePair("displayname",user.getUser_display_name()));
        return nameValuePairs;
    }

    public static List<NameValuePair> DreamToNameValuePair(Dream dream){
        List<NameValuePair> nameValuePairs=new ArrayList<>();
        int ID=dream.getPost_author().getID();
        nameValuePairs.add(new BasicNameValuePair("author",String.valueOf(ID)));
        nameValuePairs.add(new BasicNameValuePair("date",dream.getPost_date()));
        nameValuePairs.add(new BasicNameValuePair("content",dream.getPost_content()));
        nameValuePairs.add(new BasicNameValuePair("title",dream.getPost_titile()));
        nameValuePairs.add(new BasicNameValuePair("status",dream.getPost_status()));
        nameValuePairs.add(new BasicNameValuePair("password",dream.getPost_password()));
        nameValuePairs.add(new BasicNameValuePair("guid",dream.getPost_guid()));
        nameValuePairs.add(new BasicNameValuePair("type", dream.getPost_type()));
        nameValuePairs.add(new BasicNameValuePair("commentstatus",dream.getPost_comment_status()));
        nameValuePairs.add(new BasicNameValuePair("commentcount",dream.getPost_comment_count()));
        return nameValuePairs;
    }

    public static Map<String,String> DreamToMap(Dream dream){
        Map<String,String> map=new HashMap<>();
        int ID=dream.getPost_author().getID();
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
        return map;
    }

    public static List<NameValuePair> CommentToMap(Comment comment){
        List<NameValuePair> nameValuePairs=new ArrayList<>();
        int ID=comment.getComment_user_id().getID();
        nameValuePairs.add(new BasicNameValuePair("author",String.valueOf(ID)));
        nameValuePairs.add(new BasicNameValuePair("userid", String.valueOf(ID)));
        nameValuePairs.add(new BasicNameValuePair("dreamid", comment.getPost_id()));
        nameValuePairs.add(new BasicNameValuePair("content", comment.getComment_detail()));
        nameValuePairs.add(new BasicNameValuePair("time", comment.getComment_time()));
        return nameValuePairs;
    }

}

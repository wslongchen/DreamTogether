package com.example.mrpan.dreamtogether.utils;

import android.content.Entity;
import android.widget.NumberPicker;

import com.example.mrpan.dreamtogether.entity.Dream;
import com.example.mrpan.dreamtogether.entity.User;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.List;

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
        nameValuePairs.add(new BasicNameValuePair("author",dream.getWordcircle_author()));
        nameValuePairs.add(new BasicNameValuePair("date",dream.getWordcircle_date()));
        nameValuePairs.add(new BasicNameValuePair("content",dream.getWordcircle_content()));
        nameValuePairs.add(new BasicNameValuePair("title",dream.getWordcircle_titile()));
        nameValuePairs.add(new BasicNameValuePair("status",dream.getWordcircle_status()));
        nameValuePairs.add(new BasicNameValuePair("password",dream.getWordcircle_password()));
        nameValuePairs.add(new BasicNameValuePair("guid",dream.getWordcircle_guid()));
        nameValuePairs.add(new BasicNameValuePair("type", dream.getWordcircle_type()));
        nameValuePairs.add(new BasicNameValuePair("commentstatus",dream.getWordcircle_comment_status()));
        nameValuePairs.add(new BasicNameValuePair("commentcount",dream.getWordcircle_comment_count()));
        return nameValuePairs;
    }

}

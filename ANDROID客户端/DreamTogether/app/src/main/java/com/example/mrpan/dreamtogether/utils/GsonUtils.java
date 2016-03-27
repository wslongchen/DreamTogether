package com.example.mrpan.dreamtogether.utils;

import android.text.TextUtils;

import com.example.mrpan.dreamtogether.entity.BaseEntity;
import com.google.gson.Gson;

/**
 * Created by mrpan on 15/12/7.
 */
public class GsonUtils {

    public static BaseEntity getEntity(String content, Class<?> clazz){

        if(TextUtils.isEmpty(content))
            return null;

        Gson gson = new Gson();

        try {
            BaseEntity baseEntity = (BaseEntity) gson.fromJson(content.replace("\uFEFF\uFEFF\uFEFF",""), clazz);
            return baseEntity;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}

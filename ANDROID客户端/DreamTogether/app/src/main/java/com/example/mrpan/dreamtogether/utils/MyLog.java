package com.example.mrpan.dreamtogether.utils;

import android.util.Log;

/**
 * Created by mrpan on 15/12/7.
 */
public class MyLog {
    private static final String TAG = "Ann";

    public static void i(String tag, String msg) {

        Log.i(TAG, "[" + tag + "]" + msg);
    }

}

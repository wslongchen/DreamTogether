package com.example.mrpan.dreamtogether.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import com.example.mrpan.dreamtogether.utils.SpringSystem;
import com.facebook.rebound.BaseSpringSystem;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.nineoldandroids.view.ViewHelper;

/**
 * Created by mrpan on 16/5/23.
 */
public class StarViewPager extends ViewPager{


    public StarViewPager(Context context) {
        super(context);
    }

    public StarViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}

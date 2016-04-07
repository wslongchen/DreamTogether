package com.example.mrpan.dreamtogether.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mrpan.dreamtogether.R;

/**
 * Created by mrpan on 16/3/23.
 */
@SuppressLint("ResourceAsColor")
public class TitleBar extends RelativeLayout {

    private View titleBarView;
    private LayoutInflater layoutInflater;
    private ImageView leftImage;
    private ImageView rightImage;
    private TextView centerTitle;
    private TextView rightStr;
    private TextView leftStr;
    private RelativeLayout allView;

    public TitleBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        InitTitleBarView(context);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        InitTitleBarView(context);
    }

    public TitleBar(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        InitTitleBarView(context);
    }

    public void InitTitleBarView(Context context){
        layoutInflater=LayoutInflater.from(context);
        titleBarView=layoutInflater.inflate(R.layout.top_bar, this);
        leftImage=(ImageView)titleBarView.findViewById(R.id.titleBarLeftImage);
        rightImage=(ImageView)titleBarView.findViewById(R.id.titleBarRightImage);
        centerTitle=(TextView)titleBarView.findViewById(R.id.title);
        allView=(RelativeLayout)titleBarView.findViewById(R.id.titleBarView);
        rightStr=(TextView)titleBarView.findViewById(R.id.titleBarRightStr);
        leftStr=(TextView)titleBarView.findViewById(R.id.titleBarLeftStr);
    }

    /**
     * 显示左边图片
     * @param title
     * @param leftImages
     * @param onclick
     */
    public void showLeft(String title,Drawable leftImages,OnClickListener onclick){
        centerTitle.setText(title);
        centerTitle.setVisibility(View.VISIBLE);
        leftImage.setImageDrawable(leftImages);
        leftImage.setVisibility(View.VISIBLE);
        leftImage.setOnClickListener(onclick);
    }

    /**
     * 显示右边图片
     * @param title
     * @param rightImages
     * @param onclick
     */
    public void showRight(String title,int rightImages,OnClickListener onclick){
        centerTitle.setText(title);
        centerTitle.setVisibility(View.VISIBLE);
        rightImage.setImageResource(rightImages);
        rightImage.setVisibility(View.VISIBLE);
        rightImage.setOnClickListener(onclick);
    }

    /**
     * 左右都显示
     * @param title
     * @param leftImages
     * @param rightImages
     * @param leftClicki
     * @param rightClick
     */
    public void showLeftAndRight(String title,Drawable leftImages,Drawable rightImages,OnClickListener leftClicki,OnClickListener rightClick){
        centerTitle.setText(title);
        centerTitle.setVisibility(View.VISIBLE);
        leftImage.setImageDrawable(leftImages);
        leftImage.setVisibility(View.VISIBLE);
        leftImage.setOnClickListener(leftClicki);
        rightImage.setImageDrawable(rightImages);
        rightImage.setVisibility(View.VISIBLE);
        rightImage.setOnClickListener(rightClick);
    }

    /**
     * 左边图片+右边文字
     * @param title
     * @param rightStrs
     * @param leftImages
     * @param leftClick
     * @param rightClick
     */
    public void showLeftImageAndRightStr(String title,String rightStrs,Drawable leftImages,OnClickListener leftClick,OnClickListener rightClick){
        centerTitle.setVisibility(View.VISIBLE);
        centerTitle.setText(title);
        leftImage.setImageDrawable(leftImages);
        leftImage.setVisibility(View.VISIBLE);
        rightStr.setText(rightStrs);
        rightStr.setVisibility(View.VISIBLE);
        leftImage.setOnClickListener(leftClick);
        rightStr.setOnClickListener(rightClick);
        rightStr.setTextSize(16);
    }


    /**
     * 左边文字+右边文字
     * @param title
     * @param leftStrs
     * @param rightStrs
     * @param leftClick
     * @param rightClick
     */
    public void showLeftStrAndRightStr(String title,String leftStrs,String rightStrs,OnClickListener leftClick,OnClickListener rightClick){
        centerTitle.setVisibility(View.VISIBLE);
        centerTitle.setText(title);
        leftStr.setText(leftStrs);
        leftStr.setVisibility(View.VISIBLE);
        rightStr.setText(rightStrs);
        rightStr.setVisibility(View.VISIBLE);
        leftStr.setOnClickListener(leftClick);
        rightStr.setOnClickListener(rightClick);
        rightStr.setTextSize(16);
        leftStr.setTextSize(16);
    }


    /**
     * 设置背景颜色
     * @param color
     */
    public void setBgColor(int color){
        allView.setBackgroundColor(color);

    }



    /**
     * 只显示标题
     * @param title
     */
    public void showCenterTitle(String title){
        centerTitle.setText(title);
        centerTitle.setVisibility(View.VISIBLE);
    }


    public void updateRightStr(String str){
        if(rightStr.getVisibility()==VISIBLE)
            rightStr.setText(str);
    }

    public void setRightStrEnable(boolean value){
        if(rightStr.getVisibility()==VISIBLE)
            rightStr.setEnabled(value);
    }

}


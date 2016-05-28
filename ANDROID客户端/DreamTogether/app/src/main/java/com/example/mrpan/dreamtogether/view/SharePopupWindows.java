package com.example.mrpan.dreamtogether.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.example.mrpan.dreamtogether.R;
import com.example.mrpan.dreamtogether.fragment.DreamPostFragment;

/**
 * Created by mrpan on 16/5/28.
 */
public class SharePopupWindows extends PopupWindow implements View.OnClickListener{

    private Context mContext;

    public SharePopupWindows(Context mContext, View parent,int type) {

        final View view = View
                .inflate(mContext, R.layout.share_items, null);
        view.startAnimation(AnimationUtils.loadAnimation(mContext,
                R.anim.fade_ins));
        LinearLayout ll_popup = (LinearLayout) view
                .findViewById(R.id.share_popup);
        ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
                R.anim.push_bottom_in_2));
        //setOutsideTouchable(true);
        this.mContext=mContext;
        setContentView(view);
        init(view);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setBackgroundDrawable(new BitmapDrawable());
        setFocusable(true);
        showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        backgroundAlpha(0.7f);
        update();

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });

    }

    private void init(View view){
        LinearLayout share_qq = (LinearLayout) view
                .findViewById(R.id.share_qq);
        share_qq.setOnClickListener(this);
        LinearLayout share_zone = (LinearLayout) view
                .findViewById(R.id.share_zone);
        share_zone.setOnClickListener(this);
        LinearLayout share_wechat = (LinearLayout) view
                .findViewById(R.id.share_wechat);
        share_wechat.setOnClickListener(this);
        LinearLayout share_wechatmoments = (LinearLayout) view
                .findViewById(R.id.share_wechatmoments);
        share_wechatmoments.setOnClickListener(this);
        LinearLayout share_sina = (LinearLayout) view
                .findViewById(R.id.share_sina);
        share_sina.setOnClickListener(this);
        LinearLayout share_douban = (LinearLayout) view
                .findViewById(R.id.share_douban);
        share_douban.setOnClickListener(this);
        LinearLayout share_alipay = (LinearLayout) view
                .findViewById(R.id.share_alipay);
        share_alipay.setOnClickListener(this);
        LinearLayout share_email = (LinearLayout) view
                .findViewById(R.id.share_email);
        share_email.setOnClickListener(this);
        LinearLayout share_googleplus = (LinearLayout) view
                .findViewById(R.id.share_googleplus);
        share_googleplus.setOnClickListener(this);


    }

    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = ((Activity)mContext).getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        ((Activity)mContext).getWindow().setAttributes(lp);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.share_qq:

                break;
            case R.id.share_zone:
                break;
            case R.id.share_wechat:
                break;
            case R.id.share_wechatmoments:
                break;
            case R.id.share_sina:
                break;
            case R.id.share_douban:
                break;
            case R.id.share_alipay:
                break;
            case R.id.share_email:
                break;
            case R.id.share_googleplus:
                break;
            default:
                break;
        }
    }
}

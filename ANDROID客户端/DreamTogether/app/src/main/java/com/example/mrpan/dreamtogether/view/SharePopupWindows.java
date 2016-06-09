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
import android.widget.Toast;

import com.example.mrpan.dreamtogether.R;
import com.example.mrpan.dreamtogether.entity.Share;
import com.example.mrpan.dreamtogether.fragment.DreamPostFragment;
import com.example.mrpan.dreamtogether.share.AllShare;

/**
 * Created by mrpan on 16/5/28.
 */
public class SharePopupWindows extends PopupWindow implements View.OnClickListener{

    private Context mContext;
    private int type;
    private Share share;
    //0 TEXT 1 IMAGE 2 MUSIC 3 VIDEO 4 VOICE 5 webpage 6 app
    public SharePopupWindows(Context mContext, View parent,int type,Share share) {

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
        this.type=type;
        this.share=share;
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
        dismiss();
        switch (v.getId()){
            case R.id.share_qq:
                switch (type){
                    case 0:
                        AllShare.getInstance(mContext).qq_share_simple(share);
                        break;
                    case 1:
                        AllShare.getInstance(mContext).qq_share_image(share);
                        break;
                    case 2:
                        AllShare.getInstance(mContext).qq_share_music(share);
                        break;
                    case 3:
                    case 4:
                    case 5:
                        AllShare.getInstance(mContext).qq_share_simple(share);
                        break;
                    case 6:
                        AllShare.getInstance(mContext).qq_share_app(share);
                        break;
                    default:
                        break;
                }
                break;
            case R.id.share_zone:
                AllShare.getInstance(mContext).qzone_share_image_text(share);
                break;
            case R.id.share_wechat:
                switch (type){
                    case 0:
                    case 6:
                        AllShare.getInstance(mContext).wechat_share_text(share,false);
                        break;
                    case 1:
                        AllShare.getInstance(mContext).wechat_share_image(share,false);
                        break;
                    case 2:
                        AllShare.getInstance(mContext).wechat_share_music(share,false);
                        break;
                    case 3:
                        AllShare.getInstance(mContext).wechat_share_video(share,false);
                        break;
                    case 4:
                        Toast.makeText(mContext,"敬请期待",Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                        AllShare.getInstance(mContext).wechat_share_webpage(share,false);
                        break;
                    default:
                        break;
                }
                break;
            case R.id.share_wechatmoments:
                switch (type){
                    case 0:
                    case 6:
                        AllShare.getInstance(mContext).wechat_share_text(share,true);
                        break;
                    case 1:
                        AllShare.getInstance(mContext).wechat_share_image(share, true);
                        break;
                    case 2:
                        AllShare.getInstance(mContext).wechat_share_music(share, true);
                        break;
                    case 3:
                        AllShare.getInstance(mContext).wechat_share_video(share, true);
                        break;
                    case 4:
                        Toast.makeText(mContext,"敬请期待",Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                        AllShare.getInstance(mContext).wechat_share_webpage(share,true);
                        break;
                    default:
                        break;
                }
                break;
            case R.id.share_sina:
                switch (type){
                    case 0:
                    case 6:
                        AllShare.getInstance(mContext).sina_share_text(share);
                        break;
                    case 1:
                        AllShare.getInstance(mContext).sina_share_image(share);
                        break;
                    case 2:
                        AllShare.getInstance(mContext).sina_share_music(share);
                        break;
                    case 3:
                        AllShare.getInstance(mContext).sina_share_video(share);
                        break;
                    case 4:
                        AllShare.getInstance(mContext).sina_share_voice(share);
                        break;
                    case 5:
                        AllShare.getInstance(mContext).sina_share_webpage(share);
                        break;
                    default:
                        break;
                }
                break;
            case R.id.share_douban:
                Toast.makeText(mContext,"敬请期待",Toast.LENGTH_SHORT).show();
                break;
            case R.id.share_alipay:
                Toast.makeText(mContext,"敬请期待",Toast.LENGTH_SHORT).show();
                break;
            case R.id.share_email:
                Toast.makeText(mContext,"敬请期待",Toast.LENGTH_SHORT).show();
                break;
            case R.id.share_googleplus:
                Toast.makeText(mContext,"敬请期待",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}

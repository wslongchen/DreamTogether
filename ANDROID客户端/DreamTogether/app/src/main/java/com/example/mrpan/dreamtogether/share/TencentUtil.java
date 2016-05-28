package com.example.mrpan.dreamtogether.share;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.example.mrpan.dreamtogether.entity.Share;
import com.tencent.connect.UserInfo;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.Tencent;

import java.util.ArrayList;

/**
 * Created by mrpan on 16/5/23.
 */
public class TencentUtil {
    private Tencent mTencent;
    public final static String APP_ID = "1105418176";
    public final static String SCOPE = "all";
    private static String OPENID = "";
    private static String PFKEY = "";
    private static String ACCESS_TOKEN = "";
    private Context mContext;
    private UserInfo mInfo;
    private Activity activity;

    public TencentUtil(Activity activity, Context context) {
        this.mContext = context;
        this.activity = activity;
        mTencent = Tencent.createInstance(APP_ID, context);
        mInfo = new UserInfo(mContext, mTencent.getQQToken());
    }

    public void ShareApp(Share share) {
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,
                QQShare.SHARE_TO_QQ_TYPE_APP);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, share.getTITLE());
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, share.getSUMMARY());
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, share.getTARGET_URL());
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, share.getIMAGE_URL());
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, share.getAPP_NAME());
        // params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, "其他附加功能");
        mTencent.shareToQQ(activity, params, new BaseUiListener(0));
    }

    public void shareImage(Share share){
        Bundle params = new Bundle();
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL,share.getIMAGE_URL());
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, share.getAPP_NAME());
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        mTencent.shareToQQ(activity, params, new BaseUiListener(0));
    }

    public void shareQzone(Share share) {
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,
                QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, share.getTITLE());
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, share.getSUMMARY());
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL,
                share.getTARGET_URL());
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL,
                new ArrayList<String>());
        mTencent.shareToQzone(activity, params, new BaseUiListener(0));
    }

    public void ShareSimple(Share share) {
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,
                QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, share.getTITLE());
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, share.getSUMMARY());
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, share.getTARGET_URL());
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, share.getIMAGE_URL());
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, share.getAPP_NAME());
        // params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, "其他附加功能");
        mTencent.shareToQQ(activity, params, new BaseUiListener(0));
    }

    public void shareMusic(Share share){
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_AUDIO);
        params.putString(QQShare.SHARE_TO_QQ_TITLE,share.getTITLE() );
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  share.getSUMMARY());
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  share.getTARGET_URL());
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, share.getIMAGE_URL());
        params.putString(QQShare.SHARE_TO_QQ_AUDIO_URL, share.getAUDIO_URL());
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME,  share.getAPP_NAME());
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT,QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        mTencent.shareToQQ(activity, params, new BaseUiListener(0));
    }



}

package com.example.mrpan.dreamtogether.share;

import android.os.Bundle;

import com.example.mrpan.dreamtogether.utils.Config;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;

/**
 * Created by mrpan on 16/5/28.
 */
public class SinaAuthListener implements WeiboAuthListener {

    Oauth2AccessToken mAccessToken;
    @Override
    public void onComplete(Bundle values) {
        mAccessToken = Oauth2AccessToken.parseAccessToken(values); // 从 Bundle 中解析 Token
        if (mAccessToken.isSessionValid()) {
            Config.SINA_TOKEN=mAccessToken; //保存Token

        } else {
        // 当您注册的应用程序签名不正确时,就会收到错误Code,请确保签名正确
            String code = values.getString("code", "");
        }
    }

    @Override
    public void onWeiboException(WeiboException e) {

    }

    @Override
    public void onCancel() {

    }

}

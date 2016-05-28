package com.example.mrpan.dreamtogether.share;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.mrpan.dreamtogether.R;
import com.example.mrpan.dreamtogether.entity.Share;
import com.example.mrpan.dreamtogether.http.HttpHelper;
import com.example.mrpan.dreamtogether.http.HttpResponseCallBack;
import com.example.mrpan.dreamtogether.utils.BitmapUtils;
import com.example.mrpan.dreamtogether.utils.Config;
import com.example.mrpan.dreamtogether.utils.FileUtils;
import com.example.mrpan.dreamtogether.utils.Md5Utils;
import com.example.mrpan.dreamtogether.utils.MyLog;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.tencent.connect.UserInfo;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXEmojiObject;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXMusicObject;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXVideoObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by mrpan on 16/5/28.
 */
public class AllShare {

    private static final String TAG="ALLSHare";

    private static AllShare instance=null;
    private Tencent mTencent;
    public final static String APP_ID = "1105418176";
    public final static String SCOPE = "all";
    private static String OPENID = "";
    private static String PFKEY = "";
    private static String ACCESS_TOKEN = "";
    private Context mContext;
    private UserInfo mInfo;
    private long timeStamp;
    private String nonceStr, packageValue;

    private IWXAPI iwxapi;

    private static enum LocalRetCode {
        ERR_OK, ERR_HTTP, ERR_JSON, ERR_OTHER
    }


    public static synchronized AllShare getInstance(Context context) {
        if(instance==null){
            instance=new AllShare(context);
        }
        return instance;
    }

    public AllShare(Context context){
        this.mContext=context;
        mTencent = Tencent.createInstance(APP_ID, context);
        mInfo = new UserInfo(mContext, mTencent.getQQToken());
        iwxapi = WXAPIFactory.createWXAPI(context, Config.APP_ID, false);
        iwxapi.registerApp(Config.APP_ID);
        //mAuthInfo = new AuthInfo(context, Config.SINA_APP_KEY, Config.SINA_REDIRECT_URL,Config.SINA_SCOPE);
    }

    public void qq_share_simple(Share share){
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,
                QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, share.getTITLE());
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, share.getSUMMARY());
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, share.getTARGET_URL());
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, share.getIMAGE_URL());
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, share.getAPP_NAME());
        // params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, "其他附加功能");
        mTencent.shareToQQ((Activity) mContext, params, new BaseUiListener(Config.SHARE));
    }

    public void qq_share_image(Share share){
        Bundle params = new Bundle();
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL,share.getIMAGE_URL());
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, share.getAPP_NAME());
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        mTencent.shareToQQ((Activity)mContext, params, new BaseUiListener(Config.SHARE));
    }

    public void qq_share_music(Share share){
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_AUDIO);
        params.putString(QQShare.SHARE_TO_QQ_TITLE,share.getTITLE() );
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, share.getSUMMARY());
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  share.getTARGET_URL());
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, share.getIMAGE_URL());
        params.putString(QQShare.SHARE_TO_QQ_AUDIO_URL, share.getAUDIO_URL());
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, share.getAPP_NAME());
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        mTencent.shareToQQ((Activity)mContext, params, new BaseUiListener(Config.SHARE));
    }

    public void qq_share_app(Share share){
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,
                QQShare.SHARE_TO_QQ_TYPE_APP);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, share.getTITLE());
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, share.getSUMMARY());
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, share.getTARGET_URL());
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, share.getIMAGE_URL());
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, share.getAPP_NAME());
        // params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, "其他附加功能");
        mTencent.shareToQQ((Activity) mContext, params, new BaseUiListener(Config.SHARE));
    }

    public void qzone_share_image_text(Share share) {
        final Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, share.getTITLE());
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, share.getSUMMARY());
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, share.getTARGET_URL());
        ArrayList<String> arrayList=new ArrayList<>();
        arrayList.add(share.getIMAGE_URL());
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, arrayList);
        mTencent.shareToQzone((Activity) mContext, params, new BaseUiListener(Config.SHARE));
    }

    public void wechat_share_text(Share share,boolean type){
        WXTextObject textObject=new WXTextObject();
        textObject.text=share.getSUMMARY();

        WXMediaMessage mediaMessage=new WXMediaMessage();
        mediaMessage.mediaObject=textObject;
        mediaMessage.description=share.getSUMMARY();

        SendMessageToWX.Req req=new SendMessageToWX.Req();
        req.transaction=buildTransaction("text");
        req.message = mediaMessage;
        req.scene = type ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        iwxapi.sendReq(req);
    }

    public void wechat_share_emojI(Share share,boolean type){
        WXEmojiObject emoji = new WXEmojiObject();
        emoji.emojiPath = share.getIMAGE_URL();

        WXMediaMessage msg = new WXMediaMessage(emoji);
        msg.title = share.getTITLE();
        msg.description = share.getSUMMARY();
        msg.thumbData = FileUtils.readFromFile(share.getIMAGE_URL(), 0, (int) new File(share.getIMAGE_URL()).length());


        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("emoji");
        req.message = msg;
        req.scene = type ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        iwxapi.sendReq(req);
    }

    public void wechat_share_emojiII(Share share,boolean type){
        WXEmojiObject emoji = new WXEmojiObject();
        emoji.emojiData = FileUtils.readFromFile(share.getIMAGE_URL(), 0, (int) new File(share.getIMAGE_URL()).length());
        WXMediaMessage msg = new WXMediaMessage(emoji);

        msg.title = share.getTITLE();
        msg.description = share.getSUMMARY();
        msg.thumbData = FileUtils.readFromFile(share.getIMAGE_URL(), 0, (int) new File(share.getIMAGE_URL()).length());

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("emoji");
        req.message = msg;
        req.scene = type ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        iwxapi.sendReq(req);
    }

    public  void wechat_share_image(Share share,boolean type){
        try {
            Bitmap bitmap= BitmapUtils.revitionImageSize(share.getIMAGE_URL());
            WXImageObject imageObject=new WXImageObject(bitmap);
            WXMediaMessage mediaMessage=new WXMediaMessage();
            mediaMessage.mediaObject=imageObject;

            Bitmap thumbBmp=Bitmap.createScaledBitmap(bitmap,150,150,false);
            bitmap.recycle();
            mediaMessage.thumbData=FileUtils.bmpToByteArray(thumbBmp,true);
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = buildTransaction("img");
            req.message = mediaMessage;
            req.scene = type ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
            iwxapi.sendReq(req);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void wechat_share_music(Share share,boolean type) {

        WXMusicObject musicObject = new WXMusicObject();
        musicObject.musicUrl=share.getAUDIO_URL();
        WXMediaMessage mediaMessage = new WXMediaMessage();
        mediaMessage.mediaObject = musicObject;
        mediaMessage.title=share.getTITLE();
        mediaMessage.description=share.getSUMMARY();

        Bitmap thumbBmp = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.send_music_thumb);
        mediaMessage.thumbData=FileUtils.bmpToByteArray(thumbBmp,true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("music");
        req.message = mediaMessage;
        req.scene = type ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        iwxapi.sendReq(req);
    }

    public void wechat_share_video(Share share,boolean type){
        WXVideoObject videoObject=new WXVideoObject();
        videoObject.videoUrl=share.getVIDEO_URL();

        WXMediaMessage mediaMessage=new WXMediaMessage(videoObject);
        mediaMessage.title=share.getTITLE();
        mediaMessage.description=share.getSUMMARY();
        Bitmap thumb=BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.send_music_thumb);
        mediaMessage.thumbData=FileUtils.bmpToByteArray(thumb, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("video");
        req.message = mediaMessage;
        req.scene = type ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        iwxapi.sendReq(req);
    }

    public void wechat_share_webpage(Share share,boolean type){
        WXWebpageObject wxWebpageObject=new WXWebpageObject();
        wxWebpageObject.webpageUrl=share.getTARGET_URL();

        WXMediaMessage mediaMessage=new WXMediaMessage(wxWebpageObject);
        mediaMessage.title=share.getTITLE();
        mediaMessage.description=share.getSUMMARY();
        Bitmap thumb=BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.send_music_thumb);
        mediaMessage.thumbData=FileUtils.bmpToByteArray(thumb, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = mediaMessage;
        req.scene = type ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        iwxapi.sendReq(req);
    }


    public void sina_share_text(Share share){
        Intent intent=new Intent(mContext,SinaActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("data",share);
        bundle.putInt("type",0);
        mContext.startActivity(intent);
    }

    public void sina_share_image(Share share){
        Intent intent=new Intent(mContext,SinaActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("data",share);
        bundle.putInt("type",1);
        mContext.startActivity(intent);
    }
    public void sina_share_webpage(Share share){
        Intent intent=new Intent(mContext,SinaActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("data",share);
        bundle.putInt("type",2);
        mContext.startActivity(intent);
    }

    public void sina_share_music(Share share){
        Intent intent=new Intent(mContext,SinaActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("data",share);
        bundle.putInt("type",3);
        mContext.startActivity(intent);
    }

    public void sina_share_video(Share share){
        Intent intent=new Intent(mContext,SinaActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("data",share);
        bundle.putInt("type",4);
        mContext.startActivity(intent);
    }

    public void sina_share_voice(Share share){
        Intent intent=new Intent(mContext,SinaActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("data",share);
        bundle.putInt("type",5);
        mContext.startActivity(intent);
    }


    public void wechat_pay(){
        boolean isPaySupported = iwxapi.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
        if(isPaySupported){
            new GetAccessTokenTask().execute();
        }
    }


    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    private class GetAccessTokenTask extends AsyncTask<Void, Void, GetAccessTokenResult> {

        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(GetAccessTokenResult result) {
            if (dialog != null) {
                dialog.dismiss();
            }

            if (result.localRetCode == LocalRetCode.ERR_OK) {
                Log.d(TAG, "onPostExecute, accessToken = " + result.accessToken);

                GetPrepayIdTask getPrepayId = new GetPrepayIdTask(result.accessToken);
                getPrepayId.execute();
            } else {
                Toast.makeText(mContext, result.localRetCode.name(), Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected GetAccessTokenResult doInBackground(Void... params) {
            GetAccessTokenResult result = new GetAccessTokenResult();

            String url = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s",
                    Config.APP_ID, Config.APP_SECRET);
            Log.d(TAG, "get access token, url = " + url);

            byte[] buf = HttpHelper.httpGet(url);
            if (buf == null || buf.length == 0) {
                result.localRetCode = LocalRetCode.ERR_HTTP;
                return result;
            }

            String content = new String(buf);
            result.parseFrom(content);
            return result;
        }
    }

    private class GetPrepayIdTask extends AsyncTask<Void, Void, GetPrepayIdResult> {

        private ProgressDialog dialog;
        private String accessToken;

        public GetPrepayIdTask(String accessToken) {
            this.accessToken = accessToken;
        }

        @Override
        protected void onPreExecute() {
            MyLog.i(TAG,"获取PrepayId");
        }

        @Override
        protected void onPostExecute(GetPrepayIdResult result) {
            if (dialog != null) {
                dialog.dismiss();
            }

            if (result.localRetCode == LocalRetCode.ERR_OK) {
                MyLog.i(TAG, "获取PrepayId 成功");
                sendPayReq(result);
            } else {
                MyLog.i(TAG, result.localRetCode.name());
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected GetPrepayIdResult doInBackground(Void... params) {

            String url = String.format("https://api.weixin.qq.com/pay/genprepay?access_token=%s", accessToken);
            String entity = genProductArgs();

            MyLog.i(TAG, "doInBackground, url = " + url);
            MyLog.i(TAG, "doInBackground, entity = " + entity);

            GetPrepayIdResult result = new GetPrepayIdResult();

            byte[] buf = HttpHelper.httpPost(url, entity);
            if (buf == null || buf.length == 0) {
                result.localRetCode = LocalRetCode.ERR_HTTP;
                return result;
            }

            String content = new String(buf);
            MyLog.i(TAG, "doInBackground, content = " + content);
            result.parseFrom(content);
            return result;
        }
    }

    private static class GetAccessTokenResult {

        private static final String TAG = "MicroMsg.SDKSample.GetAccessTokenResult";

        public LocalRetCode localRetCode = LocalRetCode.ERR_OTHER;
        public String accessToken;
        public int expiresIn;
        public int errCode;
        public String errMsg;

        public void parseFrom(String content) {

            if (content == null || content.length() <= 0) {
                MyLog.i(TAG, "parseFrom fail, content is null");
                localRetCode = LocalRetCode.ERR_JSON;
                return;
            }

            try {
                JSONObject json = new JSONObject(content);
                if (json.has("access_token")) { // success case
                    accessToken = json.getString("access_token");
                    expiresIn = json.getInt("expires_in");
                    localRetCode = LocalRetCode.ERR_OK;
                } else {
                    errCode = json.getInt("errcode");
                    errMsg = json.getString("errmsg");
                    localRetCode = LocalRetCode.ERR_JSON;
                }

            } catch (Exception e) {
                localRetCode = LocalRetCode.ERR_JSON;
            }
        }
    }

    private static class GetPrepayIdResult {

        private static final String TAG = "MicroMsg.SDKSample.GetPrepayIdResult";

        public LocalRetCode localRetCode = LocalRetCode.ERR_OTHER;
        public String prepayId;
        public int errCode;
        public String errMsg;

        public void parseFrom(String content) {

            if (content == null || content.length() <= 0) {
                MyLog.i(TAG, "parseFrom fail, content is null");
                localRetCode = LocalRetCode.ERR_JSON;
                return;
            }

            try {
                JSONObject json = new JSONObject(content);
                if (json.has("prepayid")) { // success case
                    prepayId = json.getString("prepayid");
                    localRetCode = LocalRetCode.ERR_OK;
                } else {
                    localRetCode = LocalRetCode.ERR_JSON;
                }

                errCode = json.getInt("errcode");
                errMsg = json.getString("errmsg");

            } catch (Exception e) {
                localRetCode = LocalRetCode.ERR_JSON;
            }
        }
    }

    private String genNonceStr() {
        Random random = new Random();
        return Md5Utils.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    private String getTraceId() {
        return "crestxu_" + genTimeStamp();
    }

    private String genOutTradNo() {
        Random random = new Random();
        return Md5Utils.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }


    private String genSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        int i = 0;
        for (; i < params.size() - 1; i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append(params.get(i).getName());
        sb.append('=');
        sb.append(params.get(i).getValue());

        String sha1 = Md5Utils.sha1(sb.toString());
        Log.d(TAG, "genSign, sha1 = " + sha1);
        return sha1;
    }

    private String genProductArgs() {
        JSONObject json = new JSONObject();

        try {
            json.put("appid", Config.APP_ID);
            String traceId = getTraceId();
            json.put("traceid", traceId);
            nonceStr = genNonceStr();
            json.put("noncestr", nonceStr);

            List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
            packageParams.add(new BasicNameValuePair("bank_type", "WX"));
            packageParams.add(new BasicNameValuePair("body", "dddd"));
            packageParams.add(new BasicNameValuePair("fee_type", "1"));
            packageParams.add(new BasicNameValuePair("input_charset", "UTF-8"));
            packageParams.add(new BasicNameValuePair("notify_url", "http://weixin.qq.com"));
            packageParams.add(new BasicNameValuePair("out_trade_no", genOutTradNo()));
            packageParams.add(new BasicNameValuePair("partner", "1900000109"));
            packageParams.add(new BasicNameValuePair("spbill_create_ip", "196.168.1.1"));
            packageParams.add(new BasicNameValuePair("total_fee", "1"));
            packageValue = genPackage(packageParams);

            json.put("package", packageValue);
            timeStamp = genTimeStamp();
            json.put("timestamp", timeStamp);

            List<NameValuePair> signParams = new LinkedList<NameValuePair>();
            signParams.add(new BasicNameValuePair("appid", Config.APP_ID));
            signParams.add(new BasicNameValuePair("appkey", Config.APP_KEY));
            signParams.add(new BasicNameValuePair("noncestr", nonceStr));
            signParams.add(new BasicNameValuePair("package", packageValue));
            signParams.add(new BasicNameValuePair("timestamp", String.valueOf(timeStamp)));
            signParams.add(new BasicNameValuePair("traceid", traceId));
            json.put("app_signature", genSign(signParams));

            json.put("sign_method", "sha1");
        } catch (Exception e) {
            Log.e(TAG, "genProductArgs fail, ex = " + e.getMessage());
            return null;
        }

        return json.toString();
    }

    private void sendPayReq(GetPrepayIdResult result) {

        PayReq req = new PayReq();
        req.appId = Config.APP_ID;
        req.partnerId = Config.PARTNER_ID;
        req.prepayId = result.prepayId;
        req.nonceStr = nonceStr;
        req.timeStamp = String.valueOf(timeStamp);
        req.packageValue = "Sign=" + packageValue;

        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
        signParams.add(new BasicNameValuePair("appid", req.appId));
        signParams.add(new BasicNameValuePair("appkey", Config.APP_KEY));
        signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
        signParams.add(new BasicNameValuePair("package", req.packageValue));
        signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
        signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
        signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
        req.sign = genSign(signParams);

        iwxapi.sendReq(req);
    }

    private String genPackage(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(Config.PARTNER_KEY);

        String packageSign = Md5Utils.getMessageDigest(sb.toString().getBytes()).toUpperCase();

        return URLEncodedUtils.format(params, "utf-8") + "&sign=" + packageSign;
    }
}

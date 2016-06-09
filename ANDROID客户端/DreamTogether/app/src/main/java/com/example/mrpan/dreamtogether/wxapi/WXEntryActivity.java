package com.example.mrpan.dreamtogether.wxapi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mrpan.dreamtogether.R;
import com.example.mrpan.dreamtogether.utils.Config;
import com.example.mrpan.dreamtogether.utils.MyLog;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.ShowMessageFromWX;
import com.tencent.mm.sdk.modelmsg.WXAppExtendObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

	private IWXAPI api;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		api = WXAPIFactory.createWXAPI(this, Config.APP_ID,false);
		api.registerApp(Config.APP_ID);
		api.handleIntent(getIntent(), this);
		MyLog.i("wechat", "test");
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq baseReq) {
		MyLog.i("wechat","test");
//		Toast.makeText(this, "openid = " + baseReq.openId, Toast.LENGTH_SHORT).show();
//
//		switch (baseReq.getType()) {
//			case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
//				goToGetMsg();
//				break;
//			case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
//				goToShowMsg((ShowMessageFromWX.Req) baseReq);
//				break;
//			case ConstantsAPI.COMMAND_LAUNCH_BY_WX:
//				Toast.makeText(this, "微信", Toast.LENGTH_SHORT).show();
//				break;
//			default:
//				break;
//		}
	}
	private void goToGetMsg() {

	}

	private void goToShowMsg(ShowMessageFromWX.Req showReq) {
		WXMediaMessage wxMsg = showReq.message;
		WXAppExtendObject obj = (WXAppExtendObject) wxMsg.mediaObject;

		StringBuffer msg = new StringBuffer();
		msg.append("description: ");
		msg.append(wxMsg.description);
		msg.append("\n");
		msg.append("extInfo: ");
		msg.append(obj.extInfo);
		msg.append("\n");
		msg.append("filePath: ");
		msg.append(obj.filePath);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(wxMsg.title);
		builder.setMessage(msg.toString());

		if (wxMsg.thumbData != null && wxMsg.thumbData.length > 0) {
			ImageView thumbIv = new ImageView(this);
			thumbIv.setImageBitmap(BitmapFactory.decodeByteArray(wxMsg.thumbData, 0, wxMsg.thumbData.length));
			builder.setView(thumbIv);
		}

		builder.show();
	}
	@Override
	public void onResp(BaseResp baseResp) {
		MyLog.i("wechat","test");
//		Toast.makeText(this, "openid = " + baseResp.openId, Toast.LENGTH_SHORT).show();
//
//		if (baseResp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
//			Toast.makeText(this, "code = " + ((SendAuth.Resp) baseResp).code, Toast.LENGTH_SHORT).show();
//		}
//
//		int result = 0;
//
//		switch (baseResp.errCode) {
//			case BaseResp.ErrCode.ERR_OK:
//				result = R.string.errcode_success;
//				break;
//			case BaseResp.ErrCode.ERR_USER_CANCEL:
//				result = R.string.errcode_cancel;
//				break;
//			case BaseResp.ErrCode.ERR_AUTH_DENIED:
//				result = R.string.errcode_deny;
//				break;
//			default:
//				result = R.string.errcode_unknown;
//				break;
//		}
//
//		Toast.makeText(this, result, Toast.LENGTH_LONG).show();
	}
}
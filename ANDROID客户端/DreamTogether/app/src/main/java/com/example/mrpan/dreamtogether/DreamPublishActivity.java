package com.example.mrpan.dreamtogether;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.mrpan.dreamtogether.entity.Dream;
import com.example.mrpan.dreamtogether.fragment.WorldCircleFragment;
import com.example.mrpan.dreamtogether.http.HttpHelper;
import com.example.mrpan.dreamtogether.http.HttpResponseCallBack;
import com.example.mrpan.dreamtogether.utils.Config;
import com.example.mrpan.dreamtogether.utils.DateUtils;
import com.example.mrpan.dreamtogether.utils.OtherUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mrpan on 16/3/21.
 */
public class DreamPublishActivity extends Activity implements View.OnClickListener{

    private Dream dream=null;//要发表的
    private Context context=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    //初始化数据
    private void init(){
        context=this;
        dream=new Dream();
//        dream.s("");
//        dream.setWordcircle_date(DateUtils.getCurrentTimeStr());
//        dream.setWordcircle_author(null);
//        dream.setWordcircle_content("");
//        dream.setWordcircle_comment_count("0");
//        dream.setWordcircle_comment_status("0");
//        dream.setWordcircle_type("0");
//        dream.setWordcircle_status("0");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case 1:
                HttpHelper httpHelper=HttpHelper.getInstance();
                if(dream!=null) {
                    httpHelper.asyHttpPostRequest(Config.CREATE_DREAM, OtherUtils.DreamToNameValuePair(dream), new HttpResponseCallBack() {
                        @Override
                        public void onSuccess(String url, String result) {
                            try {
                                JSONObject jsonObject = new JSONObject(result.replace("\uFEFF\uFEFF\uFEFF", ""));
                                int ret = jsonObject.getInt("ret");
                                if (ret == Config.RESULT_RET_SUCCESS) {
                                    Intent intent = new Intent(context, WorldCircleFragment.class);
                                    startActivityForResult(intent, Config.RESULT_RET_SUCCESS);
                                    Toast.makeText(context, "Publish successed!", Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int httpResponseCode, int errCode, String err) {

                        }
                    });
                }
                break;
            default:
                break;
        }
    }
}

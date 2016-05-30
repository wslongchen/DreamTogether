package com.example.mrpan.dreamtogether.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mrpan.dreamtogether.R;
import com.example.mrpan.dreamtogether.entity.User;
import com.example.mrpan.dreamtogether.entity.UserPosts;
import com.example.mrpan.dreamtogether.http.HttpHelper;
import com.example.mrpan.dreamtogether.http.HttpResponseCallBack;
import com.example.mrpan.dreamtogether.utils.CacheUtils;
import com.example.mrpan.dreamtogether.utils.Config;
import com.example.mrpan.dreamtogether.utils.GsonUtils;
import com.example.mrpan.dreamtogether.utils.OtherUtils;
import com.example.mrpan.dreamtogether.view.TitleBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by mrpan on 16/5/22.
 */
public class EditFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "Edit";

    private View currentView;

    private TitleBar topbar;

    private Context context;

    private EditText sign;

    private User user;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.edit_fragment, container, false);
        ViewGroup viewGroup = (ViewGroup) currentView.getParent();
        if (viewGroup != null) {
            viewGroup.removeView(currentView);
        }
        context=getActivity();
        initView();
        return currentView;
    }

    private void initView(){
        topbar=(TitleBar)currentView.findViewById(R.id.top_bar);
        topbar.showLeftStrAndRightStr("修改","返回","确定",this,this);
        sign=(EditText)currentView.findViewById(R.id.dream_sign_content);
        user = (User) CacheUtils.readHttpCache(Config.DIR_CACHE_PATH, "user_info");
        if(user!=null){
            sign.setText(user.getUser_display_name());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.titleBarRightStr:
                String text=sign.getText().toString();
                if(!TextUtils.isEmpty(text)){
                    HttpHelper.getInstance().asyHttpPostRequest(Config.UPDATE_SIGN, OtherUtils.signToNameValuePair(text, user.getID(),"updateUserSign"), new MyHttpResponseCallBack(0));
                }else{
                    Toast.makeText(context, "不能为空～", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    class MyHttpResponseCallBack implements HttpResponseCallBack {
        private int flag;

        public MyHttpResponseCallBack(int flag){
            super();
            this.flag=flag;
        }

        @Override
        public void onSuccess(String url, String result) {
            Message message = new Message();
            message.arg1 = Config.HTTP_REQUEST_SUCCESS;
            message.arg2=flag;
            message.obj = result;
            myHander.sendMessage(message);
        }

        @Override
        public void onFailure(int httpResponseCode, int errCode, String err) {
            Message message = new Message();
            message.arg1 = Config.HTTP_REQUEST_ERROR;
            myHander.sendMessage(message);
        }
    }

    Handler myHander=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1){
                case Config.HTTP_REQUEST_SUCCESS:
                    switch (msg.arg2){
                        case 1:
                            if(msg.obj!=null){
                                int ret=0;
                                try {
                                    JSONObject jsonObject = new JSONObject(msg.obj.toString().replace("\uFEFF", ""));
                                    ret = jsonObject.getInt("ret");
                                    if (ret == Config.RESULT_RET_SUCCESS) {
                                        UserPosts userPosts = (UserPosts) GsonUtils.getEntity(msg.obj.toString(), UserPosts.class);
                                        List<User> users = userPosts.getPost();
                                        CacheUtils.saveHttpCache(Config.DIR_CACHE_PATH, "user_info", users.get(0));
                                        getActivity().finish();
                                    } else
                                    {
                                        Toast.makeText(context, "失败！", Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            break;
                        case 0:
                            HttpHelper.getInstance().asyHttpGetRequest(Config.GetUserByID(user.getID()),new MyHttpResponseCallBack(1));
                            break;
                        default:
                            break;
                    }
                    break;
                case Config.HTTP_REQUEST_ERROR:
                    Toast.makeText(context,"联网失败！",Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
}

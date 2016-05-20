package com.example.mrpan.dreamtogether.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.mrpan.dreamtogether.MainActivity;
import com.example.mrpan.dreamtogether.OtherActivity;
import com.example.mrpan.dreamtogether.R;
import com.example.mrpan.dreamtogether.entity.Dream;
import com.example.mrpan.dreamtogether.entity.DreamPosts;
import com.example.mrpan.dreamtogether.entity.User;
import com.example.mrpan.dreamtogether.entity.UserPosts;
import com.example.mrpan.dreamtogether.http.HttpHelper;
import com.example.mrpan.dreamtogether.http.HttpResponseCallBack;
import com.example.mrpan.dreamtogether.utils.CacheUtils;
import com.example.mrpan.dreamtogether.utils.Config;
import com.example.mrpan.dreamtogether.utils.DialogUtils;
import com.example.mrpan.dreamtogether.utils.GsonUtils;
import com.example.mrpan.dreamtogether.utils.Md5Utils;
import com.example.mrpan.dreamtogether.utils.MyLog;
import com.example.mrpan.dreamtogether.utils.MySharePreference;
import com.example.mrpan.dreamtogether.utils.RegexUtils;
import com.example.mrpan.dreamtogether.view.DeletableEditText;
import com.example.mrpan.dreamtogether.view.LXiuXiu;
import com.example.mrpan.dreamtogether.view.TitleBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by mrpan on 16/3/21.
 */
public class DreamerLoginFragment extends Fragment implements View.OnClickListener {

    public static final String TAG="DreamerLogin";

    private View currentView=null;

    private Context context=null;

    private Button loginBtn;

    private DeletableEditText loginName,loginPassword;

    private RelativeLayout newUserRegister;

    private TitleBar titleBar;

    LXiuXiu lXiuXiu1;

    Dialog dialog;

    FragmentTransaction transaction=null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView=inflater.inflate(R.layout.userlogin, container, false);
        ViewGroup viewGroup= (ViewGroup) currentView.getParent();

        if(viewGroup!=null){
            viewGroup.removeView(currentView);
        }
//        lXiuXiu1 = (LXiuXiu) currentView.findViewById(R.id.xiu1);
//        lXiuXiu1.setStep(3);
//                lXiuXiu1.setWaveType(LXiuXiu.WaveType.ALWAYS);
        initView();
        return currentView;
    }

    private void initView(){
        context=getActivity();
        loginName=(DeletableEditText)currentView.findViewById(R.id.login_name);
        loginPassword=(DeletableEditText)currentView.findViewById(R.id.login_password);
        loginBtn=(Button)currentView.findViewById(R.id.login);
        loginBtn.setOnClickListener(this);
        newUserRegister=(RelativeLayout)currentView.findViewById(R.id.newUserRegister);
        newUserRegister.setOnClickListener(this);
        titleBar=(TitleBar)currentView.findViewById(R.id.top_bar);
        titleBar.showCenterTitle("个人中心");
//        titleBar.setBgColor(R.color.dreamBlack);
    }

    @Override
    public void onClick(View v) {
        transaction=getFragmentManager().beginTransaction();
        switch (v.getId()){
            case R.id.login:

                String name=loginName.getText().toString();
                String password=loginPassword.getText().toString();
                if(RegexUtils.checkPassword(name) && RegexUtils.checkPassword(password)) {
                    dialog= DialogUtils.getLoadDialog(getActivity());
                    dialog.show();
                    HttpHelper.getInstance().asyHttpGetRequest(Config.Login_User(name, Md5Utils.StrToMd5(password)), new HttpResponseCallBack() {
                        @Override
                        public void onSuccess(String url, String result) {
                            dialog.dismiss();
                            Message message=new Message();
                            message.arg1=Config.HTTP_REQUEST_SUCCESS;
                            message.obj=result;
                            myHander.sendMessage(message);
                        }

                        @Override
                        public void onFailure(int httpResponseCode, int errCode, String err) {
                            if(dialog.isShowing())
                                dialog.dismiss();
                            Message message=new Message();
                            message.arg1=Config.HTTP_REQUEST_ERROR;
                            myHander.sendMessage(message);
                        }
                    });
                }
                else
                {
                    Toast.makeText(context,"输入项不能为空！",Toast.LENGTH_LONG).show();
                }
//                lXiuXiu1 = (LXiuXiu) currentView.findViewById(R.id.xiu1);
//                lXiuXiu1.setStep(5);
//                lXiuXiu2 = (LXiuXiu) findViewById(R.id.xiu2);
//                lXiuXiu2.setXiuXiuType(LXiuXiu.XiuXiuType.IN);
//                lXiuXiu3 = (LXiuXiu) findViewById(R.id.xiu3);
//                lXiuXiu3.setStep(3);
//                lXiuXiu3.setWaveType(LXiuXiu.WaveType.ALWAYS);
//                lXiuXiu4 = (LXiuXiu) findViewById(R.id.xiu4);
//                lXiuXiu4.setXiuXiuType(LXiuXiu.XiuXiuType.IN);
//                lXiuXiu4.setWaveType(LXiuXiu.WaveType.ALWAYS);
                break;
            case R.id.newUserRegister:
                //transaction.setCustomAnimations(R.anim.left_in,R.anim.left_out,R.anim.right_in,R.anim.right_out);
                //transaction.replace(R.id.frame_content, MainActivity.fragmentHashMap.get(DreamerRegisterFragment.TAG));
                //transaction.addToBackStack(null);
                //transaction.commit();
                Intent intent=new Intent();
                Bundle bundle=new Bundle();
                bundle.putInt("type",Config.REGISTER_TYPE);
                intent.putExtras(bundle);
                intent.setClass(context, OtherActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
                break;
            default:
                break;
        }
    }

    Handler myHander=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1){
                case Config.HTTP_REQUEST_SUCCESS:
                    if(msg.obj!=null) {
                        int ret = 0;
                        try {
                            JSONObject jsonObject = new JSONObject(msg.obj.toString().replace("\uFEFF", ""));
                            ret = jsonObject.getInt("ret");
                            if (ret == Config.RESULT_RET_SUCCESS) {
                                MyLog.i("dd", msg.obj.toString());
                                UserPosts userPosts = (UserPosts) GsonUtils.getEntity(msg.obj.toString(), UserPosts.class);
                                List<User> users = userPosts.getPost();
                                Toast.makeText(context, "登录成功！", Toast.LENGTH_LONG).show();
                                MySharePreference mySharePreference = new MySharePreference(context);
                                mySharePreference.commitBoolean("isLogin", true);
                                mySharePreference.commitString("username",loginName.getText().toString());
                                mySharePreference.commitString("userpassword",loginPassword.getText().toString());
                                CacheUtils.saveHttpCache(Config.DIR_CACHE_PATH, "user_info", users.get(0));
                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("data", users.get(0));
                                MainActivity.fragmentHashMap.get(DreamerInfoFragment.TAG).setArguments(bundle);
                                transaction.replace(R.id.frame_content, MainActivity.fragmentHashMap.get(DreamerInfoFragment.TAG));
                                transaction.addToBackStack(null);
                                transaction.commit();
                            } else {
                                Toast.makeText(context, "登录失败！", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(context, "登录失败！", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
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

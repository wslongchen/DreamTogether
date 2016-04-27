package com.example.mrpan.dreamtogether.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mrpan.dreamtogether.R;
import com.example.mrpan.dreamtogether.entity.User;
import com.example.mrpan.dreamtogether.http.HttpHelper;
import com.example.mrpan.dreamtogether.http.HttpResponseCallBack;
import com.example.mrpan.dreamtogether.utils.Config;
import com.example.mrpan.dreamtogether.utils.DateUtils;
import com.example.mrpan.dreamtogether.utils.DialogUtils;
import com.example.mrpan.dreamtogether.utils.Md5Utils;
import com.example.mrpan.dreamtogether.utils.OtherUtils;
import com.example.mrpan.dreamtogether.utils.RegexUtils;
import com.example.mrpan.dreamtogether.view.DeletableEditText;
import com.example.mrpan.dreamtogether.view.TitleBar;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mrpan on 16/3/21.
 */
public class DreamerRegisterFragment extends Fragment implements View.OnClickListener{

    public static final String TAG="DreamerRegister";

    private View currentView=null;
    private User user=null;
    private HttpHelper httpHelper=null;
    private Context context=null;

    private DeletableEditText name,password,password_valid,email,nickname,phone;
    private TextView xieyi;
    private Button registerBtn;
    private CheckBox register_agree_ck;

    private TitleBar titleBar;
    FragmentTransaction transaction=null;

    private Dialog dialog=null;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView=inflater.inflate(R.layout.user_register, container, false);
        ViewGroup viewGroup= (ViewGroup) currentView.getParent();
        if(viewGroup!=null){
            viewGroup.removeView(currentView);
        }
        context=getActivity();
        httpHelper=HttpHelper.getInstance();
        init();
        return currentView;
    }

    private void init(){
        transaction=getFragmentManager().beginTransaction();
        titleBar=(TitleBar)currentView.findViewById(R.id.top_bar);
        titleBar.showLeft("新用户注册",getResources().getDrawable(R.drawable.btn_backed),this);
        name=(DeletableEditText)currentView.findViewById(R.id.register_loginName);
        nickname=(DeletableEditText)currentView.findViewById(R.id.register_nickname);
        password=(DeletableEditText)currentView.findViewById(R.id.register_password);
        password_valid=(DeletableEditText)currentView.findViewById(R.id.register_password_valid);
        email=(DeletableEditText)currentView.findViewById(R.id.register_email);
        xieyi=(TextView)currentView.findViewById(R.id.register_tvAboutAgreement);
        register_agree_ck=(CheckBox)currentView.findViewById(R.id.register_ck);
        registerBtn=(Button)currentView.findViewById(R.id.btnRegister);
        registerBtn.setOnClickListener(this);
        registerBtn.setEnabled(false);
        phone=(DeletableEditText)currentView.findViewById(R.id.register_phone);
        register_agree_ck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    registerBtn.setEnabled(true);
                }
                else
                {
                    registerBtn.setEnabled(false);
                }
            }
        });

    }

    private boolean prepareData(){
        user=new User();
        String namestr=name.getText().toString();

        String emailstr=email.getText().toString();
        String nicknamestr=nickname.getText().toString();

        String phonestr=phone.getText().toString();

        user.setUser_email(emailstr);
        user.setUser_nickname(nicknamestr);
        user.setUser_login(namestr);
        user.setUser_phone(phonestr);
        user.setUser_url("");
        user.setUser_status("0");
        user.setUser_registered(DateUtils.getCurrentTimeStr());
        String pass=password.getText().toString();
        String pass2=password_valid.getText().toString();
        user.setUser_pass(Md5Utils.StrToMd5(pass));
        if(!RegexUtils.checkPassword(namestr) && !RegexUtils.checkPassword(emailstr) && !RegexUtils.checkPassword(nicknamestr) && !RegexUtils.checkPassword(phonestr)&& !RegexUtils.checkPassword(pass)&& !RegexUtils.checkPassword(pass2)){
            Toast.makeText(context,"输入项不能为空！",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(!RegexUtils.checkPasswordValid(pass,pass2)){
            Toast.makeText(context,"两次输入的密码不一致！",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(!RegexUtils.checkEmail(emailstr)){
            Toast.makeText(context,"邮箱格式不正确！",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(!RegexUtils.isMobile(phonestr)){
            Toast.makeText(context,"电话号码不正确！",Toast.LENGTH_LONG).show();
            return false;
        }
        else{
            return true;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRegister:
                if(prepareData()) {
                    dialog = DialogUtils.getLoadDialog(getActivity());
                    dialog.show();
                    if (user != null) {
                        httpHelper.asyHttpPostRequest(Config.CREATE_USER, OtherUtils.UserToNameValuePair(user), new HttpResponseCallBack() {
                            @Override
                            public void onSuccess(String url, String result) {
                                dialog.dismiss();
                                Message message = new Message();
                                message.arg1 = Config.HTTP_REQUEST_SUCCESS;
                                message.obj = result;
                                myHander.sendMessage(message);
                            }

                            @Override
                            public void onFailure(int httpResponseCode, int errCode, String err) {
                                if (dialog.isShowing())
                                    dialog.dismiss();
                                Message message = new Message();
                                message.arg1 = Config.HTTP_REQUEST_ERROR;
                                myHander.sendMessage(message);
                            }
                        });
                    }
                }
                break;
            case R.id.titleBarLeftImage:
                    getActivity().finish();
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
                    if(msg.obj!=null){
                        int ret=0;
                        try {
                            JSONObject jsonObject = new JSONObject(msg.obj.toString().replace("\uFEFF\uFEFF\uFEFF", ""));
                            ret = jsonObject.getInt("ret");
                            if (ret == Config.RESULT_RET_SUCCESS) {
                                Toast.makeText(context,"注册成功！",Toast.LENGTH_LONG).show();
                                //Intent intent = new Intent(context, WorldCircleFragment.class);
                                //startActivityForResult(intent, Config.RESULT_RET_SUCCESS);
                                getActivity().finish();
                                // Toast.makeText(context, "Publish successed!", Toast.LENGTH_LONG).show();
                            } else
                            {
                                Toast.makeText(context, "注册失败！", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
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

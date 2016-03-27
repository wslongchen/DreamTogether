package com.example.mrpan.dreamtogether.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mrpan.dreamtogether.R;
import com.example.mrpan.dreamtogether.entity.User;
import com.example.mrpan.dreamtogether.http.HttpHelper;
import com.example.mrpan.dreamtogether.http.HttpResponseCallBack;
import com.example.mrpan.dreamtogether.utils.Config;
import com.example.mrpan.dreamtogether.utils.DateUtils;
import com.example.mrpan.dreamtogether.utils.Md5Utils;
import com.example.mrpan.dreamtogether.utils.OtherUtils;
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

    private DeletableEditText name,password,email,nickname;
    private TextView xieyi;
    private Button registerBtn;
    private CheckBox register_agree_ck;

    private TitleBar titleBar;
    FragmentTransaction transaction=null;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView=inflater.inflate(R.layout.user_register,container,false);
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
        titleBar.showLeft("新用户注册",getResources().getDrawable(R.drawable.btn_back),this);
        name=(DeletableEditText)currentView.findViewById(R.id.register_loginName);
        nickname=(DeletableEditText)currentView.findViewById(R.id.register_nickname);
        password=(DeletableEditText)currentView.findViewById(R.id.register_password);
        email=(DeletableEditText)currentView.findViewById(R.id.register_email);
        xieyi=(TextView)currentView.findViewById(R.id.register_tvAboutAgreement);
        register_agree_ck=(CheckBox)currentView.findViewById(R.id.register_ck);
        registerBtn=(Button)currentView.findViewById(R.id.btnRegister);
        registerBtn.setOnClickListener(this);

    }

    private void prepareData(){
        user=new User();
        user.setUser_login(name.getText().toString());
        user.setUser_email(email.getText().toString());
        user.setUser_nickname(nickname.getText().toString());
        user.setUser_url("");
        user.setUser_status("0");
        user.setUser_registered(DateUtils.getCurrentTimeStr());
        String pass=password.getText().toString();
        user.setUser_pass(Md5Utils.StrToMd5(pass));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRegister:
                prepareData();
                if(user!=null){
                    httpHelper.asyHttpPostRequest(Config.CREATE_USER, OtherUtils.UserToNameValuePair(user), new HttpResponseCallBack() {
                        @Override
                        public void onSuccess(String url, String result) {
                            try {
                                JSONObject jsonObject = new JSONObject(result.replace("\uFEFF\uFEFF\uFEFF", ""));
                                int ret = jsonObject.getInt("ret");
                                if (ret == Config.RESULT_RET_SUCCESS) {
                                    Intent intent = new Intent(context, WorldCircleFragment.class);
                                    startActivityForResult(intent, Config.RESULT_RET_SUCCESS);
                                   // Toast.makeText(context, "Publish successed!", Toast.LENGTH_LONG).show();
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
            case R.id.titleBarLeftImage:
                    getFragmentManager().popBackStack();
                break;
            default:
                break;
        }
    }
}

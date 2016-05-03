package com.example.mrpan.dreamtogether.fragment;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mrpan.dreamtogether.MainActivity;
import com.example.mrpan.dreamtogether.OtherActivity;
import com.example.mrpan.dreamtogether.R;
import com.example.mrpan.dreamtogether.entity.User;
import com.example.mrpan.dreamtogether.utils.CacheUtils;
import com.example.mrpan.dreamtogether.utils.Config;
import com.example.mrpan.dreamtogether.adapter.UserInfoAdapter;
import com.example.mrpan.dreamtogether.utils.MySharePreference;
import com.example.mrpan.dreamtogether.view.CustomDialog;
import com.example.mrpan.dreamtogether.view.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by mrpan on 16/3/24.
 */
public class DreamerInfoFragment extends Fragment implements AdapterView.OnItemClickListener,View.OnClickListener{

    public final static String TAG="DreamerInfo";

    private View currentView;

    private TextView userNickname,userInfos;

    private ImageView userImg,qrImg;

    private ListView userInfoList;

    private Context context;

    private User user=null;

    private TitleBar titleBar=null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView=inflater.inflate(R.layout.user_info,container,false);
        ViewGroup viewGroup= (ViewGroup) currentView.getParent();
        if(viewGroup!=null){
            viewGroup.removeView(currentView);
        }
        context=getActivity();
        Bundle data=getArguments();
        if(data!=null) {
            user = (User) data.getSerializable("data");
        }else{
            user=(User)CacheUtils.readHttpCache(Config.DIR_CACHE_PATH,"user_info");
        }
        initView();

        return currentView;

    }

    private void initView(){
        titleBar=(TitleBar)currentView.findViewById(R.id.top_bar);
        titleBar.showRight("我的梦想",R.drawable.btn_post,this);
        userInfoList=(ListView)currentView.findViewById(R.id.user_info_list);
        userNickname=(TextView)currentView.findViewById(R.id.user_nickname);
        userImg=(ImageView)currentView.findViewById(R.id.userImg);
        qrImg=(ImageView)currentView.findViewById(R.id.qrImg);
        qrImg.setOnClickListener(this);

        if(user!=null){
            userNickname.setText(user.getUser_nickname());
        }

        List<HashMap<String,Object>> datas=new ArrayList<>();
        HashMap<String,Object> data=null;
        data=new HashMap<>();
        data.put("isNull", true);
        datas.add(data);
        data=new HashMap<>();
        data.put("isNull", false);
        data.put("menuImg", R.mipmap.ic_launcher);
        data.put("menuText", "我的梦想历程");
        data.put("menu","menu1");
        datas.add(data);
        data=new HashMap<>();
        data.put("isNull", false);
        data.put("menuImg", R.mipmap.ic_launcher);
        data.put("menuText", "菜单二");
        data.put("menu","menu2");
        datas.add(data);
        data=new HashMap<>();
        data.put("isNull", true);
        datas.add(data);
        data=new HashMap<>();
        data.put("isNull", false);
        data.put("menuImg", R.mipmap.ic_launcher);
        data.put("menuText", "退出登录");
        data.put("menu","menuExit");
        datas.add(data);
        data=new HashMap<>();
        data.put("isNull", true);
        datas.add(data);

        //View heardview=View.inflate(context,R.layout.user_info_header,null);
        //userInfoList.addHeaderView(heardview);
        UserInfoAdapter infoAdapter=new UserInfoAdapter(datas,context);
        userInfoList.setAdapter(infoAdapter);
        userInfoList.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ListView listView=(ListView)parent;
        HashMap<String, Object> map = (HashMap<String, Object>) listView.getItemAtPosition(position);
        if(map.get("isNull").equals(true))
            return;
        switch (map.get("menu").toString()){
            case "menu1":
                Intent intent=new Intent();
                Bundle bundle=new Bundle();
                bundle.putInt("type", Config.TIMELINE_TYPE);
                bundle.putInt("data", user.getID());
                intent.putExtras(bundle);
                intent.setClass(context, OtherActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
                break;
            case "menuExit":
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("是否退出登陆?");
                builder.setTitle("提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        MySharePreference mySharePreference = new MySharePreference(context);
                        mySharePreference.commitBoolean("isLogin", false);
                        android.support.v4.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_content, MainActivity.fragmentHashMap.get(DreamerLoginFragment.TAG));
                        //transaction.addToBackStack(null);
                        transaction.commit();
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("取消",
                        new android.content.DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                builder.create().show();

                break;
            default:
                break;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.qrImg:
                Toast.makeText(context,"QR",Toast.LENGTH_LONG).show();
                break;
            case R.id.titleBarRightImage:
                Intent intent=new Intent();
                Bundle bundle=new Bundle();
                bundle.putInt("type", Config.POST_TYPE);
                bundle.putInt("data", user.getID());
                intent.putExtras(bundle);
                intent.setClass(context, OtherActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.top_in, R.anim.top_out);
                break;
            default:
                break;
        }
    }
}

package com.example.mrpan.dreamtogether.fragment;

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

import com.example.mrpan.dreamtogether.OtherActivity;
import com.example.mrpan.dreamtogether.R;
import com.example.mrpan.dreamtogether.entity.User;
import com.example.mrpan.dreamtogether.utils.Config;
import com.example.mrpan.dreamtogether.utils.UserInfoAdapter;

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
        user= (User) data.getSerializable("data");

        initView();

        return currentView;

    }

    private void initView(){

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
        data.put("menuText", "菜单二");
        data.put("menu","menu3");
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
                intent.putExtras(bundle);
                intent.setClass(context, OtherActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
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
            default:
                break;
        }
    }
}

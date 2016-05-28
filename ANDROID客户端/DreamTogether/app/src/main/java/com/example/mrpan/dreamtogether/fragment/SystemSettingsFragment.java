package com.example.mrpan.dreamtogether.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mrpan.dreamtogether.OtherActivity;
import com.example.mrpan.dreamtogether.R;
import com.example.mrpan.dreamtogether.adapter.SystemSettingAdapter;
import com.example.mrpan.dreamtogether.entity.User;
import com.example.mrpan.dreamtogether.utils.CacheUtils;
import com.example.mrpan.dreamtogether.utils.Config;
import com.example.mrpan.dreamtogether.view.NoScrollGridView;
import com.example.mrpan.dreamtogether.view.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mrpan on 16/4/27.
 */
public class SystemSettingsFragment extends Fragment implements View.OnClickListener{

    public final static String TAG="SystemSettings";

    private View currentView;

    private Context context;

    private TitleBar titleBar=null;

    private NoScrollGridView setting_grid;

    private TextView about=null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView=inflater.inflate(R.layout.system_settings_fragment,container,false);
        ViewGroup viewGroup= (ViewGroup) currentView.getParent();
        if(viewGroup!=null){
            viewGroup.removeView(currentView);
        }
        context=getActivity();

        initView();

        return currentView;

    }

    private void initView(){
        setting_grid=(NoScrollGridView)currentView.findViewById(R.id.setting_grid);
        ArrayList<HashMap<String,Object>> menus=new ArrayList<>();
        HashMap<String,Object> menu1=new HashMap<>();
        menu1.put("image", R.mipmap.gonggao);
        menu1.put("text", "公告通知");
        menus.add(menu1);
        HashMap<String,Object> menu2=new HashMap<>();
        menu2.put("image", R.mipmap.help);
        menu2.put("text", "帮助中心");
        menus.add(menu2);
        HashMap<String,Object> menu3=new HashMap<>();
        menu3.put("image", R.mipmap.lianxi);
        menu3.put("text", "联系我们");
        menus.add(menu3);
        HashMap<String,Object> menu4=new HashMap<>();
        menu4.put("image", R.mipmap.guli);
        menu4.put("text", "鼓励我们");
        menus.add(menu4);
        HashMap<String,Object> menu5=new HashMap<>();
        menu5.put("image",R.mipmap.guanyu);
        menu5.put("text", "关于我们");
        menus.add(menu5);
        HashMap<String,Object> menu6=new HashMap<>();
        menu6.put("image",R.mipmap.share);
        menu6.put("text", "分享");
        menus.add(menu6);




        SystemSettingAdapter adapter=new SystemSettingAdapter(context,menus);
        setting_grid.setAdapter(adapter);
//        titleBar=(TitleBar)currentView.findViewById(R.id.top_bar);
//        titleBar.showCenterTitle("系统设置");
//        about=(TextView)currentView.findViewById(R.id.item_about);
//        about.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.item_about:
//                Intent intent=new Intent();
//                Bundle bundle=new Bundle();
//                bundle.putInt("type", Config.BROWSER_TYPE);
//                intent.putExtras(bundle);
//                intent.setClass(context, OtherActivity.class);
//                startActivity(intent);
//                getActivity().overridePendingTransition(R.anim.top_in, R.anim.top_out);
//                break;
            default:
                break;
        }
    }
}

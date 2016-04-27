package com.example.mrpan.dreamtogether.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mrpan.dreamtogether.R;
import com.example.mrpan.dreamtogether.entity.User;
import com.example.mrpan.dreamtogether.utils.CacheUtils;
import com.example.mrpan.dreamtogether.utils.Config;
import com.example.mrpan.dreamtogether.view.TitleBar;

/**
 * Created by mrpan on 16/4/27.
 */
public class SystemSettingsFragment extends Fragment implements View.OnClickListener{

    public final static String TAG="SystemSettings";

    private View currentView;

    private Context context;

    private TitleBar titleBar=null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView=inflater.inflate(R.layout.dream_settings_fragment,container,false);
        ViewGroup viewGroup= (ViewGroup) currentView.getParent();
        if(viewGroup!=null){
            viewGroup.removeView(currentView);
        }
        context=getActivity();

        initView();

        return currentView;

    }

    private void initView(){
        titleBar=(TitleBar)currentView.findViewById(R.id.top_bar);
        titleBar.showCenterTitle("系统设置");
    }

    @Override
    public void onClick(View v) {

    }
}

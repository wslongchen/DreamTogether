package com.example.mrpan.dreamtogether.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mrpan.dreamtogether.R;
import com.example.mrpan.dreamtogether.view.TitleBar;

/**
 * Created by mrpan on 16/4/25.
 */
public class DreamFragment extends Fragment implements View.OnClickListener {

    public static final String TAG="Dream";

    private View currentView;

    private TitleBar topbar;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView=inflater.inflate(R.layout.dreams_demo,container,false);
        ViewGroup viewGroup= (ViewGroup) currentView.getParent();
        if(viewGroup!=null){
            viewGroup.removeView(currentView);
        }
        initView();
        return currentView;
    }

    private void initView(){
        topbar=(TitleBar)currentView.findViewById(R.id.top_bar);
        topbar.showCenterTitle("梦想");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.titleBarRightImage:
                 break;
            default:
                break;
        }
    }
}

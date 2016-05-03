package com.example.mrpan.dreamtogether.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mrpan.dreamtogether.MainActivity;
import com.example.mrpan.dreamtogether.R;
import com.example.mrpan.dreamtogether.adapter.TestFragPagerAdapter;
import com.example.mrpan.dreamtogether.entity.Dream;
import com.example.mrpan.dreamtogether.entity.DreamPosts;
import com.example.mrpan.dreamtogether.entity.User;
import com.example.mrpan.dreamtogether.entity.UserPosts;
import com.example.mrpan.dreamtogether.http.HttpResponseCallBack;
import com.example.mrpan.dreamtogether.utils.CacheUtils;
import com.example.mrpan.dreamtogether.utils.Config;
import com.example.mrpan.dreamtogether.utils.GsonUtils;
import com.example.mrpan.dreamtogether.utils.MyLog;
import com.example.mrpan.dreamtogether.utils.MySharePreference;
import com.example.mrpan.dreamtogether.view.MrpanViewPager;
import com.example.mrpan.dreamtogether.view.TitleBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by mrpan on 16/4/27.
 */
public class DreamRadomFragment extends Fragment implements View.OnClickListener{

    public final static String TAG="DreamRadom";

    MrpanViewPager pager;
    int currentItem;

    private List<Dream> dreams;

    private View currentView;
    private Context context;

    private TitleBar titleBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView=inflater.inflate(R.layout.dream_star_fragment,container,false);
        ViewGroup viewGroup= (ViewGroup) currentView.getParent();
        if(viewGroup!=null){
            viewGroup.removeView(currentView);
        }
        context=getActivity();
        initView();
        return currentView;
    }

    private void initView(){
        //titleBar=(TitleBar)currentView.findViewById(R.id.top_bar);
        pager = (MrpanViewPager) currentView.findViewById(R.id.myViewPager1);
        pager.setAdapter(new TestFragPagerAdapter(getActivity().getSupportFragmentManager(),dreams));

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int state) {
                switch(state){
                    case 1: //正在滑动
                        break;
                    case 2: //滑动结束
                        break;
                }
            }
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int arg0) {
            }

        });
    }

    @Override
    public void onResume() {
        super.onResume();
//        pager.notify();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

        }
    }

    public List<Dream> getDreams() {
        return dreams;
    }

    public void setDreams(List<Dream> dreams) {
        this.dreams = dreams;
    }
}

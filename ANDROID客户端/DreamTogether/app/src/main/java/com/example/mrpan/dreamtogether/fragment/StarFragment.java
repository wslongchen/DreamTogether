package com.example.mrpan.dreamtogether.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.mrpan.dreamtogether.R;
import com.example.mrpan.dreamtogether.adapter.TestFragPagerAdapter;
import com.example.mrpan.dreamtogether.entity.Dream;
import com.example.mrpan.dreamtogether.entity.DreamPosts;
import com.example.mrpan.dreamtogether.utils.CacheUtils;
import com.example.mrpan.dreamtogether.utils.Config;
import com.example.mrpan.dreamtogether.view.DeletableEditText;
import com.example.mrpan.dreamtogether.view.StarViewPager;
import com.example.mrpan.dreamtogether.view.TitleBar;

import java.util.List;

/**
 * Created by mrpan on 16/5/23.
 */
public class StarFragment extends Fragment implements View.OnClickListener{

    public static final String TAG="Star";

    private View currentView=null;

    ViewPager pager=null;

    private Context context=null;

    private TitleBar titleBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView=inflater.inflate(R.layout.star_fragment, container, false);
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
        pager=(ViewPager) currentView.findViewById(R.id.myViewPager1);
        DreamPosts cache= (DreamPosts) CacheUtils.readHttpCache(Config.DIR_CACHE_PATH, "all_dream");
        if(cache!=null)
        {
            pager.setAdapter(new StarPagerAdapter(getFragmentManager(),cache.getPost()));

        }

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int state) {
                switch(state){
                    case 1: //正在滑动
                        break;
                    case 2: //滑动结束
                        //tips.setVisibility(View.GONE);
                        break;
                }
            }
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                if(arg2<300 &&arg2>0){

                }else {

                }


            }

            @Override
            public void onPageSelected(int arg0) {

            }

        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case 1:
                break;
            default:
                break;
        }
    }



    public class StarPagerAdapter extends FragmentStatePagerAdapter {

        private List<Dream> dreams;

        public StarPagerAdapter(FragmentManager fm,List<Dream> dreams) {
            super(fm);
            this.dreams=dreams;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            StarItemFragment f = (StarItemFragment) super.instantiateItem(container, position);
            f.setDream(dreams.get(position % getCount()));
            return f;
        }

        @Override
        public Fragment getItem(int arg0) {
            Fragment frag =null;
            frag=new StarItemFragment();
            return frag;
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }

        @Override
        public int getCount() {
            return dreams.size();
        }
    }
}

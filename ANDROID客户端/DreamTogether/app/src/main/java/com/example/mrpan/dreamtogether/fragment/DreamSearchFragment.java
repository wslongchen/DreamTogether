package com.example.mrpan.dreamtogether.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mrpan.dreamtogether.R;
import com.example.mrpan.dreamtogether.utils.Config;
import com.example.mrpan.dreamtogether.view.DeletableEditText;
import com.example.mrpan.dreamtogether.view.KeywordsFlow;
import com.example.mrpan.dreamtogether.view.TitleBar;

import java.util.Random;

/**
 * Created by mrpan on 16/3/21.
 */
public class DreamSearchFragment extends Fragment implements View.OnClickListener{

    public final static String TAG="DreamSearch";

    private ImageView back_arrow;
    private Animation shakeAnim;
    private DeletableEditText searchEdit;
    private KeywordsFlow keywordsFlow;
    private View currentView;
    private Context context;

    private TitleBar titleBar;

    int STATE=1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView=inflater.inflate(R.layout.activity_dream_search,container,false);
        ViewGroup viewGroup= (ViewGroup) currentView.getParent();
        if(viewGroup!=null){
            viewGroup.removeView(currentView);
        }
        context=getActivity();
        initView();
        return currentView;
    }

    private void initView() {
        titleBar=(TitleBar)currentView.findViewById(R.id.top_bar);
        titleBar.showCenterTitle("搜索");
        keywordsFlow = (KeywordsFlow) currentView.findViewById(R.id.keywordsflow);
        keywordsFlow.setDuration(1000l);
        keywordsFlow.setOnItemClickListener(this);
        back_arrow = (ImageView) currentView.findViewById(R.id.back_arrow);
        back_arrow.setAnimation(shakeAnim);
        searchEdit = (DeletableEditText) currentView.findViewById(R.id.search_view);
        feedKeywordsFlow(keywordsFlow, keywords);
        keywordsFlow.go2Show(KeywordsFlow.ANIMATION_IN);
        handler.sendEmptyMessageDelayed(Config.FEEDKEY_START, 5000);
        shakeAnim = AnimationUtils.loadAnimation(context, R.anim.shake_y);
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case Config.FEEDKEY_START:
                    keywordsFlow.rubKeywords();
                    feedKeywordsFlow(keywordsFlow, keywords);
                    keywordsFlow.go2Show(KeywordsFlow.ANIMATION_OUT);
                    sendEmptyMessageDelayed(Config.FEEDKEY_START, 5000);
                    break;
            }
        };
    };
    private static String[] keywords = new String[] { "娘要嫁人", "球爱酒吧", "使徒行者",
            "亮剑", "完美搭档", "致青春", "非常完美", "一生一世", "穿越火线", "天龙八部", "匹诺曹", "让子弹飞",
            "穿越火线", "情定三生", "心术", "马向阳下乡记", "人在囧途", " 高达", " 刀剑神域", "泡芙小姐",
            "尖刀出鞘", "甄嬛传", "兵出潼关", "电锯惊魂3D", "古剑奇谭", "同桌的你" };



    private static void feedKeywordsFlow(KeywordsFlow keywordsFlow, String[] arr) {
        Random random = new Random();
        for (int i = 0; i < KeywordsFlow.MAX; i++) {
            int ran = random.nextInt(arr.length);
            String tmp = arr[ran];
            keywordsFlow.feedKeyword(tmp);
        }

    }

    @Override
    public void onClick(View v) {
        if (v instanceof TextView) {
            String keyword = ((TextView) v).getText().toString().trim();
            searchEdit.setText(keyword);
            searchEdit.setSelection(keyword.length());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        back_arrow.clearAnimation();
        handler.removeMessages(Config.FEEDKEY_START);
        STATE = 0;
    }

    @Override
    public void onStop() {
        super.onStop();
        handler.removeMessages(Config.FEEDKEY_START);
        STATE = 0;
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeMessages(Config.FEEDKEY_START);
        STATE = 0;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (STATE == 0) {
            keywordsFlow.rubKeywords();
            handler.sendEmptyMessageDelayed(Config.FEEDKEY_START, 3000);
        }

    }
}

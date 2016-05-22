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
    private static String[] keywords = new String[] { "小安安", "梦想自留地", "www.mrpann.com",
            "小男人", "穿越去唐朝", "潘安", "程序员", "PHP大法好", "C艹", "Python", "石江凯", "找女朋友",
            "小灰灰", "陈固", "心术", "石江凯找女朋友", "去月球", " 买吉他", " 中五百万", "拍电影",
            "做模特", "考一百分", "变帅", "吃鲍鱼", "买买卖", "看电影" };



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
        getFragmentManager().beginTransaction().remove(this);
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

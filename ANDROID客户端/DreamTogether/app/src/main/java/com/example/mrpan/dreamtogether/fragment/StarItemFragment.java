package com.example.mrpan.dreamtogether.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mrpan.dreamtogether.OtherActivity;
import com.example.mrpan.dreamtogether.R;
import com.example.mrpan.dreamtogether.adapter.DreamImageGridAdapter;
import com.example.mrpan.dreamtogether.entity.Dream;
import com.example.mrpan.dreamtogether.utils.Config;
import com.example.mrpan.dreamtogether.utils.DateUtils;
import com.example.mrpan.dreamtogether.utils.MyLog;
import com.example.mrpan.dreamtogether.view.TitleBar;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mrpan on 16/5/24.
 */
public class StarItemFragment extends Fragment {
    private Context context;
    private View currentView;
    private Dream dream;

    private ImageView imageView,image_star;
    private TextView temprature,dream_location,time,content,author;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView=inflater.inflate(R.layout.xiaoji_fragment,container,false);
        ViewGroup viewGroup= (ViewGroup) currentView.getParent();
        if(viewGroup!=null){
            viewGroup.removeView(currentView);
        }
        context=getActivity();
        initView();
        initData();
        return currentView;
    }

    private void initData() {
        author.setText(dream.getPost_author().getUser_nickname());
        content.setText(dream.getPost_content());
        if(dream.getPost_imgs()!=null && !dream.getPost_imgs().equals("")) {
            if (dream.getPost_imgs().length() > 0 && !dream.getPost_imgs().trim().isEmpty() && !dream.getPost_imgs().equals("")) {
                String[] imgs = dream.getPost_imgs().split(",");
                if(imgs.length>0){
                    ImageLoader.getInstance().displayImage("http://" + imgs[0],image_star);
                }
        }
        }else{
            image_star.setImageResource(R.mipmap.bg_search);
        }
        time.setText(DateUtils.getDateStr(dream.getPost_date()));
    }

    private void initView(){
        temprature=(TextView)currentView.findViewById(R.id.temprature);
        dream_location=(TextView)currentView.findViewById(R.id.dream_location);
        time=(TextView)currentView.findViewById(R.id.time);
        content=(TextView)currentView.findViewById(R.id.content);
        author=(TextView)currentView.findViewById(R.id.author);
        image_star=(ImageView)currentView.findViewById(R.id.image_star);
        imageView=(ImageView)currentView.findViewById(R.id.image_share);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt("type", Config.SHARE_TYPE);
                bundle.putSerializable("data",dream);
                intent.putExtras(bundle);
                intent.setClass(context, OtherActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.top_in, R.anim.top_out);
            }
        });
    }

    public Dream getDream() {
        return dream;
    }

    public void setDream(Dream dream) {
        this.dream = dream;
    }
}

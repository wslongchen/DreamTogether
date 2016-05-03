package com.example.mrpan.dreamtogether.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.mrpan.dreamtogether.R;
import com.example.mrpan.dreamtogether.entity.Dream;
import com.example.mrpan.dreamtogether.utils.Config;

import java.util.List;

/**
 * Created by mrpan on 16/4/27.
 */
public class CardDreamFragment extends Fragment {

    public static final String TAG="CardDream";
    private Dream dream;
    boolean visible = true;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dream_cardview_fragment, container, false);
        initUI(rootView);
        return rootView;
    }

    private void initUI(View root){
        final View tv1 = root.findViewById(R.id.textView1);
        final View tv2 = root.findViewById(R.id.textView2);
        ImageView iv = (ImageView) root.findViewById(R.id.imageView1);
        Bundle bundle = getArguments();
        int res = bundle.getInt(Config.KEY, R.mipmap.bg_search);
        iv.setImageResource(res);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(!visible){
//                    visible = true;
//                    tv1.setVisibility(View.VISIBLE);
//                    tv2.setVisibility(View.VISIBLE);
//                }else{
//                    visible = false;
//                    tv1.setVisibility(View.INVISIBLE);
//                    tv2.setVisibility(View.INVISIBLE);
//                }
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

package com.example.mrpan.dreamtogether.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mrpan.dreamtogether.OtherActivity;
import com.example.mrpan.dreamtogether.R;
import com.example.mrpan.dreamtogether.adapter.DreamImageGridAdapter;
import com.example.mrpan.dreamtogether.entity.Dream;
import com.example.mrpan.dreamtogether.entity.User;
import com.example.mrpan.dreamtogether.utils.Config;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
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
        //final View tv1 = root.findViewById(R.id.textView1);
        //final View tv2 = root.findViewById(R.id.textView2);
        TextView author=(TextView)root.findViewById(R.id.dream_random_author);
        TextView content=(TextView)root.findViewById(R.id.dream_random_content);
        ImageView iv = (ImageView) root.findViewById(R.id.dream_random_img);

        //Bundle bundle = getArguments();
        User user =dream.getPost_author(); //bundle.getInt(Config.KEY, R.mipmap.bg_search);
        final String[] imgs;
        if(dream.getPost_imgs()!=null && !dream.getPost_imgs().equals("")) {
            if (dream.getPost_imgs().length() > 0 && !dream.getPost_imgs().trim().isEmpty() && !dream.getPost_imgs().equals("")) {
                imgs = dream.getPost_imgs().split(",");
                ImageLoader.getInstance().displayImage("http://"+imgs[0],iv);
            }
        }else {
            iv.setImageResource(R.mipmap.bg_search);
        }
        content.setText(dream.getPost_content());

        author.setText(user.getUser_nickname());
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.left_in, R.anim.left_out, R.anim.right_in, R.anim.right_out);
                ((DreamDetailFragment)OtherActivity.fragmentHashMap.get(DreamDetailFragment.TAG)).setDream(dream);
                transaction.replace(R.id.other_layout, OtherActivity.fragmentHashMap.get(DreamDetailFragment.TAG));
                transaction.addToBackStack(null);
                transaction.commit();
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

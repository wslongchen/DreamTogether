package com.example.mrpan.dreamtogether;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.example.mrpan.dreamtogether.fragment.DreamerRegisterFragment;
import com.example.mrpan.dreamtogether.fragment.UserDreamListFragment;
import com.example.mrpan.dreamtogether.fragment.WorldCircleFragment;
import com.example.mrpan.dreamtogether.utils.Config;

import java.util.HashMap;

/**
 * Created by mrpan on 16/3/26.
 */
public class OtherActivity extends FragmentActivity {

    public static HashMap<String,Fragment> fragmentHashMap=null;
    private DreamerRegisterFragment dreamerRegisterFragment;
    private FragmentTransaction transaction=null;
    private UserDreamListFragment userDreamListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);
        initView();
        Bundle bundle=getIntent().getExtras();
        int type=bundle.getInt("type");
        switch (type){
            case Config.REGISTER_TYPE:
                transaction = getSupportFragmentManager().beginTransaction();
                //transaction.setCustomAnimations(R.anim.left_in,R.anim.left_out,R.anim.right_in,R.anim.right_out);
                transaction.replace(R.id.other_layout, fragmentHashMap.get(DreamerRegisterFragment.TAG));
                //transaction.addToBackStack(null);
                transaction.commit();
                break;
            case Config.TIMELINE_TYPE:
                transaction = getSupportFragmentManager().beginTransaction();
                //transaction.setCustomAnimations(R.anim.left_in,R.anim.left_out,R.anim.right_in,R.anim.right_out);
                transaction.replace(R.id.other_layout,fragmentHashMap.get(userDreamListFragment.TAG));
                //transaction.addToBackStack(null);
                transaction.commit();
                break;
            default:
                break;
        }
    }

    private void initView() {
        fragmentHashMap=new HashMap<>();
        dreamerRegisterFragment=new DreamerRegisterFragment();
        userDreamListFragment=new UserDreamListFragment();


        fragmentHashMap.put(DreamerRegisterFragment.TAG, dreamerRegisterFragment);
        fragmentHashMap.put(UserDreamListFragment.TAG,userDreamListFragment);


    }
}

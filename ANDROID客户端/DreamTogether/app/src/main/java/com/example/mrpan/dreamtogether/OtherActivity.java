package com.example.mrpan.dreamtogether;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.example.mrpan.dreamtogether.fragment.DreamPostFragment;
import com.example.mrpan.dreamtogether.fragment.DreamerRegisterFragment;
import com.example.mrpan.dreamtogether.fragment.PhotoFragment;
import com.example.mrpan.dreamtogether.fragment.PicSelectFragment;
import com.example.mrpan.dreamtogether.fragment.SelectImageGridFragment;
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
    private DreamPostFragment dreamPostFragment;
    private PhotoFragment photoFragment;
    private PicSelectFragment picSelectFragment;
    private SelectImageGridFragment selectImageGridFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);
        initView();
        Bundle bundle=getIntent().getExtras();
        int type=bundle.getInt("type");
        int ID=0;
        switch (type){
            case Config.REGISTER_TYPE:
                transaction = getSupportFragmentManager().beginTransaction();
                //transaction.setCustomAnimations(R.anim.left_in,R.anim.left_out,R.anim.right_in,R.anim.right_out);
                transaction.replace(R.id.other_layout, fragmentHashMap.get(DreamerRegisterFragment.TAG));
                //transaction.addToBackStack(null);
                transaction.commit();
                break;
            case Config.TIMELINE_TYPE:
                ID=bundle.getInt("data");
                transaction = getSupportFragmentManager().beginTransaction();
                //transaction.setCustomAnimations(R.anim.left_in,R.anim.left_out,R.anim.right_in,R.anim.right_out);
                transaction.replace(R.id.other_layout, fragmentHashMap.get(UserDreamListFragment.TAG));
                ((UserDreamListFragment)fragmentHashMap.get(UserDreamListFragment.TAG)).setAuthorID(ID);
                //transaction.addToBackStack(null);
                transaction.commit();
                break;
            case Config.POST_TYPE:
                ID=bundle.getInt("data");
                transaction=getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.other_layout, fragmentHashMap.get(DreamPostFragment.TAG));
                ((DreamPostFragment)fragmentHashMap.get(DreamPostFragment.TAG)).setUserID(ID);
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
        dreamPostFragment=new DreamPostFragment();
        photoFragment=new PhotoFragment();
        picSelectFragment=new PicSelectFragment();
        selectImageGridFragment=new SelectImageGridFragment();

        fragmentHashMap.put(DreamerRegisterFragment.TAG, dreamerRegisterFragment);
        fragmentHashMap.put(UserDreamListFragment.TAG,userDreamListFragment);
        fragmentHashMap.put(DreamPostFragment.TAG,dreamPostFragment);
        fragmentHashMap.put(PhotoFragment.TAG,photoFragment);
        fragmentHashMap.put(PicSelectFragment.TAG,picSelectFragment);
        fragmentHashMap.put(SelectImageGridFragment.TAG,selectImageGridFragment);

    }
}

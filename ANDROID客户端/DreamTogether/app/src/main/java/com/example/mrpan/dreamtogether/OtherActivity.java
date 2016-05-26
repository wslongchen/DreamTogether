package com.example.mrpan.dreamtogether;

import android.os.Bundle;
import android.provider.Browser;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.EditText;

import com.example.mrpan.dreamtogether.entity.Dream;
import com.example.mrpan.dreamtogether.fragment.BrowserFragment;
import com.example.mrpan.dreamtogether.fragment.CardDreamFragment;
import com.example.mrpan.dreamtogether.fragment.ChatFragment;
import com.example.mrpan.dreamtogether.fragment.DreamDetailFragment;
import com.example.mrpan.dreamtogether.fragment.DreamPostFragment;
import com.example.mrpan.dreamtogether.fragment.DreamRadomFragment;
import com.example.mrpan.dreamtogether.fragment.DreamXiuXiuFragment;
import com.example.mrpan.dreamtogether.fragment.DreamerRegisterFragment;
import com.example.mrpan.dreamtogether.fragment.EditFragment;
import com.example.mrpan.dreamtogether.fragment.MessegeFragment;
import com.example.mrpan.dreamtogether.fragment.PhotoFragment;
import com.example.mrpan.dreamtogether.fragment.PicSelectFragment;
import com.example.mrpan.dreamtogether.fragment.SelectImageGridFragment;
import com.example.mrpan.dreamtogether.fragment.ShareFragment;
import com.example.mrpan.dreamtogether.fragment.UserDreamListFragment;
import com.example.mrpan.dreamtogether.fragment.WorldCircleFragment;
import com.example.mrpan.dreamtogether.utils.Config;
import com.example.mrpan.dreamtogether.utils.MyLog;

import java.util.ArrayList;
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
    private DreamRadomFragment dreamRadomFragment=null;
    private DreamXiuXiuFragment dreamXiuXiuFragment=null;
    private DreamDetailFragment dreamDetailFragment=null;
    private BrowserFragment browserFragment=null;
    private ChatFragment chatFragment=null;
    private MessegeFragment messegeFragment=null;
    private EditFragment editFragment=null;
    private ShareFragment shareFragment=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
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
            case Config.PHOTO_TYPE:
                ID=bundle.getInt("ID");
                transaction = getSupportFragmentManager().beginTransaction();
                ((PhotoFragment)fragmentHashMap.get(PhotoFragment.TAG)).setArguments(bundle);
                //transaction.setCustomAnimations(R.anim.left_in,R.anim.left_out,R.anim.right_in,R.anim.right_out);
                transaction.replace(R.id.other_layout, fragmentHashMap.get(PhotoFragment.TAG));
                //transaction.addToBackStack(null);
                transaction.commit();
                break;
            case Config.XIUXIU_TYPE:
                System.out.println("xiuxiu");
                transaction = getSupportFragmentManager().beginTransaction();
                //transaction.setCustomAnimations(R.anim.left_in,R.anim.left_out,R.anim.right_in,R.anim.right_out);
                transaction.add(R.id.other_layout, fragmentHashMap.get(DreamXiuXiuFragment.TAG));
                //transaction.addToBackStack(null);
                transaction.commit();
                break;
            case Config.DREAM_DETAILS_TYPE:
                Dream dream= (Dream) bundle.getSerializable("data");
                transaction = getSupportFragmentManager().beginTransaction();
                //transaction.setCustomAnimations(R.anim.left_in,R.anim.left_out,R.anim.right_in,R.anim.right_out);
                if(dream!=null){
                    ((DreamDetailFragment)fragmentHashMap.get(DreamDetailFragment.TAG)).setDream(dream);
                }
                transaction.replace(R.id.other_layout, fragmentHashMap.get(DreamDetailFragment.TAG));
                //transaction.addToBackStack(null);
                transaction.commit();
                break;
            case Config.BROWSER_TYPE:
                transaction = getSupportFragmentManager().beginTransaction();
                //transaction.setCustomAnimations(R.anim.left_in,R.anim.left_out,R.anim.right_in,R.anim.right_out);
                transaction.replace(R.id.other_layout, fragmentHashMap.get(BrowserFragment.TAG));
                //transaction.addToBackStack(null);
                transaction.commit();
                break;
            case Config.MESSAGE_TYPE:
                transaction = getSupportFragmentManager().beginTransaction();
                //transaction.setCustomAnimations(R.anim.left_in,R.anim.left_out,R.anim.right_in,R.anim.right_out);
                transaction.replace(R.id.other_layout, fragmentHashMap.get(MessegeFragment.TAG));
                //transaction.addToBackStack(null);
                transaction.commit();
                break;
            case Config.EDIT_TYPE:
                transaction = getSupportFragmentManager().beginTransaction();
                //transaction.setCustomAnimations(R.anim.left_in,R.anim.left_out,R.anim.right_in,R.anim.right_out);
                transaction.replace(R.id.other_layout, fragmentHashMap.get(EditFragment.TAG));
                //transaction.addToBackStack(null);
                transaction.commit();
                break;
            case Config.SHARE_TYPE:
                transaction = getSupportFragmentManager().beginTransaction();
                //transaction.setCustomAnimations(R.anim.left_in,R.anim.left_out,R.anim.right_in,R.anim.right_out);
                transaction.replace(R.id.other_layout, fragmentHashMap.get(ShareFragment.TAG));
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
        dreamRadomFragment=new DreamRadomFragment();
        dreamXiuXiuFragment=new DreamXiuXiuFragment();
        dreamDetailFragment=new DreamDetailFragment();
        browserFragment=new BrowserFragment();
        chatFragment=new ChatFragment();
        messegeFragment=new MessegeFragment();
        editFragment=new EditFragment();
        shareFragment=new ShareFragment();

        fragmentHashMap.put(DreamerRegisterFragment.TAG, dreamerRegisterFragment);
        fragmentHashMap.put(UserDreamListFragment.TAG,userDreamListFragment);
        fragmentHashMap.put(DreamPostFragment.TAG,dreamPostFragment);
        fragmentHashMap.put(PhotoFragment.TAG,photoFragment);
        fragmentHashMap.put(PicSelectFragment.TAG,picSelectFragment);
        fragmentHashMap.put(SelectImageGridFragment.TAG,selectImageGridFragment);
        fragmentHashMap.put(DreamRadomFragment.TAG,dreamRadomFragment);
        fragmentHashMap.put(DreamXiuXiuFragment.TAG,dreamXiuXiuFragment);
        fragmentHashMap.put(DreamDetailFragment.TAG,dreamDetailFragment);
        fragmentHashMap.put(BrowserFragment.TAG,browserFragment);
        fragmentHashMap.put(ChatFragment.TAG,chatFragment);
        fragmentHashMap.put(MessegeFragment.TAG,messegeFragment);
        fragmentHashMap.put(EditFragment.TAG,editFragment);
        fragmentHashMap.put(ShareFragment.TAG,shareFragment);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:

                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}

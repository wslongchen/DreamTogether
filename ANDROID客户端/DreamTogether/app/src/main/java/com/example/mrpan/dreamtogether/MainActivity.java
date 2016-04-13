package com.example.mrpan.dreamtogether;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.mrpan.dreamtogether.fragment.DreamSearchFragment;
import com.example.mrpan.dreamtogether.fragment.DreamerInfoFragment;
import com.example.mrpan.dreamtogether.fragment.DreamerLoginFragment;
import com.example.mrpan.dreamtogether.fragment.DreamerRegisterFragment;
import com.example.mrpan.dreamtogether.fragment.WorldCircleFragment;
import com.example.mrpan.dreamtogether.http.HttpHelper;
import com.example.mrpan.dreamtogether.utils.Config;
import com.example.mrpan.dreamtogether.utils.DialogUtils;
import com.example.mrpan.dreamtogether.utils.MyLog;
import com.example.mrpan.dreamtogether.utils.MySharePreference;
import com.example.mrpan.dreamtogether.view.loadview.Load2Dialog;

import java.util.HashMap;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    public static HashMap<String,Fragment> fragmentHashMap=null;
    private WorldCircleFragment worldCircleFragment=null;
    private DreamerLoginFragment dreamerLoginFragment=null;
    private DreamerInfoFragment dreamerInfoFragment=null;
    //private DreamerRegisterFragment dreamerRegisterFragment=null;
    private DreamSearchFragment dreamSearchFragment=null;



    private FragmentTransaction transaction=null;
    private PopupWindow popWindow;
    private DisplayMetrics dm;
    ImageView home_iv,auth_iv,message_iv,more_iv;
    FrameLayout menuHome,menuAuth,menuMessage,menuMore;

    private void initView(){
        fragmentHashMap=new HashMap<>();
        worldCircleFragment=new WorldCircleFragment();
        fragmentHashMap.put(WorldCircleFragment.TAG,worldCircleFragment);
        dreamerLoginFragment=new DreamerLoginFragment();
        fragmentHashMap.put(DreamerLoginFragment.TAG,dreamerLoginFragment);
        dreamerInfoFragment=new DreamerInfoFragment();
        fragmentHashMap.put(DreamerInfoFragment.TAG, dreamerInfoFragment);
       // dreamerRegisterFragment=new DreamerRegisterFragment();
        //fragmentHashMap.put(DreamerRegisterFragment.TAG,dreamerRegisterFragment);
        dreamSearchFragment=new DreamSearchFragment();
        fragmentHashMap.put(DreamSearchFragment.TAG, dreamSearchFragment);




        menuAuth=(FrameLayout)findViewById(R.id.menu_auth);
        menuAuth.setOnClickListener(this);
        menuHome=(FrameLayout)findViewById(R.id.menu_home);
        menuHome.setOnClickListener(this);
        menuMore=(FrameLayout)findViewById(R.id.menu_more);
        menuMore.setOnClickListener(this);
        menuMessage=(FrameLayout)findViewById(R.id.menu_message);
        menuMessage.setOnClickListener(this);
        home_iv=(ImageView)findViewById(R.id.image_home);
        message_iv=(ImageView)findViewById(R.id.image_message);
        auth_iv=(ImageView)findViewById(R.id.image_auth);
        more_iv=(ImageView)findViewById(R.id.image_more);

        home_click();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPermission();

        initView();
    }

    private void initPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    Config.REQUEST_CODE_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Config.REQUEST_CODE_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Permission Granted
                    //callDirectly("15574968442");
                } else {
                    // Permission Denied
                    Toast.makeText(this, "请在设置里允许dream使用存储功能,否则会影响缓存功能！", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    //                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action",        }
//
//    }


    //点击图片切换事件
    private void home_click(){
        home_iv.setSelected(true);
        menuHome.setSelected(true);
        auth_iv.setSelected(false);
        menuAuth.setSelected(false);
        message_iv.setSelected(false);
        menuMessage.setSelected(false);
        more_iv.setSelected(false);
        menuMore.setSelected(false);

        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_content,fragmentHashMap.get(WorldCircleFragment.TAG));
        //transaction.addToBackStack(null);
        transaction.commit();


    }

    private void auth_click(){

        home_iv.setSelected(false);
        menuHome.setSelected(false);
        auth_iv.setSelected(true);
        menuAuth.setSelected(true);
        message_iv.setSelected(false);
        menuMessage.setSelected(false);
        more_iv.setSelected(false);
        menuMore.setSelected(false);

        MySharePreference mySharePreference=new MySharePreference(this);
        boolean isLogin=mySharePreference.getBoolean("isLogin",false);
        transaction = getSupportFragmentManager().beginTransaction();
        if(isLogin){
            transaction.replace(R.id.frame_content,fragmentHashMap.get(DreamerInfoFragment.TAG));
        }else{
            transaction.replace(R.id.frame_content,fragmentHashMap.get(DreamerLoginFragment.TAG));
        }
        //transaction.addToBackStack(null);
        transaction.commit();
    }

    private void message_click(){
        home_iv.setSelected(false);
        menuHome.setSelected(false);
        auth_iv.setSelected(false);
        menuAuth.setSelected(false);
        message_iv.setSelected(true);
        menuMessage.setSelected(true);
        more_iv.setSelected(false);
        menuMore.setSelected(false);
    }

    private void more_click(){
        home_iv.setSelected(false);
        menuHome.setSelected(false);
        auth_iv.setSelected(false);
        menuAuth.setSelected(false);
        message_iv.setSelected(false);
        menuMessage.setSelected(false);
        more_iv.setSelected(true);
        menuMore.setSelected(true);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.menu_home:
                home_click();
                break;
            case R.id.menu_auth:
                auth_click();
                break;
            case R.id.menu_more:
                more_click();
                break;
            case R.id.menu_message:
                message_click();
            default:

                break;
        }
    }

    private long exitTime = 0;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {

            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

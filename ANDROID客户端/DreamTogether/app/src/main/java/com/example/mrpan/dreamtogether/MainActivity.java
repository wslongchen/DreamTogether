package com.example.mrpan.dreamtogether;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static HashMap<String,Fragment> fragmentHashMap=null;
    private WorldCircleFragment worldCircleFragment=null;
    private DreamerLoginFragment dreamerLoginFragment=null;
    private DreamerInfoFragment dreamerInfoFragment=null;
    //private DreamerRegisterFragment dreamerRegisterFragment=null;
    private DreamSearchFragment dreamSearchFragment=null;



    private FragmentTransaction transaction=null;
    private PopupWindow popWindow;
    private DisplayMetrics dm;
    ImageView imageView;
    FrameLayout menuHome,menuAuth,menuMore;

    private void initView(){
        fragmentHashMap=new HashMap<>();
        worldCircleFragment=new WorldCircleFragment();
        fragmentHashMap.put(WorldCircleFragment.TAG,worldCircleFragment);
        dreamerLoginFragment=new DreamerLoginFragment();
        fragmentHashMap.put(DreamerLoginFragment.TAG,dreamerLoginFragment);
        dreamerInfoFragment=new DreamerInfoFragment();
        fragmentHashMap.put(DreamerInfoFragment.TAG,dreamerInfoFragment);
       // dreamerRegisterFragment=new DreamerRegisterFragment();
        //fragmentHashMap.put(DreamerRegisterFragment.TAG,dreamerRegisterFragment);
        dreamSearchFragment=new DreamSearchFragment();
        fragmentHashMap.put(DreamSearchFragment.TAG,dreamSearchFragment);


        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_content, worldCircleFragment);
        //transaction.addToBackStack(null);
        transaction.commit();

        imageView=(ImageView)findViewById(R.id.toggle_btn);
        imageView.setOnClickListener(this);

        menuAuth=(FrameLayout)findViewById(R.id.menu_auth);
        menuAuth.setOnClickListener(this);
        menuHome=(FrameLayout)findViewById(R.id.menu_home);
        menuHome.setOnClickListener(this);
        menuMore=(FrameLayout)findViewById(R.id.menu_more);
        menuMore.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HttpHelper httpHelper=HttpHelper.getInstance();
        initView();

    }

    //                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action",        }
//
//    }

    /**
     * 显示PopupWindow弹出菜单
     */
    private void showPopupWindow(View parent) {
        if (popWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(R.layout.popwindow_layout, null);
            dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            // 创建一个PopuWidow对象
            popWindow = new PopupWindow(view, dm.widthPixels, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        // 使其聚集 ，要想监听菜单里控件的事件就必须要调用此方法
        popWindow.setFocusable(true);
        // 设置允许在外点击消失
        popWindow.setOutsideTouchable(true);
        // 设置背景，这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popWindow.setBackgroundDrawable(new BitmapDrawable());
        // PopupWindow的显示及位置设置
        // popWindow.showAtLocation(parent, Gravity.FILL, 0, 0);
        popWindow.showAsDropDown(parent, 0, 0);

        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {

            }
        });

        // 监听触屏事件
        popWindow.setTouchInterceptor(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                // 改变显示的按钮图片为正常状态
                // changeButtonImage();
                popWindow.dismiss();
                return false;
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toggle_btn:
                showPopupWindow(imageView);
                break;
            case R.id.menu_home:
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_content,fragmentHashMap.get(WorldCircleFragment.TAG));
                //transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.menu_auth:
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
                break;
            case R.id.menu_more:

                break;
            default:

                break;
        }
    }

    private long exitTime = 0;

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

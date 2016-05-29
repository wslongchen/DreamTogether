package com.example.mrpan.dreamtogether;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.location.SettingInjectorService;
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

import com.example.mrpan.dreamtogether.fragment.CardDreamFragment;
import com.example.mrpan.dreamtogether.fragment.DreamRadomFragment;
import com.example.mrpan.dreamtogether.fragment.DreamSearchFragment;
import com.example.mrpan.dreamtogether.fragment.DreamerInfoFragment;
import com.example.mrpan.dreamtogether.fragment.DreamerLoginFragment;
import com.example.mrpan.dreamtogether.fragment.DreamerRegisterFragment;
import com.example.mrpan.dreamtogether.fragment.StarFragment;
import com.example.mrpan.dreamtogether.fragment.SystemSettingsFragment;
import com.example.mrpan.dreamtogether.fragment.WorldCircleFragment;
import com.example.mrpan.dreamtogether.http.HttpHelper;
import com.example.mrpan.dreamtogether.utils.Config;
import com.example.mrpan.dreamtogether.utils.DialogUtils;
import com.example.mrpan.dreamtogether.utils.MyLog;
import com.example.mrpan.dreamtogether.utils.MySharePreference;
import com.example.mrpan.dreamtogether.utils.SystemStatusManager;
import com.example.mrpan.dreamtogether.view.loadview.Load2Dialog;
import com.example.mrpan.dreamtogether.xmpp.MySystemService;

import java.util.HashMap;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    public static HashMap<String,Fragment> fragmentHashMap=null;
    private WorldCircleFragment worldCircleFragment=null;
    private DreamerLoginFragment dreamerLoginFragment=null;
    private DreamerInfoFragment dreamerInfoFragment=null;
    //private DreamerRegisterFragment dreamerRegisterFragment=null;
    private DreamSearchFragment dreamSearchFragment=null;
    private SystemSettingsFragment systemSettingsFragment=null;
    private StarFragment starFragment=null;

    private Fragment currentFragment=null;




    private FragmentTransaction transaction=null;
    private PopupWindow popWindow;
    private DisplayMetrics dm;
    ImageView home_iv,auth_iv,star_iv,more_iv;
    FrameLayout menuHome,menuAuth,menuStar,menuMore;

    private void initView(){
        fragmentHashMap=new HashMap<>();
        if(worldCircleFragment==null)
        {
            worldCircleFragment=new WorldCircleFragment();
            fragmentHashMap.put(WorldCircleFragment.TAG,worldCircleFragment);
        }

        if(dreamerInfoFragment==null)
        {
            dreamerLoginFragment=new DreamerLoginFragment();
            fragmentHashMap.put(DreamerLoginFragment.TAG,dreamerLoginFragment);
        }

        if(starFragment==null)
        {
            starFragment=new StarFragment();
            fragmentHashMap.put(StarFragment.TAG,starFragment);
        }

        if(dreamerInfoFragment==null){
            dreamerInfoFragment=new DreamerInfoFragment();
            fragmentHashMap.put(DreamerInfoFragment.TAG, dreamerInfoFragment);
        }
       // dreamerRegisterFragment=new DreamerRegisterFragment();
        //fragmentHashMap.put(DreamerRegisterFragment.TAG,dreamerRegisterFragment);
        if(dreamSearchFragment==null){
            dreamSearchFragment=new DreamSearchFragment();
            fragmentHashMap.put(DreamSearchFragment.TAG, dreamSearchFragment);
        }
        if(systemSettingsFragment==null){
            systemSettingsFragment=new SystemSettingsFragment();
            fragmentHashMap.put(SystemSettingsFragment.TAG,systemSettingsFragment);
        }





        menuAuth=(FrameLayout)findViewById(R.id.menu_auth);
        menuAuth.setOnClickListener(this);
        menuHome=(FrameLayout)findViewById(R.id.menu_home);
        menuHome.setOnClickListener(this);
        menuMore=(FrameLayout)findViewById(R.id.menu_more);
        menuMore.setOnClickListener(this);
        menuStar=(FrameLayout)findViewById(R.id.menu_star);
        menuStar.setOnClickListener(this);
        home_iv=(ImageView)findViewById(R.id.image_home);
        star_iv=(ImageView)findViewById(R.id.image_star);
        auth_iv=(ImageView)findViewById(R.id.image_auth);
        more_iv=(ImageView)findViewById(R.id.image_more);

        home_click();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.activity_main);
        //setTranslucentStatus();
        initPermission();
        super.onCreate(savedInstanceState);
        initView();

        boolean isLogin=new MySharePreference(this).getBoolean("isLogin",false);
        if(isLogin){
            initReceiver();
            Intent intent = new Intent(this, MySystemService.class);
            startService(intent);
        }
    }
    private BroadcastReceiver receiver;
    private void initReceiver() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(Config.ACTION_IS_LOGIN_SUCCESS)){
                    boolean isLoginSuccess=intent.getBooleanExtra("isLoginSuccess", false);
                    if(isLoginSuccess){//登录成功
                        //默认开启声音和震动提醒
//                        if(MyApplication.xmppConnection!=null){
//                            Toast.makeText(context,"登录成功,"+MyApplication.xmppConnection.getUser(),Toast.LENGTH_LONG).show();
//                        }
                        Toast.makeText(context,"登录成功",Toast.LENGTH_LONG).show();
                        //PreferencesUtils.putSharePre(LoginActivity.this, Const.MSG_IS_VOICE, true);
                        //PreferencesUtils.putSharePre(LoginActivity.this, Const.MSG_IS_VIBRATE, true);
//                        Intent intent2=new Intent(mcontext,MainActivity.class);
//                        startActivity(intent2);
//                        finish();
                    }else{
                        Toast.makeText(context,"登录失败，请检您的网络是否正常以及用户名和密码是否正确",Toast.LENGTH_LONG).show();
                        //ToastUtil.showShortToast(mContext, "登录失败，请检您的网络是否正常以及用户名和密码是否正确");
                    }
                }
            }
        };
        //注册广播接收者
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(Config.ACTION_IS_LOGIN_SUCCESS);
        registerReceiver(receiver, mFilter);
    }

    private void initPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.LOCATION_HARDWARE,Manifest.permission.CAMERA,Manifest.permission.CALL_PHONE},
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

    public void switchContent(Fragment from, Fragment to) {
        if(currentFragment==null){
            currentFragment=to;
            transaction = getSupportFragmentManager().beginTransaction();
            if (!to.isAdded()) {
                transaction.add(R.id.frame_content, to).commit();
            }
        }else{
            if (currentFragment != to) {
                currentFragment = to;
                transaction = getSupportFragmentManager().beginTransaction();
                if (!to.isAdded()) {
                    transaction.hide(from).add(R.id.frame_content, to).commit();
                } else {
                    transaction.hide(from).show(to).commit();
                }
            }
        }

    }
    //点击图片切换事件
    private void home_click(){
        home_iv.setSelected(true);
        menuHome.setSelected(true);
        auth_iv.setSelected(false);
        menuAuth.setSelected(false);
        star_iv.setSelected(false);
        menuStar.setSelected(false);
        more_iv.setSelected(false);
        menuMore.setSelected(false);

        switchContent(currentFragment,worldCircleFragment);


    }

    private void auth_click() {

        home_iv.setSelected(false);
        menuHome.setSelected(false);
        auth_iv.setSelected(true);
        menuAuth.setSelected(true);
        star_iv.setSelected(false);
        menuStar.setSelected(false);
        more_iv.setSelected(false);
        menuMore.setSelected(false);

        MySharePreference mySharePreference = new MySharePreference(this);
        boolean isLogin = mySharePreference.getBoolean("isLogin", false);
        if (isLogin) {
            switchContent(currentFragment,dreamerInfoFragment);
        } else {
            switchContent(currentFragment,dreamerLoginFragment);
        }

    }

    private void star_click(){
        home_iv.setSelected(false);
        menuHome.setSelected(false);
        auth_iv.setSelected(false);
        menuAuth.setSelected(false);
        star_iv.setSelected(true);
        menuStar.setSelected(true);
        more_iv.setSelected(false);
        menuMore.setSelected(false);

        switchContent(currentFragment,starFragment);
    }

    private void more_click(){
        home_iv.setSelected(false);
        menuHome.setSelected(false);
        auth_iv.setSelected(false);
        menuAuth.setSelected(false);
        star_iv.setSelected(false);
        menuStar.setSelected(false);
        more_iv.setSelected(true);
        menuMore.setSelected(true);
        switchContent(currentFragment,systemSettingsFragment);
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
            case R.id.menu_star:
                star_click();
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
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
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
                try{
                    MySystemService.getInstance().stopSelf();
                }catch(Exception e){

                }
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

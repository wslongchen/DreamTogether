package com.example.mrpan.dreamtogether.fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mrpan.dreamtogether.OtherActivity;
import com.example.mrpan.dreamtogether.R;
import com.example.mrpan.dreamtogether.adapter.SystemSettingAdapter;
import com.example.mrpan.dreamtogether.entity.Share;
import com.example.mrpan.dreamtogether.entity.User;
import com.example.mrpan.dreamtogether.utils.CacheUtils;
import com.example.mrpan.dreamtogether.utils.Config;
import com.example.mrpan.dreamtogether.utils.DateUtils;
import com.example.mrpan.dreamtogether.utils.MySharePreference;
import com.example.mrpan.dreamtogether.view.CustomDialog;
import com.example.mrpan.dreamtogether.view.NoScrollGridView;
import com.example.mrpan.dreamtogether.view.SharePopupWindows;
import com.example.mrpan.dreamtogether.view.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mrpan on 16/4/27.
 */
public class SystemSettingsFragment extends Fragment implements View.OnClickListener{

    public final static String TAG="SystemSettings";

    private View currentView;

    private Context context;

    private TitleBar titleBar=null;

    private NoScrollGridView setting_grid;

    private TextView total_use_day,total_use_cache,total_use_x;

    private Button exit;

    final public static int REQUEST_CODE_ASK_CALL_PHONE = 123;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView=inflater.inflate(R.layout.system_settings_fragment,container,false);
        ViewGroup viewGroup= (ViewGroup) currentView.getParent();
        if(viewGroup!=null){
            viewGroup.removeView(currentView);
        }
        context=getActivity();

        initView();

        return currentView;

    }

    private void initView(){

        total_use_cache=(TextView)currentView.findViewById(R.id.total_user_cache);
        total_use_day=(TextView)currentView.findViewById(R.id.total_user_day);
        total_use_x=(TextView)currentView.findViewById(R.id.total_use_x);
        exit=(Button)currentView.findViewById(R.id.setting_out);
        exit.setOnClickListener(this);

        setting_grid=(NoScrollGridView)currentView.findViewById(R.id.setting_grid);
        ArrayList<HashMap<String,Object>> menus=new ArrayList<>();
        HashMap<String,Object> menu1=new HashMap<>();
        menu1.put("image", R.mipmap.gonggao);
        menu1.put("text", "公告通知");
        menus.add(menu1);
        HashMap<String,Object> menu2=new HashMap<>();
        menu2.put("image", R.mipmap.help);
        menu2.put("text", "帮助中心");
        menus.add(menu2);
        HashMap<String,Object> menu3=new HashMap<>();
        menu3.put("image", R.mipmap.lianxi);
        menu3.put("text", "联系我们");
        menus.add(menu3);
        HashMap<String,Object> menu4=new HashMap<>();
        menu4.put("image", R.mipmap.guli);
        menu4.put("text", "鼓励我们");
        menus.add(menu4);
        HashMap<String,Object> menu5=new HashMap<>();
        menu5.put("image", R.mipmap.guanyu);
        menu5.put("text", "关于我们");
        menus.add(menu5);
        HashMap<String,Object> menu6=new HashMap<>();
        menu6.put("image", R.mipmap.share);
        menu6.put("text", "分享");
        menus.add(menu6);




        SystemSettingAdapter adapter=new SystemSettingAdapter(context,menus);
        setting_grid.setAdapter(adapter);
        setting_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        final CustomDialog.Builder builder = new CustomDialog.Builder(context);
                        builder.setMessage("是否拨打客服电话?");
                        builder.setTitle("联系我们");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                onCall("15574968442");
                                dialog.dismiss();
                            }
                        });

                        builder.setNegativeButton("取消",
                                new android.content.DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                        builder.create().show();
                        break;
                    case 3:
                        break;
                    case 4:
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putInt("type", Config.BROWSER_TYPE);
                        intent.putExtras(bundle);
                        intent.setClass(context, OtherActivity.class);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.top_in, R.anim.top_out);
                        break;
                    case 5:
                        Share share = new Share();
                        share.setAPP_NAME("梦想");
                        share.setSUMMARY("test");
                        share.setTITLE("test");
                        share.setTARGET_URL("http://www.mrpann.com");
                        new SharePopupWindows(context, currentView, 6, share);
                        break;
                    default:
                        break;
                }
            }
        });


        titleBar=(TitleBar)currentView.findViewById(R.id.top_bar);
        titleBar.showCenterTitle("系统");
        MySharePreference mySharePreference=new MySharePreference(context);
        boolean isLogin = mySharePreference.getBoolean("isLogin", false);
        if(isLogin){
            User user = (User) CacheUtils.readHttpCache(Config.DIR_CACHE_PATH, "user_info");
            if(user!=null){
                total_use_day.setText(DateUtils.getDays(user.getUser_registered()));
            }
        }else{
            total_use_day.setText("请登录");
        }
        total_use_cache.setText(CacheUtils.getHttpCacheSize(context));
//        about=(TextView)currentView.findViewById(R.id.item_about);
//        about.setOnClickListener(this);
    }

    public void onCall(String mobile){
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE);
            if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE_ASK_CALL_PHONE);
                return;
            }else{
                //上面已经写好的拨号方法
                callDirectly(mobile);
            }
        } else {
            //上面已经写好的拨号方法
            callDirectly(mobile);
        }
    }

    private void callDirectly(String mobile){
        Intent intent = new Intent();
        intent.setAction("android.intent.action.CALL");
        intent.setData(Uri.parse("tel:" + mobile));
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.item_about:
//                Intent intent=new Intent();
//                Bundle bundle=new Bundle();
//                bundle.putInt("type", Config.BROWSER_TYPE);
//                intent.putExtras(bundle);
//                intent.setClass(context, OtherActivity.class);
//                startActivity(intent);
//                getActivity().overridePendingTransition(R.anim.top_in, R.anim.top_out);
//                break;
            case R.id.setting_out:
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("是否退出程序?");
                builder.setTitle("提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().finish();
                    }
                });

                builder.setNegativeButton("取消",
                        new android.content.DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                builder.create().show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_CALL_PHONE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callDirectly("15574968442");
                } else {
                    // Permission Denied
                    Toast.makeText(getActivity(), "CALL_PHONE Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}

package com.example.mrpan.dreamtogether.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.example.mrpan.dreamtogether.OtherActivity;
import com.example.mrpan.dreamtogether.R;
import com.example.mrpan.dreamtogether.entity.Dream;
import com.example.mrpan.dreamtogether.entity.Location;
import com.example.mrpan.dreamtogether.entity.Meta;
import com.example.mrpan.dreamtogether.entity.User;
import com.example.mrpan.dreamtogether.http.HttpHelper;
import com.example.mrpan.dreamtogether.http.HttpResponseCallBack;
import com.example.mrpan.dreamtogether.utils.BitmapUtils;
import com.example.mrpan.dreamtogether.utils.Config;
import com.example.mrpan.dreamtogether.utils.DateUtils;
import com.example.mrpan.dreamtogether.utils.DialogUtils;
import com.example.mrpan.dreamtogether.adapter.DreamPostGridAdapter;
import com.example.mrpan.dreamtogether.utils.FileUtils;
import com.example.mrpan.dreamtogether.utils.GsonUtils;
import com.example.mrpan.dreamtogether.utils.MyLog;
import com.example.mrpan.dreamtogether.utils.OtherUtils;
import com.example.mrpan.dreamtogether.utils.RegexUtils;
import com.example.mrpan.dreamtogether.view.TitleBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrpan on 16/3/30.
 */
public class DreamPostFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "DreamPost";

    private View currentView = null;
    private Context context = null;

    private GridView noScrollgridview;
    private DreamPostGridAdapter adapter;

    private TitleBar titleBar;

    private String path = "";

    private EditText dream_content;

    private TextView dream_location;

    private PopupWindow popWindow;

    private FragmentTransaction transaction;

    private Dialog dialog=null;

    private int UserID;

    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    private Location mLocation=new Location();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.dream_post_fragment, container, false);
        ViewGroup viewGroup = (ViewGroup) currentView.getParent();
        if (viewGroup != null) {
            viewGroup.removeView(currentView);
        }
        context = getActivity();
        mLocationClient = new LocationClient(context);//声明LocationClient类
        initLocation();
        mLocationClient.registerLocationListener( myListener );
        Init();
        mLocationClient.start();
        return currentView;
    }

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        //int span=1000;
        //option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(false);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());// 单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            List<Poi> list = location.getPoiList();// POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            mLocation.setAddr(location.getAddrStr());
            mLocation.setLatitude(location.getLatitude() + "");
            mLocation.setLontitude(location.getLongitude() + "");
            dream_location.setText(location.getAddrStr());
        }
    }

    public void Init() {
        transaction=getFragmentManager().beginTransaction();
        dream_location=(TextView)currentView.findViewById(R.id.dream_post_loacation);
        titleBar = (TitleBar) currentView.findViewById(R.id.top_bar);
        titleBar.showLeftStrAndRightStr("发表梦想", "取消","发表", this, this);
        noScrollgridview = (GridView) currentView.findViewById(R.id.dream_post_scrollgridview);
        noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new DreamPostGridAdapter(context);
        loading();
        noScrollgridview.setAdapter(adapter);
        noScrollgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (arg2 == BitmapUtils.drr.size()) {
                    new PopupWindows(context, noScrollgridview);
                    backgroundAlpha(0.7f);
                } else {

                    Bundle bundle = new Bundle();
                    bundle.putInt("ID", arg2);
                    transaction=getFragmentManager().beginTransaction();
                    transaction.replace(R.id.other_layout, OtherActivity.fragmentHashMap.get(PhotoFragment.TAG));
                    ((PhotoFragment)OtherActivity.fragmentHashMap.get(PhotoFragment.TAG)).setArguments(bundle);
                    //transaction.setCustomAnimations(R.anim.left_in, R.anim.left_out, R.anim.right_in, R.anim.right_out);
                    //transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });

        dream_content=(EditText)currentView.findViewById(R.id.dream_post_content);
        dream_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(s.length()==0&&s.equals("")){
                    titleBar.setRightStrEnable(false);
                }
                else
                {
                    titleBar.setRightStrEnable(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==0 && s.equals("")){
                    titleBar.setRightStrEnable(false);
                }
                else
                {
                    titleBar.setRightStrEnable(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==0 &&s.equals("")){
                    titleBar.setRightStrEnable(false);
                }
                else
                {
                    titleBar.setRightStrEnable(true);
                }
            }
        });
//        activity_selectimg_send = (TextView) currentView.findViewById(R.id);
//        activity_selectimg_send.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View v) {
////                List<String> list = new ArrayList<String>();
////                for (int i = 0; i < Bimp.drr.size(); i++) {
////                    String Str = Bimp.drr.get(i).substring(
////                            Bimp.drr.get(i).lastIndexOf("/") + 1,
////                            Bimp.drr.get(i).lastIndexOf("."));
////                    list.add(FileUtils.SDPATH+Str+".JPEG");
//                }
//                // 高清的压缩图片全部就在  list 路径里面了
//                // 高清的压缩过的 bmp 对象  都在 Bimp.bmp里面
//                // 完成上传服务器后 .........
//                //FileUtils.deleteDir();
//
//        });



        dream_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLocationClient.requestLocation();
            }
        });
    }



    @Override
    public void onResume() {
        super.onResume();
        MyLog.i("post", "resume");
       // loading();
        Message message = new Message();
        message.arg1 = 4;
        myHander.sendMessage(message);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        MyLog.i("destroy", "cccc");
        BitmapUtils.bmp.clear();
        BitmapUtils.drr.clear();
        BitmapUtils.max=0;
        if (mLocationClient != null && mLocationClient.isStarted()){
            mLocationClient.stop();
            mLocationClient = null;
        }
        
    }


    public class PopupWindows extends PopupWindow {

        public PopupWindows(Context mContext, View parent) {

            final View view = View
                    .inflate(mContext, R.layout.popwindow_dream_select_item, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.fade_ins));
            LinearLayout ll_popup = (LinearLayout) view
                    .findViewById(R.id.dream_post_img_popup);
            ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.push_bottom_in_2));
            //setOutsideTouchable(true);
            setContentView(view);
            setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);



            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            update();


            final Button bt1 = (Button) view
                    .findViewById(R.id.dream_item_popupwindows_camera);
            Button bt2 = (Button) view
                    .findViewById(R.id.dream_item_popupwindows_Photo);
            Button bt3 = (Button) view
                    .findViewById(R.id.dream_item_popupwindows_cancel);
            bt1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    onPhoto();
                    //onCall("15574968442");
                    dismiss();
                }
            });
            bt2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    transaction = getFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.anim.left_in, R.anim.left_out, R.anim.right_in, R.anim.right_out);
                    transaction.replace(R.id.other_layout, OtherActivity.fragmentHashMap.get(PicSelectFragment.TAG));
                    transaction.addToBackStack(null);
                    transaction.commit();
//                    Intent intent = new Intent(Intent.ACTION_PICK);
//                    intent.setType("image/*");//相片类型
//                    startActivityForResult(intent, 7);
                    dismiss();
                }
            });
            bt3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dismiss();
                }
            });

            setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss() {
                    backgroundAlpha(1f);
                }
            });

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_CALL_PHONE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    photo();
                } else {
                    // Permission Denied
                    Toast.makeText(getActivity(), "Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }



    public void onPhoto() {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_ASK_CALL_PHONE);
                return;
            } else {
                photo();
            }
        } else {
            photo();
        }
    }

    //拍照
    public void photo() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Config.DIR_IMAGE_PATH+"take/", String.valueOf(System.currentTimeMillis())
                + ".jpg");
        path = file.getPath();
        Uri imageUri = Uri.fromFile(file);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        openCameraIntent.putExtra(path, file);
        startActivityForResult(openCameraIntent, Config.TAKE_PICTURE);
    }

    final public static int REQUEST_CODE_ASK_CALL_PHONE = 123;



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Config.TAKE_PICTURE:
//                BitmapUtils.max+=1;

                if (BitmapUtils.drr.size() < 9 && resultCode == -1) {
                    BitmapUtils.drr.add(path);
                }
                loading();
                Message message = new Message();
                message.arg1 = 4;
                myHander.sendMessage(message);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.titleBarRightStr:
                String content=dream_content.getText().toString();
                if(RegexUtils.checkPassword(content)){
                    dialog = DialogUtils.getLoadDialog(getActivity());
                    dialog.show();
                    Dream dream=new Dream();
                    User user=new User();
                    user.setID(UserID);
                    dream.setPost_author(user);
                    dream.setPost_titile("none");
                    dream.setPost_content(content);
                    dream.setPost_password("null");
                    dream.setPost_guid("null");
                    dream.setPost_date(DateUtils.getCurrentTimeStr());
                    dream.setPost_comment_count("0");
                    dream.setPost_comment_status("0");
                    dream.setPost_type("0");
                    dream.setPost_status("0");
                    List<Meta> metas=new ArrayList<>();
                    Meta meta=null;
                    meta=new Meta();
                    meta.setMeta_key("hot_info");
                    meta.setMeta_value("1");
                    metas.add(meta);
                    meta=new Meta();
                    meta.setMeta_key("device_info");
                    meta.setMeta_value(OtherUtils.getDeviceInfo());
                    metas.add(meta);
                    meta=new Meta();
                    meta.setMeta_key("location");

                    meta.setMeta_value(GsonUtils.getJsonStr(mLocation));
                    metas.add(meta);
                    dream.setMetas(metas);
                    List<String> list = new ArrayList<String>();
                for (int i = 0; i < BitmapUtils.drr.size(); i++) {
                    String Str = BitmapUtils.drr.get(i).substring(
                            BitmapUtils.drr.get(i).lastIndexOf("/") + 1,
                            BitmapUtils.drr.get(i).lastIndexOf("."));
                    list.add(Config.DIR_IMAGE_PATH+Str+".JPEG");
                }
                    if(list.size()>0){
                        HttpHelper.getInstance().asyHttpPostRequest(Config.CREATE_DREAM_WITH_IMG, OtherUtils.DreamToMap(dream,"postDreamImg"), list,new DreamPostHttpResponseCallBack(2));
                    }else {
                        HttpHelper.getInstance().asyHttpPostRequest(Config.CREATE_DREAM, OtherUtils.DreamToNameValuePair(dream,"publishDream"), new DreamPostHttpResponseCallBack(1));
                    }

                }else {
                    Toast.makeText(context,"不能为空！",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.titleBarLeftStr:
                getActivity().finish();
                break;
            default:
                break;
        }
    }


    public void loading() {
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    if (BitmapUtils.max == BitmapUtils.drr.size()) {
                        Message message = new Message();
                        message.arg1 = 4;
                        myHander.sendMessage(message);
                        break;
                    } else {
                        try {
                            String path = BitmapUtils.drr.get(BitmapUtils.max);
                            Bitmap bm = BitmapUtils.revitionImageSize(path);
                            System.out.println(path);
//                            if(BitmapUtils.bmp.contains(bm)){
//
//                            }
//                            BitmapUtils.bmp.add(bm);
//                            String newStr = path.substring(
//                                    path.lastIndexOf("/") + 1,
//                                    path.lastIndexOf("."));
//                            FileUtils.saveBitmap(bm, newStr,Config.DIR_IMAGE_PATH);
//                            BitmapUtils.max += 1;
                            if (!BitmapUtils.bmp.contains(bm)) {
                                BitmapUtils.bmp.add(bm);
                                String newStr = path.substring(
                                        path.lastIndexOf("/") + 1,
                                        path.lastIndexOf("."));
                                FileUtils.saveBitmap(bm, newStr, Config.DIR_IMAGE_PATH);
                                BitmapUtils.max += 1;
                            }
                            Message message = new Message();
                            message.arg1 = 4;
                            myHander.sendMessage(message);
                        } catch (IOException e) {

                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    public void loading2(){
        new Thread(new Runnable() {
            public void run() {
                try {
                    for (int i = 0; i < BitmapUtils.drr.size(); i++) {
                        String path = BitmapUtils.drr.get(BitmapUtils.max);
                        Bitmap bm = BitmapUtils.revitionImageSize(path);
                        System.out.println(path);
                        if (!BitmapUtils.bmp.contains(bm)) {
                            BitmapUtils.bmp.add(bm);
                            String newStr = path.substring(
                                    path.lastIndexOf("/") + 1,
                                    path.lastIndexOf("."));
                            FileUtils.saveBitmap(bm, newStr, Config.DIR_IMAGE_PATH);
                        }
                    }
                    Message message = new Message();
                    message.arg1 = 4;
                    myHander.sendMessage(message);
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }

    Handler myHander=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1){
                case Config.HTTP_REQUEST_SUCCESS:
                    switch (msg.arg2){
                        case 1:
                            if(msg.obj!=null){
                                int ret=0;
                                MyLog.i(TAG,msg.obj.toString());
                                try {
                                    JSONObject jsonObject = new JSONObject(msg.obj.toString().replace("\uFEFF", ""));
                                    ret = jsonObject.getInt("ret");
                                    if (ret == Config.RESULT_RET_SUCCESS) {
                                        Toast.makeText(context,"发表成功！",Toast.LENGTH_LONG).show();
                                        //Intent intent = new Intent(context, WorldCircleFragment.class);
                                        //startActivityForResult(intent, Config.RESULT_RET_SUCCESS);
                                        getActivity().finish();
                                        // Toast.makeText(context, "Publish successed!", Toast.LENGTH_LONG).show();
                                    } else
                                    {
                                        Toast.makeText(context, "发表失败！", Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            break;
                        case 2:
                            if(msg.obj!=null){
                                int ret=0;
                                try {
                                    JSONObject jsonObject = new JSONObject(msg.obj.toString().replace("\uFEFF", ""));
                                    ret = jsonObject.getInt("ret");
                                    if (ret == Config.RESULT_RET_SUCCESS) {
                                        Toast.makeText(context,"发表成功！",Toast.LENGTH_LONG).show();
                                        //Intent intent = new Intent(context, WorldCircleFragment.class);
                                        //startActivityForResult(intent, Config.RESULT_RET_SUCCESS);
                                        getActivity().finish();
                                        // Toast.makeText(context, "Publish successed!", Toast.LENGTH_LONG).show();
                                    } else
                                    {
                                        Toast.makeText(context, "发表失败！", Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            break;
                        default:
                            break;
                    }
                    break;
                case 4:
                    adapter.notifyDataSetChanged();
                    break;
                case Config.HTTP_REQUEST_ERROR:
                    Toast.makeText(context,"联网失败！",Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    class DreamPostHttpResponseCallBack implements HttpResponseCallBack {
        private int flag;

        public DreamPostHttpResponseCallBack(int flag){
            super();
            this.flag=flag;
        }

        @Override
        public void onSuccess(String url, String result) {
            MyLog.i(TAG,result);
            Message message = new Message();
            message.arg1 = Config.HTTP_REQUEST_SUCCESS;
            message.arg2=flag;
            message.obj = result;
            myHander.sendMessage(message);
        }

        @Override
        public void onFailure(int httpResponseCode, int errCode, String err) {
            Message message = new Message();
            message.arg1 = Config.HTTP_REQUEST_ERROR;
            myHander.sendMessage(message);
        }
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }
}

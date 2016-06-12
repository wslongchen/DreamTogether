package com.example.mrpan.dreamtogether.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mrpan.dreamtogether.MainActivity;
import com.example.mrpan.dreamtogether.OtherActivity;
import com.example.mrpan.dreamtogether.R;
import com.example.mrpan.dreamtogether.entity.User;
import com.example.mrpan.dreamtogether.http.HttpHelper;
import com.example.mrpan.dreamtogether.http.HttpResponseCallBack;
import com.example.mrpan.dreamtogether.utils.BitmapUtils;
import com.example.mrpan.dreamtogether.utils.CacheUtils;
import com.example.mrpan.dreamtogether.utils.Config;
import com.example.mrpan.dreamtogether.adapter.UserInfoAdapter;
import com.example.mrpan.dreamtogether.utils.FileUtils;
import com.example.mrpan.dreamtogether.utils.MyLog;
import com.example.mrpan.dreamtogether.utils.MySharePreference;
import com.example.mrpan.dreamtogether.utils.OtherUtils;
import com.example.mrpan.dreamtogether.utils.SystemStatusManager;
import com.example.mrpan.dreamtogether.view.CircleImageView;
import com.example.mrpan.dreamtogether.view.CustomDialog;
import com.example.mrpan.dreamtogether.view.TitleBar;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by mrpan on 16/3/24.
 */
public class DreamerInfoFragment extends Fragment implements AdapterView.OnItemClickListener,View.OnClickListener {

    public final static String TAG = "DreamerInfo";

    private View currentView;

    private TextView userNickname, userInfos,user_head_sign;

    private ImageView userImg, qrImg, takePhoto,user_head_sign_edit;

    private CircleImageView photo;

    private ListView userInfoList;

    private Context context;

    private User user = null;

    private TitleBar titleBar = null;

    private RelativeLayout relativeLayout = null;

    private String path = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //透明状态栏
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        initStatus();

        currentView = inflater.inflate(R.layout.user_info, container, false);
        ViewGroup viewGroup = (ViewGroup) currentView.getParent();
        if (viewGroup != null) {
            viewGroup.removeView(currentView);
        }
        context = getActivity();
        Bundle data = getArguments();
        if (data != null) {
            user = (User) data.getSerializable("data");
        } else {
            user = (User) CacheUtils.readHttpCache(Config.DIR_CACHE_PATH, "user_info");
        }
        initView();

        return currentView;

    }

    private void initStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemStatusManager tintManager = new SystemStatusManager(getActivity());
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarAlpha(0.0f);
            tintManager.setStatusBarTintColor(R.color.dreamTransparent);
        }
    }

    private void setTranslucentStatus(boolean on) {
        Window win = getActivity().getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    private void initView() {

        userInfoList = (ListView) currentView.findViewById(R.id.user_info_list);
        View headerview = View.inflate(context, R.layout.user_info_header, (ViewGroup) currentView.getParent());
        userInfoList.addHeaderView(headerview);
        user_head_sign=(TextView)currentView.findViewById(R.id.user_head_sign);
        user_head_sign_edit=(ImageView)currentView.findViewById(R.id.user_head_sign_edit);
        user_head_sign_edit.setOnClickListener(this);
        relativeLayout = (RelativeLayout) currentView.findViewById(R.id.user_info_head);
        //relativeLayout.setBackground(BitmapUtils.blurImageAmeliorate(BitmapUtils.drawableToBitmap(getResources().getDrawable(R.mipmap.bg_search)), context));
        float scaleFactor = 20;//图片缩放比例；
        float radius = 10;//模糊程度
        //MyLog.i(TAG,headerview.getWidth()+","+headerview.getHeight());

        Bitmap overlay = BitmapUtils.drawableToBitmap(getResources().getDrawable(R.mipmap.bg_search));
        Bitmap bg= ImageLoader.getInstance().loadImageSync("http://"+user.getUser_img());


        relativeLayout.setOnClickListener(this);
        titleBar = (TitleBar) currentView.findViewById(R.id.top_bar);
        // titleBar.setBgColor(R.color.dreamBlack);
        titleBar.showRight("我的梦想", R.drawable.btn_post, this);
        titleBar.setBgColor(R.color.dreamTransparent);
        titleBar.setBackgroundColor(getResources().getColor(R.color.dreamTransparent));

        userNickname = (TextView) currentView.findViewById(R.id.user_nickname2);
        userImg = (ImageView) currentView.findViewById(R.id.userImg);
        qrImg = (ImageView) currentView.findViewById(R.id.qrImg);
        qrImg.setOnClickListener(this);

        photo = (CircleImageView) currentView.findViewById(R.id.user_head_photo);
        //photo.setImageResource(R.mipmap.bg_search);
        if(bg!=null){
            relativeLayout.setBackground(BitmapUtils.bitmapTodrawable(bg));
            // overlay = BitmapUtils.doBlur(overlay, (int) radius, true);

            photo.setImageBitmap(bg);
        }else{
            relativeLayout.setBackground(getResources().getDrawable(R.mipmap.bg_search));
        }

        takePhoto = (ImageView) currentView.findViewById(R.id.user_head_takephoto);
        takePhoto.setOnClickListener(this);
//        //takePhoto.setImageResource(R.mipmap.mfy);
//       // takePhoto.setBackgroundColor(getResources().getColor(R.color.dreamWhite));

        if (user != null) {
            userNickname.setText(user.getUser_nickname());
            ImageLoader.getInstance().displayImage("http://"+user.getUser_img(),userImg);
            user_head_sign.setText(user.getUser_display_name());
        }

        List<HashMap<String, Object>> datas = new ArrayList<>();
        HashMap<String, Object> data = null;
        data = new HashMap<>();
        data.put("isNull", true);
        datas.add(data);
        data = new HashMap<>();
        data.put("isNull", false);
        data.put("menuImg", R.mipmap.dreamer_qr);
        data.put("menuText", "我的二维码");
        data.put("menu", "menu0");
        datas.add(data);
        data = new HashMap<>();
        data.put("isNull", false);
        data.put("menuImg", R.mipmap.licheng);
        data.put("menuText", "我的梦想历程");
        data.put("menu", "menu1");
        datas.add(data);
        data = new HashMap<>();
        data.put("isNull", false);
        data.put("menuImg", R.mipmap.new_message);
        data.put("menuText", "我的梦想消息");
        data.put("menu", "menu2");
        datas.add(data);
        data = new HashMap<>();
        data.put("isNull", true);
        datas.add(data);
        data = new HashMap<>();
        data.put("isNull", false);
        data.put("menuImg", R.mipmap.tuichu);
        data.put("menuText", "退出登录");
        data.put("menu", "menuExit");
        datas.add(data);
        data = new HashMap<>();
        data.put("isNull", true);
        datas.add(data);

        //View heardview=View.inflate(context,R.layout.user_info_header,null);
        //userInfoList.addHeaderView(heardview);
        UserInfoAdapter infoAdapter = new UserInfoAdapter(datas, context);
        userInfoList.setAdapter(infoAdapter);
        userInfoList.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ListView listView = (ListView) parent;
        HashMap<String, Object> map = (HashMap<String, Object>) listView.getItemAtPosition(position);
        if (map.get("isNull").equals(true))
            return;
        Intent intent = null;
        Bundle bundle = null;
        switch (map.get("menu").toString()) {
            case "menu0":
                intent = new Intent();
                bundle = new Bundle();
                bundle.putInt("type", Config.DREAM_QRCODE);
                bundle.putSerializable("data", user);
                intent.putExtras(bundle);
                intent.setClass(context, OtherActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
                break;
            case "menu1":
                intent = new Intent();
                bundle = new Bundle();
                bundle.putInt("type", Config.TIMELINE_TYPE);
                bundle.putInt("data", user.getID());
                intent.putExtras(bundle);
                intent.setClass(context, OtherActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
                break;
            case "menu2":
                intent = new Intent();
                bundle = new Bundle();
                bundle.putInt("type", Config.MESSAGE_TYPE);
                intent.setClass(context, OtherActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
                break;
            case "menuExit":
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("是否退出登陆?");
                builder.setTitle("提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        MySharePreference mySharePreference = new MySharePreference(context);
                        mySharePreference.commitBoolean("isLogin", false);
                        mySharePreference.commitString("username", "");
                        mySharePreference.commitString("userpassword", "");
                        android.support.v4.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_content, MainActivity.fragmentHashMap.get(DreamerLoginFragment.TAG));
                        //transaction.addToBackStack(null);
                        transaction.commit();
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
//                MySharePreference mySharePreference = new MySharePreference(context);
//                mySharePreference.commitBoolean("isLogin", false);
//                mySharePreference.commitString("username", "");
//                mySharePreference.commitString("userpassword", "");
//                android.support.v4.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.frame_content, MainActivity.fragmentHashMap.get(DreamerLoginFragment.TAG));
//                //transaction.addToBackStack(null);
//                transaction.commit();
                break;
            default:
                break;
        }

    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        Bundle bundle = null;
        switch (v.getId()) {
            case R.id.qrImg:
                //Toast.makeText(context,"QR",Toast.LENGTH_LONG).show();
                break;
            case R.id.titleBarRightImage:
                intent = new Intent();
                bundle = new Bundle();
                bundle.putInt("type", Config.POST_TYPE);
                bundle.putInt("data", user.getID());
                intent.putExtras(bundle);
                intent.setClass(context, OtherActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.top_in, R.anim.top_out);
                break;
            case R.id.user_info:
//                Toast.makeText(context,"dddd",Toast.LENGTH_LONG).show();
                break;
            case R.id.user_head_takephoto:
                new PopupWindows(context, currentView);
                backgroundAlpha(0.7f);
                break;
            case R.id.user_head_sign_edit:
//                intent = new Intent();
//                bundle = new Bundle();
//                bundle.putInt("type", Config.POST_TYPE);
//                bundle.putInt("data", user.getID());
//                intent.putExtras(bundle);
//                intent.setClass(context, OtherActivity.class);
//                startActivity(intent);
//                getActivity().overridePendingTransition(R.anim.top_in, R.anim.top_out);
                break;
            default:
                break;
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



            showAtLocation(parent, Gravity.CENTER, 0, 0);
            update();


            final Button bt1 = (Button) view
                    .findViewById(R.id.dream_item_popupwindows_camera);
            Button bt2 = (Button) view
                    .findViewById(R.id.dream_item_popupwindows_Photo);
            Button bt3 = (Button) view
                    .findViewById(R.id.dream_item_popupwindows_cancel);
            bt3.setVisibility(View.GONE);
            bt1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    onPhoto();
                    //onCall("15574968442");
                    dismiss();
                }
            });
            bt2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");//相片类型
                    startActivityForResult(intent, 7);
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


    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }

    public void onPhoto() {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_ASK_CALL_PHONE);
                return;
            } else {
                //上面已经写好的拨号方法
                photo();
            }
        } else {
            //上面已经写好的拨号方法
            photo();
        }
    }

    //拍照
    public void photo() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Config.DIR_IMAGE_PATH + "take/", String.valueOf(System.currentTimeMillis())
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
        MyLog.i("CODE", Activity.RESULT_OK+"");
        List<String> list;
        switch (requestCode) {

            case Config.TAKE_PICTURE:
//                BitmapUtils.max+=1;
                try {
                    Bitmap bm = BitmapUtils.revitionImageSize(path);
//                ImageView imageView = (ImageView) currentView.findViewById(R.id.iv01);
                    photo.setImageBitmap(bm);
                    relativeLayout.setBackground(BitmapUtils.bitmapTodrawable(bm));
//                /* 将Bitmap设定到ImageView */
//                imageView.setImageBitmap(bitmap);
                    String Str = path.substring(
                            path.lastIndexOf("/") + 1,
                            path.lastIndexOf("."));
                    list = new ArrayList<String>();
                    MyLog.i("path", path);
                    list.add(Config.DIR_IMAGE_PATH+"take/"+Str+".JPEG");
                    HttpHelper.getInstance().asyHttpPostRequest(Config.UPDATE_USER_IMG, null, list, new DreamInfoHttpResponseCallBack(1));
                } catch (Exception e) {
                    MyLog.i("Exception", e.getMessage());
                }
                break;
            case 7:
                Uri uri = data.getData();

                ContentResolver cr = context.getContentResolver();

                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));

                    BitmapUtils.saveBitmapToSDCard(bitmap, Config.DIR_IMAGE_PATH + "take/", "take");
//                ImageView imageView = (ImageView) currentView.findViewById(R.id.iv01);

                    list = new ArrayList<String>();
                    path="take";
                    Bitmap bm = BitmapUtils.revitionImageSize(Config.DIR_IMAGE_PATH + "take/" + path + ".jpg");
                    BitmapUtils.saveBitmapToSDCard(bm, Config.DIR_IMAGE_PATH + "take/", "take");
                    relativeLayout.setBackground(BitmapUtils.bitmapTodrawable(bm));
                    photo.setImageBitmap(bm);
                    list = new ArrayList<String>();
                    list.add(Config.DIR_IMAGE_PATH + "take/" + path + ".jpg");
                    MyLog.i("path", path);
                    Map<String,String> map=new HashMap<>();
                    map.put("id",String.valueOf(user.getID()));
                    HttpHelper.getInstance().asyHttpPostRequest(Config.UPDATE_USER_IMG, map, list, new DreamInfoHttpResponseCallBack(1));
//                /* 将Bitmap设定到ImageView */
//                imageView.setImageBitmap(bitmap);
                } catch (Exception e) {
                    MyLog.i("Exception", e.getMessage());
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onResume() {
        super.onResume();
        user = (User) CacheUtils.readHttpCache(Config.DIR_CACHE_PATH, "user_info");
        if (user != null) {
            userNickname.setText(user.getUser_nickname());
            user_head_sign.setText(user.getUser_display_name());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getFragmentManager().beginTransaction().remove(MainActivity.fragmentHashMap.get(TAG));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getFragmentManager().beginTransaction().remove(MainActivity.fragmentHashMap.get(TAG));
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
                                MyLog.i(TAG, msg.obj.toString());
                                try {
                                    JSONObject jsonObject = new JSONObject(msg.obj.toString().replace("\uFEFF", ""));
                                    ret = jsonObject.getInt("ret");
                                    if (ret == Config.RESULT_RET_SUCCESS) {
                                        Toast.makeText(context,"修改成功！",Toast.LENGTH_LONG).show();

                                        FileUtils.deleteDir(Config.DIR_IMAGE_PATH + "take/");
                                    } else
                                    {
                                        Toast.makeText(context, "修改失败！", Toast.LENGTH_LONG).show();
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
    class DreamInfoHttpResponseCallBack implements HttpResponseCallBack {
        private int flag;

        public DreamInfoHttpResponseCallBack(int flag){
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
}

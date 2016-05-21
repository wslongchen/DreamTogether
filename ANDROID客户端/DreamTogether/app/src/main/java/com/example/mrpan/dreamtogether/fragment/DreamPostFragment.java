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
import android.widget.Toast;

import com.example.mrpan.dreamtogether.OtherActivity;
import com.example.mrpan.dreamtogether.R;
import com.example.mrpan.dreamtogether.entity.Dream;
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

    private PopupWindow popWindow;

    private FragmentTransaction transaction;

    private Dialog dialog=null;

    private int UserID;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.dream_post_fragment, container, false);
        ViewGroup viewGroup = (ViewGroup) currentView.getParent();
        if (viewGroup != null) {
            viewGroup.removeView(currentView);
        }
        context = getActivity();
        Init();
        return currentView;
    }

    public void Init() {
        transaction=getFragmentManager().beginTransaction();;
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
                    transaction.replace(R.id.other_layout, OtherActivity.fragmentHashMap.get(PhotoFragment.TAG));
                    OtherActivity.fragmentHashMap.get(PhotoFragment.TAG).setArguments(bundle);
                    //transaction.setCustomAnimations(R.anim.left_in, R.anim.left_out, R.anim.right_in, R.anim.right_out);
                    transaction.addToBackStack(null);
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
    }


    @Override
    public void onResume() {
        super.onResume();
        MyLog.i("post", "resume");
        Message message = new Message();
        message.what = 1;
        handler.sendMessage(message);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        MyLog.i("destroy", "cccc");
        BitmapUtils.bmp.clear();
        BitmapUtils.drr.clear();
        BitmapUtils.max=0;
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
                    // Permission Granted
                    //callDirectly("15574968442");
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

    private void callDirectly(String mobile){
        Intent intent = new Intent();
        intent.setAction("android.intent.action.CALL");
        intent.setData(Uri.parse("tel:" + mobile));
        context.startActivity(intent);
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
        File file = new File(Config.DIR_IMAGE_PATH+"take/", String.valueOf(System.currentTimeMillis())
                + ".jpg");
        path = file.getPath();
        Uri imageUri = Uri.fromFile(file);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        openCameraIntent.putExtra(path, file);
        startActivityForResult(openCameraIntent, Config.TAKE_PICTURE);
    }

    final public static int REQUEST_CODE_ASK_CALL_PHONE = 123;

    public void onCall(String mobile){
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.CALL_PHONE);
            if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CALL_PHONE},REQUEST_CODE_ASK_CALL_PHONE);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Config.TAKE_PICTURE:
                if (BitmapUtils.drr.size() < 9 && resultCode == -1) {
                    BitmapUtils.drr.add(path);
                }

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
                    dream.setMetas(metas);
                    List<String> list = new ArrayList<String>();
                for (int i = 0; i < BitmapUtils.drr.size(); i++) {
                    String Str = BitmapUtils.drr.get(i).substring(
                            BitmapUtils.drr.get(i).lastIndexOf("/") + 1,
                            BitmapUtils.drr.get(i).lastIndexOf("."));
                    list.add(Config.DIR_IMAGE_PATH+Str+".JPEG");
                }
                    if(list.size()>0){
                        HttpHelper.getInstance().asyHttpPostRequest(Config.CREATE_DREAM_WITH_IMG, OtherUtils.DreamToMap(dream), list,new DreamPostHttpResponseCallBack(2));
                    }else {
                        HttpHelper.getInstance().asyHttpPostRequest(Config.CREATE_DREAM, OtherUtils.DreamToNameValuePair(dream), new DreamPostHttpResponseCallBack(1));
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


        Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    adapter.notifyDataSetChanged();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public void loading() {
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    if (BitmapUtils.max == BitmapUtils.drr.size()) {
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                        break;
                    } else {
                        try {
                            String path = BitmapUtils.drr.get(BitmapUtils.max);
                            Bitmap bm = BitmapUtils.revitionImageSize(path);
                            System.out.println(path);
                            if(BitmapUtils.bmp.contains(bm)){

                            }
                            BitmapUtils.bmp.add(bm);
                            String newStr = path.substring(
                                    path.lastIndexOf("/") + 1,
                                    path.lastIndexOf("."));
                            FileUtils.saveBitmap(bm, newStr,Config.DIR_IMAGE_PATH);
                            BitmapUtils.max += 1;
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
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
                    message.what = 1;
                    handler.sendMessage(message);
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

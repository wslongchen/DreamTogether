package com.example.mrpan.dreamtogether.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.mrpan.dreamtogether.OtherActivity;
import com.example.mrpan.dreamtogether.R;
import com.example.mrpan.dreamtogether.entity.Dream;
import com.example.mrpan.dreamtogether.entity.Share;
import com.example.mrpan.dreamtogether.tecent.TencentUtil;
import com.example.mrpan.dreamtogether.utils.BitmapUtils;
import com.example.mrpan.dreamtogether.utils.Config;
import com.example.mrpan.dreamtogether.utils.FileUtils;
import com.example.mrpan.dreamtogether.utils.MyLog;
import com.example.mrpan.dreamtogether.view.TitleBar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by mrpan on 16/5/24.
 */
public class ShareFragment extends Fragment implements View.OnClickListener{

    public static final String TAG="Share";
    private Context context;
    private View currentView;
    private Dream dream;
    private TitleBar titleBar;


    private ImageView share_image;
    private ScrollView scrollView;
    private LinearLayout s;

    String path;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView=inflater.inflate(R.layout.share_fragment,container,false);
        ViewGroup viewGroup= (ViewGroup) currentView.getParent();
        if(viewGroup!=null){
            viewGroup.removeView(currentView);
        }
        context=getActivity();
        init();
        return currentView;
    }

    private void init(){
        share_image=(ImageView)currentView.findViewById(R.id.share_image);
        titleBar=(TitleBar)currentView.findViewById(R.id.top_bar);
        titleBar.showLeftStrAndRightStr("小记","取消","分享",this,this);
        scrollView=(ScrollView)currentView.findViewById(R.id.share_view);
        s=(LinearLayout)currentView.findViewById(R.id.share_v);
        s.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                s.setFocusable(true);
                s.setFocusableInTouchMode(true);
                s.requestFocus();

                return false;
            }
        });
        share_image.setOnClickListener(this);
        scrollView.setDrawingCacheEnabled(true);

    }
    public static Bitmap getBitmapByView(ScrollView scrollView) {
        int h = 0;
        Bitmap bitmap = null;
        // 获取listView实际高度
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
            //scrollView.getChildAt(i).setBackgroundResource(R.drawable.bg3);
        }
        MyLog.i(TAG, scrollView.getWidth() + ":WIdth,Height:" + scrollView.getChildAt(0).getHeight());
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(scrollView.getWidth(), h,
                Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);
//        // 测试输出
//        FileOutputStream out = null;
//        try {
//            out = new FileOutputStream("/sdcard/screen_test.png");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        try {
//            if (null != out) {
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
//                out.flush();
//                out.close();
//            }
//        } catch (IOException e) {
//            // TODO: handle exception
//        }
        return bitmap;
    }

    private Bitmap getViewBitmap( View view ){

        view.setDrawingCacheEnabled( true );
        view.buildDrawingCache();

        Bitmap bitmap = null;

        try{

            if( null != view.getDrawingCache( ) ){

                bitmap = Bitmap.createBitmap(view.getDrawingCache());

            }else{

                //Bitmap bitmapTmp =( (BitmapDrawable)( getResources( ).getDrawable( R.drawable.syncompdetailcontent_background ) ) ).getBitmap( );

            }

        }catch( OutOfMemoryError e ){

            e.printStackTrace( );

        }finally{

            view.setDrawingCacheEnabled( false );

            view.destroyDrawingCache( );

        }

        return bitmap;

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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1003:
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Config.TAKE_PICTURE:
//                BitmapUtils.max+=1;

                break;
            case Activity.RESULT_OK:
                Uri uri = data.getData();
                MyLog.i("uri", uri.toString());
                ContentResolver cr = context.getContentResolver();
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
//                ImageView imageView = (ImageView) currentView.findViewById(R.id.iv01);
//                /* 将Bitmap设定到ImageView */
//                imageView.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    MyLog.i("Exception", e.getMessage());
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
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

    public void onPhoto() {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1003);
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


    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.titleBarLeftStr:
                getActivity().finish();
                break;
            case R.id.titleBarRightStr:
                TencentUtil tencentUtil=new TencentUtil(getActivity(),context);
                Share share=new Share();
                share.setAPP_NAME("梦想");
                Bitmap bitmap=getViewBitmap(s);
                FileUtils.saveBitmap(bitmap, "share", Config.DIR_IMAGE_PATH);
                share.setSUMMARY("test");
                share.setTITLE("test");
                share.setIMAGE_URL(Config.DIR_IMAGE_PATH+"share.JPEG");
                tencentUtil.shareImage(share);
                break;
            case R.id.share_image:
                new PopupWindows(context, currentView);
                break;
            default:
                break;
        }
    }
}

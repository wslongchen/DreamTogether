package com.example.mrpan.dreamtogether.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

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

    private ScrollView scrollView;
    private LinearLayout s;

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
            default:
                break;
        }
    }
}

package com.example.mrpan.dreamtogether.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mrpan.dreamtogether.OtherActivity;
import com.example.mrpan.dreamtogether.R;
import com.example.mrpan.dreamtogether.entity.Share;
import com.example.mrpan.dreamtogether.utils.BitmapUtils;
import com.example.mrpan.dreamtogether.utils.Config;
import com.example.mrpan.dreamtogether.utils.MakeQRCodeUtils;
import com.example.mrpan.dreamtogether.utils.OtherUtils;
import com.example.mrpan.dreamtogether.view.ActionSheetDialog;
import com.example.mrpan.dreamtogether.view.SharePopupWindows;
import com.example.mrpan.dreamtogether.view.TitleBar;
import com.google.zxing.WriterException;
import com.sina.weibo.sdk.utils.ImageUtils;

import java.io.FileNotFoundException;

/**
 * Created by mrpan on 16/6/10.
 */
public class QRCodeFragment extends Fragment implements View.OnClickListener,ActionSheetDialog.OnSheetItemClickListener {
    public static final String TAG="QRCode";

    private View currentView;

    private ImageView qr_code;

    private Context context;

    private TitleBar topbar;

    private Bitmap qr_1=null,qr_2=null,qr_3=null,current=null;

    private boolean current_qr=true;

    private LinearLayout qr_View;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView=inflater.inflate(R.layout.dreamer_qr_fragment,container,false);
        ViewGroup viewGroup= (ViewGroup) currentView.getParent();
        if(viewGroup!=null){
            viewGroup.removeView(currentView);
        }
        context=getActivity();
        initView();
        //handler.sendEmptyMessage(1);
        return currentView;
    }

    private void initView(){
        topbar=(TitleBar)currentView.findViewById(R.id.top_bar);
        topbar.showLeftStrAndRightStr("梦想", "返回", "分享", this, this);
        qr_code=(ImageView)currentView.findViewById(R.id.personalized_code);
        qr_View=(LinearLayout)currentView.findViewById(R.id.qr_view);
        initOtherQrImage("潘安");
    }
    @Override
    public void onResume() {
        super.onResume();
        //获得控件的宽高
        calculateView("潘安");
        qr_code.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                share();
                return false;
            }
        });
    }

    private void share() {
        ActionSheetDialog actionSheet = new ActionSheetDialog(context)
                .builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("换个样式", ActionSheetDialog.SheetItemColor.Blue, this)
                .addSheetItem("发送给好友", ActionSheetDialog.SheetItemColor.Blue, this)
                .addSheetItem("保存到手机", ActionSheetDialog.SheetItemColor.Blue, this);

        actionSheet.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.titleBarRightStr:
                new SharePopupWindows(context,currentView,0,new Share());
                break;
            case R.id.titleBarLeftStr:
                getActivity().finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(int which) {
        switch (which){
            case 1:
                if(qr_1!=null&&qr_2!=null){
                    if(current_qr){
                        qr_code.setImageBitmap(qr_2);
                        current=qr_2;
                        current_qr=false;

                    }else {
                        qr_code.setImageBitmap(qr_1);
                        current=qr_1;
                        current_qr=true;
                    }
                    Toast.makeText(context,"已更换",Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                sendToFriends();
                break;
            case 3:
                saveLocal();
                Toast.makeText(context, "已保存到手机", Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void saveLocal(){

        Bitmap bitmap=BitmapUtils.getViewBitmap(qr_View);
        BitmapUtils.saveBitmapToSDCard(bitmap,Config.DIR_IMAGE_PATH+"code","qrcode");
        Uri imageUri=  Uri.parse(Config.DIR_IMAGE_PATH+"code/qrcode.jpg");
        // 其次把文件插入到系统图库
        String filename="qrcode.jpg";
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    imageUri.getPath(), filename, null);
            // 最后通知图库更新
            context.sendBroadcast(new Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, imageUri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void sendToFriends() {
        Intent intent=new Intent(Intent.ACTION_SEND);
        Bitmap bitmap=BitmapUtils.getViewBitmap(qr_View);
        BitmapUtils.saveBitmapToSDCard(bitmap,Config.DIR_IMAGE_PATH+"code","qrcode");
        Uri imageUri=  Uri.parse(Config.DIR_IMAGE_PATH+"code/qrcode.jpg");
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, imageUri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, "梦想二维码"));
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1://换个样式初始化
                        initOtherQrImage("潘安");
                    break;
                case 2:
                    if(qr_1!=null&&qr_2!=null){
                        if(current_qr){
                            qr_code.setImageResource(R.mipmap.ic_launcher);
                            current=qr_2;
                            current_qr=false;

                        }else {
                            qr_code.setImageBitmap(qr_1);
                            current=qr_1;
                            current_qr=true;
                        }
                        Toast.makeText(context,"已更换",Toast.LENGTH_SHORT).show();
                    }

                    break;
                default:
                    break;
            }
        }
    };


    private void initOtherQrImage(final String content){
        int height = qr_code.getMeasuredHeight();
        int width = qr_code.getMeasuredWidth();
        Bitmap logo = MakeQRCodeUtils.gainBitmap(context, R.mipmap.ic_launcher);
        Bitmap   background = MakeQRCodeUtils.gainBitmap(context, R.mipmap.bg_search);
        try {
            //获得二维码图片
            qr_2 = MakeQRCodeUtils.makeQRImage(logo,
                    content,
                    width, height);

            //给二维码加背景
            // bitmap = MakeQRCodeUtils.addBackground(bitmap, background);
            //加水印
            //bitmap = MakeQRCodeUtils.composeWatermark(bitmap, markBMP);
            //设置二维码图片
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //设置二维码
    private void calculateView(final String content) {
        final ViewTreeObserver vto = qr_code.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                if (vto.isAlive()) {
                    vto.removeOnPreDrawListener(this);
                }
                int height = qr_code.getMeasuredHeight();
                int width = qr_code.getMeasuredWidth();
                Bitmap logo = MakeQRCodeUtils.gainBitmap(context, R.mipmap.ic_launcher);
                Bitmap   background = MakeQRCodeUtils.gainBitmap(context, R.mipmap.bg_search);
                //Bitmap markBMP = MakeQRCodeUtils.gainBitmap(context, R.mipmap.app_icon);
                try {
                    //获得二维码图片
                    Bitmap bitmap = MakeQRCodeUtils.makeQRImage(
                            content,
                            width, height);
                    //给二维码加背景
                    // bitmap = MakeQRCodeUtils.addBackground(bitmap, background);
                    //加水印
                    //bitmap = MakeQRCodeUtils.composeWatermark(bitmap, markBMP);
                    //设置二维码图片
                    qr_code.setImageBitmap(bitmap);
                    qr_1=bitmap;
                    current=bitmap;
                   // current_qr=0;
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
    }
}

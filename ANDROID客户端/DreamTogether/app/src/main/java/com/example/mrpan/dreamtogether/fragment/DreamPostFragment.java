package com.example.mrpan.dreamtogether.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.example.mrpan.dreamtogether.R;
import com.example.mrpan.dreamtogether.utils.BitmapUtils;
import com.example.mrpan.dreamtogether.utils.Config;
import com.example.mrpan.dreamtogether.utils.DreamPostGridAdapter;
import com.example.mrpan.dreamtogether.view.TitleBar;

import java.io.File;

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
        titleBar = (TitleBar) currentView.findViewById(R.id.top_bar);
        titleBar.showRight("发表梦想", R.mipmap.ic_launcher, this);
        noScrollgridview = (GridView) currentView.findViewById(R.id.dream_post_scrollgridview);
        noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new DreamPostGridAdapter(context);
        adapter.notifyDataSetChanged();
        noScrollgridview.setAdapter(adapter);
        noScrollgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (arg2 == BitmapUtils.bmp.size()) {
                    new PopupWindows(context, noScrollgridview);


                } else {
//                    Intent intent = new Intent(PublishedActivity.this,
//                            PhotoActivity.class);
//                    intent.putExtra("ID", arg2);
//                    startActivity(intent);
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


    public String getString(String s) {
        String path = null;
        if (s == null)
            return "";
        for (int i = s.length() - 1; i > 0; i++) {
            s.charAt(i);
        }
        return path;
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    public class PopupWindows extends PopupWindow {

        public PopupWindows(Context mContext, View parent) {

            View view = View
                    .inflate(mContext, R.layout.popwindow_dream_select_item, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.fade_ins));
            LinearLayout ll_popup = (LinearLayout) view
                    .findViewById(R.id.dream_post_img_popup);
            ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.push_bottom_in_2));

            setWidth(ViewGroup.LayoutParams.FILL_PARENT);
            setHeight(ViewGroup.LayoutParams.FILL_PARENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(true);
            setContentView(view);
            
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            update();

            setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {

                }
            });

            Button bt1 = (Button) view
                    .findViewById(R.id.dream_item_popupwindows_camera);
            Button bt2 = (Button) view
                    .findViewById(R.id.dream_item_popupwindows_Photo);
            Button bt3 = (Button) view
                    .findViewById(R.id.dream_item_popupwindows_cancel);
            bt1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    photo();
                    dismiss();
                }
            });
            bt2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
//                    Intent intent = new Intent(this,
//                            TestPicActivity.class);
//                    startActivity(intent);
                    dismiss();
                }
            });
            bt3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dismiss();
                }
            });

        }
    }

    //拍照
    public void photo() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Config.DIR_IMAGE_PATH, String.valueOf(System.currentTimeMillis())
                + ".jpg");
        path = file.getPath();
        Uri imageUri = Uri.fromFile(file);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(openCameraIntent, Config.TAKE_PICTURE);
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

    }


}

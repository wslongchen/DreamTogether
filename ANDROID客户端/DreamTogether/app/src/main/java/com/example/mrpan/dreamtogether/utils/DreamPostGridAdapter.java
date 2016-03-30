package com.example.mrpan.dreamtogether.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.mrpan.dreamtogether.R;

import java.io.IOException;

/**
 * Created by mrpan on 16/3/30.
 */
@SuppressLint("HandlerLeak")
public class DreamPostGridAdapter extends BaseAdapter {
    private LayoutInflater inflater; // 视图容器
    private int selectedPosition = -1;// 选中的位置
    private boolean shape;
    private Context context;

    public boolean isShape() {
        return shape;
    }

    public void setShape(boolean shape) {
        this.shape = shape;
    }

    public DreamPostGridAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context=context;
    }


    public int getCount() {
        return (BitmapUtils.bmp.size() + 1);
    }

    public Object getItem(int arg0) {

        return null;
    }

    public long getItemId(int arg0) {

        return 0;
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    /**
     * ListView Item设置
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        final int coord = position;
        ViewHolder holder = null;
        if (convertView == null) {

            convertView = inflater.inflate(R.layout.dream_post_item,
                    parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView
                    .findViewById(R.id.item_grida_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position == BitmapUtils.bmp.size()) {
            holder.image.setImageBitmap(BitmapFactory.decodeResource(
                    context.getResources(), R.drawable.icon_addpic_unfocused));
            if (position == 9) {
                holder.image.setVisibility(View.GONE);
            }
        } else {
            holder.image.setImageBitmap(BitmapUtils.bmp.get(position));
        }

        return convertView;
    }

    public class ViewHolder {
        public ImageView image;
    }

//    Handler handler = new Handler() {
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case 1:
//                    adapter.notifyDataSetChanged();
//                    break;
//            }
//            super.handleMessage(msg);
//        }
//    };
//
//    public void loading() {
//        new Thread(new Runnable() {
//            public void run() {
//                while (true) {
//                    if (BitmapUtils.max == BitmapUtils.drr.size()) {
//                        Message message = new Message();
//                        message.what = 1;
//                        handler.sendMessage(message);
//                        break;
//                    } else {
//                        try {
//                            String path = BitmapUtils.drr.get(BitmapUtils.max);
//                            System.out.println(path);
//                            Bitmap bm = BitmapUtils.revitionImageSize(path);
//                            BitmapUtils.bmp.add(bm);
//                            String newStr = path.substring(
//                                    path.lastIndexOf("/") + 1,
//                                    path.lastIndexOf("."));
//                            //FileUtils.saveBitmap(bm, "" + newStr);
//                            BitmapUtils.max += 1;
//                            Message message = new Message();
//                            message.what = 1;
//                            handler.sendMessage(message);
//                        } catch (IOException e) {
//
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//        }).start();
//    }
}

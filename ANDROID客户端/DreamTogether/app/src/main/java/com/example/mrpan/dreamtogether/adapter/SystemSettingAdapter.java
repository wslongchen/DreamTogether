package com.example.mrpan.dreamtogether.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mrpan.dreamtogether.R;
import com.example.mrpan.dreamtogether.entity.ImageItem;
import com.example.mrpan.dreamtogether.utils.BitmapCache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mrpan on 16/5/27.
 */
public class SystemSettingAdapter extends BaseAdapter{

    final String TAG = getClass().getSimpleName();
    List<HashMap<String,Object>> dataList;
    public Map<String, String> map = new HashMap<String, String>();

    private Context context;



    public SystemSettingAdapter(Context context, List<HashMap<String,Object>> list) {
        this.context = context;
        dataList = list;
    }

    @Override
    public int getCount() {
        int count = 0;
        if (dataList != null) {
            count = dataList.size();
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    class Holder {
        private ImageView iv;
        private TextView text;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = View.inflate(context, R.layout.system_settings_items, null);
            holder.iv = (ImageView) convertView.findViewById(R.id.setting_image);
            holder.text = (TextView) convertView
                    .findViewById(R.id.setting_text);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        final HashMap<String,Object> item = dataList.get(position);

        holder.iv.setImageResource((int)item.get("image"));
        holder.text.setText(item.get("text").toString());
        return convertView;
    }
}


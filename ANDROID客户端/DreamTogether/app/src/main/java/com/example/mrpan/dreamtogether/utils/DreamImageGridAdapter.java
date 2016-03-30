package com.example.mrpan.dreamtogether.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.mrpan.dreamtogether.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by mrpan on 16/3/30.
 */
public class DreamImageGridAdapter extends BaseAdapter {

    private List<HashMap<String,Object>> datas;
    private Context context;

    public DreamImageGridAdapter(Context context,List<HashMap<String,Object>> datas){
        this.datas=datas;
        this.context=context;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GridViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new GridViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dream_img_item,
                    parent, false);
            viewHolder.dream_img = (ImageView) convertView
                    .findViewById(R.id.iv_dream_img);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (GridViewHolder) convertView.getTag();
        }
        String url = datas.get(position).get("url").toString();
        //ImageLoader.getInstance().displayImage(url, viewHolder.dream_img);
        return convertView;
    }

    class GridViewHolder{
        private ImageView dream_img;
    }
}

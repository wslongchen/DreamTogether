package com.example.mrpan.dreamtogether.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mrpan.dreamtogether.R;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by mrpan on 16/3/25.
 */
public class UserInfoAdapter extends BaseAdapter {

    private List<HashMap<String,Object>> datas;
    private Context context;


    public UserInfoAdapter(List<HashMap<String,Object>> datas,Context context){
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

        UserViewHolder viewHolder = null;

        if(convertView==null){
            viewHolder=new UserViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.user_info_item, parent, false);
            viewHolder.menuImage= (ImageView) convertView.findViewById(R.id.menuImg);
            viewHolder.menuTitle=(TextView)convertView.findViewById(R.id.menuTitle);
            viewHolder.menu_layout=(RelativeLayout)convertView.findViewById(R.id.user_menu_layout);
            viewHolder.menu_line=(LinearLayout)convertView.findViewById(R.id.user_info_line);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (UserViewHolder) convertView.getTag();
        }
        HashMap<String,Object> menu=datas.get(position);
        if(menu.get("isNull").equals(true)){
            viewHolder.menu_layout.setVisibility(View.GONE);
            viewHolder.menu_line.setVisibility(View.VISIBLE);
        }
        else
        {
            viewHolder.menu_line.setVisibility(View.GONE);
            viewHolder.menu_layout.setVisibility(View.VISIBLE);
            viewHolder.menuImage.setImageResource(R.mipmap.ic_launcher);
            viewHolder.menuTitle.setText(menu.get("menuText").toString());
        }


        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount();
    }

    class UserViewHolder {

        public ImageView menuImage;
        public TextView menuTitle;
        public RelativeLayout menu_layout;
        public LinearLayout menu_line;

    }
}

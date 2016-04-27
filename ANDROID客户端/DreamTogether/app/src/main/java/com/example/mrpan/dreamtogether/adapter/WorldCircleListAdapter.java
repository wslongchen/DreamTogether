package com.example.mrpan.dreamtogether.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mrpan.dreamtogether.OtherActivity;
import com.example.mrpan.dreamtogether.R;
import com.example.mrpan.dreamtogether.entity.Dream;
import com.example.mrpan.dreamtogether.entity.User;
import com.example.mrpan.dreamtogether.utils.Config;
import com.example.mrpan.dreamtogether.view.NoScrollGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mrpan on 16/2/16.
 */
public class WorldCircleListAdapter extends RecyclerView.Adapter<WorldCircleListAdapter.ViewHolder>{
    private List<Dream> dreams;

    private Context mContext;

    public WorldCircleListAdapter(Context context, List<Dream> dreams)
    {
        this.mContext = context;
        this.dreams = dreams;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup viewGroup, int i )
    {
        //给ViewHolder设置布局文件
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.items, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder( ViewHolder viewHolder, int i )
    {
        // 给ViewHolder设置元素
        Dream p = dreams.get(i);
        viewHolder.dream_content.setText(p.getPost_content());
        User u=p.getPost_author();
        viewHolder.dream_author.setText(u.getUser_nickname());
        if(u.getUser_img()==null||u.getUser_img().isEmpty()){
           viewHolder.author_img.setImageResource(R.mipmap.ic_launcher);
        }
        else{

        }
//        if(p.getPost_comment_count()==null || p.getPost_comment_count().equals("")||p.getPost_comment_count().equals("0")){
//            viewHolder.dream_comments_layout.setVisibility(View.GONE);
//        }
        final String[] imgs;
        if(p.getPost_imgs()!=null && !p.getPost_imgs().equals("")) {
            if (p.getPost_imgs().length() > 0 && !p.getPost_imgs().trim().isEmpty() && !p.getPost_imgs().equals("")) {
                imgs = p.getPost_imgs().split(",");
                List<HashMap<String, String>> lists = new ArrayList<>();
                for (int j = 0; j < imgs.length; j++) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("url", "http://" + imgs[j]);
                    lists.add(map);
                }
                viewHolder.dream_img_gridView.setVisibility(View.VISIBLE);
                viewHolder.dream_img_gridView.setAdapter(new DreamImageGridAdapter(mContext, lists));
                viewHolder.dream_img_gridView
                        .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent,
                                                    View view, int position, long id) {
                                Intent intent=new Intent();
                                Bundle bundle=new Bundle();
                                bundle.putInt("type", Config.PHOTO_TYPE);
                                bundle.putStringArray("imgs", imgs);
                                bundle.putInt("ID",position);
                                intent.putExtras(bundle);
                                intent.setClass(mContext, OtherActivity.class);
                                mContext.startActivity(intent);
                            }
                        });
            }
        }
//        List<Meta> metas=dreams.get(i).getMetas();
//        List<HashMap<String,Object>> datas=new ArrayList<>();
//        for (Meta m:metas) {
//            if(m.getMeta_key().equals("dream_img")){
//                //
//            }
//            HashMap<String,Object> data=new HashMap<>();
//            data.put("url",m.getMeta_value());
//        }

//        viewHolder.favorite_total.setText(p.getDate()+"  from "+p.getAuthor());
//        if(p.getImage().length>0)
//        {
//            viewHolder.favorite_pic.setImageBitmap(BitmapUtils.Bytes2Bimap(p.getImage()));
//        }
//        else
//            viewHolder.favorite_pic.setImageResource(R.mipmap.drawing012);
    }

    @Override
    public int getItemCount()
    {
        // 返回数据总数
        return dreams == null ? 0 : dreams.size();
    }

    // 重写的自定义ViewHolder
    public static class ViewHolder
            extends RecyclerView.ViewHolder
    {
        public ImageView author_img;

        public TextView dream_author;

        public TextView dream_content;

        public NoScrollGridView dream_img_gridView;

        //public TextView dream_place;

        public TextView dream_date;

        //public TextView dream_comments_names;

        //public RelativeLayout dream_comments_layout;

        public ViewHolder( View v )
        {
            super(v);
            dream_author = (TextView) v.findViewById(R.id.dream_author);
            dream_content = (TextView) v.findViewById(R.id.dream_content);
           //dream_place = (TextView) v.findViewById(R.id.dream_place);
            dream_date = (TextView) v.findViewById(R.id.dream_date);
            //dream_comments_names = (TextView) v.findViewById(R.id.dream_comments_names);
            dream_img_gridView=(NoScrollGridView)v.findViewById(R.id.dream_img_gridView);
            author_img = (ImageView) v.findViewById(R.id.dream_author_img);
            //dream_comments_layout=(RelativeLayout)v.findViewById(R.id.dream_comments_layout);
        }
    }
}

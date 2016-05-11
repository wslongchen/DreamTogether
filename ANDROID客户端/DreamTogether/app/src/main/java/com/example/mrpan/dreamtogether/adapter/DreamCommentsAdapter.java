package com.example.mrpan.dreamtogether.adapter;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mrpan.dreamtogether.R;
import com.example.mrpan.dreamtogether.entity.BaseEntity;
import com.example.mrpan.dreamtogether.entity.Comment;
import com.example.mrpan.dreamtogether.entity.User;
import com.example.mrpan.dreamtogether.utils.ExpressionUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;

/**
 * Created by mrpan on 16/4/30.
 */
public class DreamCommentsAdapter extends BaseAdapter {
    private List<Comment> datas;
    private Context context;
    private int authorID;


    public DreamCommentsAdapter(List<Comment> datas,Context context){
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
                    R.layout.dream_comments_item, parent, false);
            viewHolder.userImg= (ImageView) convertView.findViewById(R.id.userImg);
            viewHolder.userName=(TextView)convertView.findViewById(R.id.user_nickname);
            viewHolder.commentsContent=(TextView)convertView.findViewById(R.id.comments_content);
            viewHolder.commentsDate=(TextView)convertView.findViewById(R.id.comments_date);
            viewHolder.commentsView=(RelativeLayout)convertView.findViewById(R.id.comments_view);
            viewHolder.authorCommentsView=(RelativeLayout)convertView.findViewById(R.id.author_comment_view);
            viewHolder.auhtorCommentDate=(TextView)convertView.findViewById(R.id.author_comment_date);
            viewHolder.authorCommentContent=(TextView)convertView.findViewById(R.id.author_comment_conent);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (UserViewHolder) convertView.getTag();
        }
        Comment comment=datas.get(position);
        User user=comment.getComment_user_id();
        if(user.getID()==authorID){
            viewHolder.commentsView.setVisibility(View.GONE);
            viewHolder.authorCommentsView.setVisibility(View.VISIBLE);
            viewHolder.auhtorCommentDate.setText(comment.getComment_time());
            SpannableStringBuilder sb = ExpressionUtils.prase(context, viewHolder.authorCommentContent, comment.getComment_content());// 对内容做处理
            viewHolder.authorCommentContent.setText(sb);
        }
        else
        {

            viewHolder.authorCommentsView.setVisibility(View.GONE);
            viewHolder.commentsView.setVisibility(View.VISIBLE);
            SpannableStringBuilder sb = ExpressionUtils.prase(context, viewHolder.commentsContent, comment.getComment_content());// 对内容做处理
            viewHolder.commentsContent.setText(sb);
            viewHolder.commentsDate.setText(comment.getComment_time());
            viewHolder.userName.setText(user.getUser_nickname());
            if(user.getUser_img()!=null){
                ImageLoader.getInstance().displayImage("http://"+user.getUser_img(),viewHolder.userImg);
            }
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

        public ImageView userImg;
        public TextView userName;
        public TextView commentsDate;
        public TextView commentsContent;
        public TextView authorCommentContent;
        public TextView auhtorCommentDate;
        public RelativeLayout commentsView;
        public RelativeLayout authorCommentsView;

    }

    public int getAuthorID() {
        return authorID;
    }

    public void setAuthorID(int authorID) {
        this.authorID = authorID;
    }
}

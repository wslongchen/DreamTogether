package com.example.mrpan.dreamtogether.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mrpan.dreamtogether.MyApplication;
import com.example.mrpan.dreamtogether.R;
import com.example.mrpan.dreamtogether.entity.Session;
import com.example.mrpan.dreamtogether.entity.User;
import com.example.mrpan.dreamtogether.utils.Config;
import com.example.mrpan.dreamtogether.utils.DateUtils;
import com.example.mrpan.dreamtogether.utils.ExpressionUtils;
import com.example.mrpan.dreamtogether.utils.MyLog;
import com.example.mrpan.dreamtogether.view.CircleImageView;
import com.example.mrpan.dreamtogether.xmpp.XmppUtil;

import org.jivesoftware.smack.XMPPException;

import java.util.Date;
import java.util.List;

/**
 * Created by mrpan on 16/5/11.
 */
public class SessionAdapter extends BaseAdapter {
    private Context mContext;
    private List<Session> lists;

    public SessionAdapter(Context context, List<Session> lists) {
        this.mContext = context;
        this.lists = lists;
    }

    @Override
    public int getCount() {
        if (lists != null) {
            return lists.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.message_item,null);
            holder = new Holder();
            holder.iv = (CircleImageView) convertView.findViewById(R.id.user_head);
            holder.tv_name = (TextView) convertView.findViewById(R.id.user_name);
            holder.tv_tips = (TextView) convertView.findViewById(R.id.tips);
            holder.tv_content = (TextView) convertView.findViewById(R.id.content);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_newmsg= (TextView) convertView.findViewById(R.id.tv_newmsg);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        Session session = lists.get(position);
        if(session.getType().equals(Config.MSG_TYPE_ADD_FRIEND)){
            holder.tv_tips .setVisibility(View.VISIBLE);
            holder.iv.setImageResource(R.mipmap.ibl);
        }else{
            holder.tv_tips .setVisibility(View.GONE);
            if(session.getFrom().equals(Config.XMPP_HOSTNAME)){
                holder.iv.setImageResource(R.mipmap.ic_launcher);
            }else{
                holder.iv.setImageResource(R.mipmap.ic_launcher);
            }
        }MyLog.i("ddd",session.getFrom());
        if(session.getFrom().equals(Config.XMPP_HOSTNAME)){

            holder.tv_name.setText("系统消息");
        }else {
                User u = XmppUtil.getUserVCard(MyApplication.xmppConnection, session.getFrom());
                holder.tv_name.setText(u.getUser_nickname());
        }
        //holder.tv_name.setText(session.getFrom());
        holder.tv_content.setText(ExpressionUtils.prase(mContext, holder.tv_content, session.getContent() == null ? "" : session.getContent()));
        holder.tv_time.setText(DateUtils.getCustomStr(DateUtils.StrToDate(session.getTime()),"HH:mm"));
        if(!TextUtils.isEmpty(session.getNotReadCount())&&Integer.parseInt(session.getNotReadCount())>0){
            holder.tv_newmsg.setVisibility(View.VISIBLE);
            holder.tv_newmsg.setText(session.getNotReadCount());
        }else{
            holder.tv_newmsg.setVisibility(View.GONE);
            holder.tv_newmsg.setText("");
        }
        return convertView;
    }

    class Holder {
        CircleImageView iv;
        TextView tv_name,tv_tips;
        TextView tv_content;
        TextView tv_time,tv_newmsg;
    }
}

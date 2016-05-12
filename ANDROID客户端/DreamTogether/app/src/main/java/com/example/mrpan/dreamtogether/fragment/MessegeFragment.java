package com.example.mrpan.dreamtogether.fragment;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.mrpan.dreamtogether.MyApplication;
import com.example.mrpan.dreamtogether.OtherActivity;
import com.example.mrpan.dreamtogether.R;
import com.example.mrpan.dreamtogether.adapter.SessionAdapter;
import com.example.mrpan.dreamtogether.db.ChatMsgDao;
import com.example.mrpan.dreamtogether.db.SessionDao;
import com.example.mrpan.dreamtogether.entity.Session;
import com.example.mrpan.dreamtogether.entity.User;
import com.example.mrpan.dreamtogether.utils.Config;
import com.example.mrpan.dreamtogether.utils.DateUtils;
import com.example.mrpan.dreamtogether.utils.MyLog;
import com.example.mrpan.dreamtogether.utils.MySharePreference;
import com.example.mrpan.dreamtogether.view.TitleBar;
import com.example.mrpan.dreamtogether.view.WaterRefreshView.WaterDropListView;
import com.example.mrpan.dreamtogether.xmpp.XmppUtil;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by mrpan on 16/5/11.
 */
public class MessegeFragment extends Fragment implements View.OnClickListener{
    public static final String TAG="Messege";

    private Context context;

    private TitleBar titleBar=null;

    private View currentView=null;

    private WaterDropListView waterDropListView=null;

    private SessionDao sessionDao;
    private SessionAdapter adapter;
    private List<Session> sessionList;
    private ChatMsgDao chatMsgDao;
    private String userid;

    private AddFriendReceiver addFriendReceiver;
    IntentFilter intentFilter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView=inflater.inflate(R.layout.message_fragment,container,false);
        ViewGroup viewGroup= (ViewGroup) container.getParent();
        if(viewGroup!=null){
            viewGroup.removeView(currentView);
        }
        context=getActivity();
        addFriendReceiver=new AddFriendReceiver();
        intentFilter=new IntentFilter(Config.ACTION_ADDFRIEND);
        context.registerReceiver(addFriendReceiver, intentFilter);
        initView();
        initData();
        return currentView;
    }

    private void initView(){
        sessionDao=new SessionDao(context);
        userid=new MySharePreference(context).getString("username","");
        titleBar=(TitleBar)currentView.findViewById(R.id.top_bar);
        titleBar.showLeft("消息",R.drawable.btn_back,this);
        waterDropListView=(WaterDropListView)currentView.findViewById(R.id.session_listview);
        waterDropListView.setPullLoadEnable(false);
    }

    private void initData(){
        sessionList=sessionDao.queryAllSessions(userid);
        MyLog.i(TAG,sessionList.size()+":size,userid:"+userid);
        adapter = new SessionAdapter(context, sessionList);
        waterDropListView.setAdapter(adapter);
        //waterDropListView.notifyStateChanged();
        waterDropListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
                final Session session=sessionList.get(arg2-1);
                if(session.getType().equals(Config.MSG_TYPE_ADD_FRIEND)){
                    if(!TextUtils.isEmpty(session.getIsdispose())){
                        if(!session.getIsdispose().equals("1")){
                            AlertDialog.Builder bd=new AlertDialog.Builder(context);
                            bd.setItems(new String[]{"同意"}, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    Roster roster=MyApplication.xmppConnection.getRoster();
                                    XmppUtil.addGroup(roster, "我的好友");//先默认创建一个分组
                                    if(XmppUtil.addUsers(roster, session.getFrom()+"@"+MyApplication.xmppConnection.getServiceName(), session.getFrom(),"我的好友")){
                                        //告知对方，同意添加其为好友
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    //注意消息的协议格式 =》接收者卍发送者卍消息类型卍消息内容卍发送时间
                                                    String message= session.getFrom()+Config.SPLIT+userid+Config.SPLIT+Config.MSG_TYPE_ADD_FRIEND_SUCCESS+Config.SPLIT+""+Config.SPLIT+ DateUtils.getCurrentTimeStr();
                                                    XmppUtil.sendMessage(MyApplication.xmppConnection, message, session.getFrom());
                                                } catch (XMPPException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }).start();
                                        sessionDao.updateSessionToDisPose(session.getId());//将本条数据在数据库中改为已处理
//										ToastUtil.showShortToast(mContext, "你们已经是好友了，快去聊天吧！");
                                        sessionList.remove(session);
                                        session.setIsdispose("1");
                                        sessionList.add(0,session);
                                        adapter.notifyDataSetChanged();
                                        //发送广播更新好友列表
                                        Intent intent=new Intent(Config.ACTION_FRIENDS_ONLINE_STATUS_CHANGE);
                                        context.sendBroadcast(intent);
                                    }else{
                                        Toast.makeText(context, "添加好友失败",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                            bd.create().show();
                        }else{
                            Toast.makeText(context, "已同意",Toast.LENGTH_LONG).show();
                        }
                    }
                }else{
                    FragmentTransaction transaction=getFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.anim.left_in, R.anim.left_out, R.anim.right_in, R.anim.right_out);
                    User u=XmppUtil.getUserVCard(MyApplication.xmppConnection,session.getFrom());
                    ((ChatFragment) OtherActivity.fragmentHashMap.get(ChatFragment.TAG)).setToUser(u);
                    transaction.replace(R.id.other_layout, OtherActivity.fragmentHashMap.get(ChatFragment.TAG));
                    transaction.addToBackStack(null);
                    transaction.commit();

                }
            }
        });
        waterDropListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                final Session session = sessionList.get(arg2 - 1);
                AlertDialog.Builder bd = new AlertDialog.Builder(context);
                bd.setItems(new String[]{"删除会话"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        sessionDao.deleteSession(session);
                        chatMsgDao.deleteAllMsg(session.getFrom(), session.getTo());
                        context.sendBroadcast(new Intent(Config.ACTION_NEW_MSG));
                        initData();
                    }
                }).create().show();
                return true;
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.titleBarLeftImage:
                getActivity().finish();
                break;
        }
    }

    class AddFriendReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context arg0, Intent intent) {
            initData();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        context.registerReceiver(addFriendReceiver, intentFilter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        context.unregisterReceiver(addFriendReceiver);
    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        context.unregisterReceiver(addFriendReceiver);
//    }
}

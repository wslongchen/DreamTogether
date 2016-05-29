package com.example.mrpan.dreamtogether.xmpp;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import com.example.mrpan.dreamtogether.MyApplication;
import com.example.mrpan.dreamtogether.utils.Config;
import com.example.mrpan.dreamtogether.utils.MySharePreference;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Presence;

import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Created by mrpan on 16/5/9.
 */
public class MySystemService extends Service {
    private static MySystemService mInstance = null;
    public static DatagramSocket ds = null;

    private NotificationManager mNotificationManager;

    private String mUserName, mPassword;
    private XmppConnectionManager mXmppConnectionManager;
    private XMPPConnection mXMPPConnection;

    private CheckConnectionListener checkConnectionListener;
   // private FriendsPacketListener friendsPacketListener;
    private MySharePreference mySharePreference;

    private final IBinder binder = new MyBinder();

    public class MyBinder extends Binder {
        public MySystemService getService() {
            return MySystemService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mySharePreference=new MySharePreference(this);
        mUserName = mySharePreference.getString("username","");
        mPassword =mySharePreference.getString("userpassword","");
        try {
            ds = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);         // 通知
        mXmppConnectionManager = XmppConnectionManager.getInstance();
        initXMPPTask();
    }

    public static MySystemService getInstance() {
        return mInstance;
    }


    /**
     * 初始化xmpp和完成后台登录
     */
    private void initXMPPTask() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    initXMPP();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 初始化XMPP
     */
    void initXMPP() {
        mXMPPConnection = mXmppConnectionManager.init();						//初始化XMPPConnection
        try {
            mPassword = mySharePreference.getString("userpassword","");//PreferencesUtils.getSharePreStr(this, "pwd");
            mXMPPConnection.connect();
            try{
                if(checkConnectionListener!=null){
                    mXMPPConnection.removeConnectionListener(checkConnectionListener);
                    checkConnectionListener=null;
                }
            }catch(Exception e){

            }
            mXMPPConnection.login(mUserName, mPassword);
            if(mXMPPConnection.isAuthenticated()){                                     //登录成功
                MyApplication.xmppConnection=mXMPPConnection;
                sendLoginBroadcast(true);
                //添加xmpp连接监听
                checkConnectionListener=new CheckConnectionListener(this);
                mXMPPConnection.addConnectionListener(checkConnectionListener);
                // 注册好友状态更新监听
//                friendsPacketListener=new FriendsPacketListener(this);
//                PacketFilter filter = new AndFilter(new PacketTypeFilter(Presence.class));
//                mXMPPConnection.addPacketListener(friendsPacketListener, filter);
                XmppUtil.setPresence(this,mXMPPConnection,1);//PreferencesUtils.getSharePreInt(this, "online_status"));//设置在线状态
            }else{
                sendLoginBroadcast(false);
                stopSelf();                                                                                        //如果登录失败，自动销毁Service
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendLoginBroadcast(false);
            stopSelf();
            Toast.makeText(this,"服务器连接失败",Toast.LENGTH_SHORT).show();
        }															//登录XMPP
        ChatManager chatmanager = mXMPPConnection.getChatManager();
        chatmanager.addChatListener(new ChatManagerListener() {
            @Override
            public void chatCreated(Chat arg0, boolean arg1) {
                arg0.addMessageListener(new MsgListener(MySystemService.this, mNotificationManager));
            }
        });
    }

    /**
     * 发送登录状态广播
     * @param isLoginSuccess
     */
    void sendLoginBroadcast(boolean isLoginSuccess){
        Intent intent =new Intent(Config.ACTION_IS_LOGIN_SUCCESS);
        intent.putExtra("isLoginSuccess", isLoginSuccess);
        sendBroadcast(intent);
    }


    @Override
    public void onDestroy() {
        if(mNotificationManager!=null){

        }
        try {
            if (mXMPPConnection != null) {
                mXMPPConnection.disconnect();
                mXMPPConnection = null;
            }
            if(mXmppConnectionManager!=null){
                mXmppConnectionManager = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

}

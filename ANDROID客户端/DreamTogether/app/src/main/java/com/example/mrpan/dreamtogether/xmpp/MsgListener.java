package com.example.mrpan.dreamtogether.xmpp;

import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Notification.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;

import com.example.mrpan.dreamtogether.R;
import com.example.mrpan.dreamtogether.db.ChatMsgDao;
import com.example.mrpan.dreamtogether.db.SessionDao;
import com.example.mrpan.dreamtogether.entity.Msg;
import com.example.mrpan.dreamtogether.entity.Session;
import com.example.mrpan.dreamtogether.utils.Config;
import com.example.mrpan.dreamtogether.utils.DateUtils;
import com.example.mrpan.dreamtogether.utils.MyLog;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;


@SuppressWarnings("static-access")
public class MsgListener implements MessageListener {
	
	private MySystemService context;
	private NotificationManager mNotificationManager;


	private Notification mNotification;
	private KeyguardManager mKeyguardManager = null;
	
	private boolean isShowNotice=false;
	
	private ChatMsgDao msgDao;
	private SessionDao sessionDao;
	
	public MsgListener(MySystemService context, NotificationManager mNotificationManager){
		this.context=context;
		this.mNotificationManager=mNotificationManager;
		mKeyguardManager = (KeyguardManager) context.getSystemService(context.KEYGUARD_SERVICE);    
		sessionDao=new SessionDao(context);
		msgDao=new ChatMsgDao(context);
	}
	
	@Override
	public void processMessage(Chat arg0, Message message) {
		try{
			String msgBody = message.getBody();
			if (TextUtils.isEmpty(msgBody))
				return;
			//iz28dbl1lqiz
			if(message.getFrom().equals(Config.XMPP_HOSTNAME)){
				System.out.println(message.getFrom());
				Session session=new Session();
				session.setFrom(Config.XMPP_HOSTNAME);
				session.setTo("all");
				session.setNotReadCount("");//未读消息数量
				session.setTime(DateUtils.getCurrentTimeStr());
				session.setType(Config.MSG_TYPE_TEXT);
				session.setContent(message.getBody());

				if(sessionDao.isContent(Config.XMPP_HOSTNAME,"all")){//判断最近联系人列表是否已存在记录
					sessionDao.updateSession(session);
				}else{
					sessionDao.insertSession(session);
				}
			}else {
				//接收者卍发送者卍消息类型卍消息内容卍发送时间
				String[] msgs = msgBody.split(Config.SPLIT);
				String to = msgs[0];//接收者,当然是自己
				String from = msgs[1];//发送者，谁给你发的消息
				String msgtype = msgs[2];//消息类型
				String msgcontent = msgs[3];//消息内容
				String msgtime = msgs[4];//消息时间
				Session session = new Session();
				session.setFrom(from);
				session.setTo(to);
				session.setNotReadCount("");//未读消息数量
				session.setTime(msgtime);
				MyLog.i("DDDD",msgcontent+",type"+msgtype);
				if (msgtype.equals(Config.MSG_TYPE_TEXT)) {//文本类型
					Msg msg = new Msg();
					msg.setToUser(to);
					msg.setFromUser(from);
					msg.setIsComing(0);
					msg.setContent(msgcontent);
					msg.setDate(msgtime);
					msg.setIsReaded("0");
					msg.setType(msgtype);
					msgDao.insert(msg);
					sendNewMsg(msg);
					session.setType(Config.MSG_TYPE_TEXT);
					session.setContent(msgcontent);
					if (sessionDao.isContent(from, to)) {//判断最近联系人列表是否已存在记录
						sessionDao.updateSession(session);
					} else {
						sessionDao.insertSession(session);
					}
				}
				if (msgtype.equals(Config.MSG_TYPE_VOICE)) {//语音类型
					message.getProperty("");
					Msg msg = new Msg();
					msg.setToUser(to);
					msg.setFromUser(from);
					msg.setIsComing(0);
					msg.setContent(msgcontent);
					msg.setDate(msgtime);
					msg.setIsReaded("0");
					msg.setType(msgtype);
					msgDao.insert(msg);
					sendNewMsg(msg);
					session.setType(Config.MSG_TYPE_VOICE);
					session.setContent(msgcontent);
					if (sessionDao.isContent(from, to)) {//判断最近联系人列表是否已存在记录
						sessionDao.updateSession(session);
					} else {
						sessionDao.insertSession(session);
					}
				}
			}
			Intent intent=new Intent(Config.ACTION_ADDFRIEND);//发送广播，通知消息界面更新
			context.sendBroadcast(intent);
		//showNotice(msgcontent);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	void sendNewMsg(Msg msg){
		Intent intent=new Intent(Config.ACTION_NEW_MSG);//发送广播到聊天界面
		Bundle b=new Bundle();
		b.putSerializable("msg", msg);
		intent.putExtra("msg", b);
		context.sendBroadcast(intent);
	}
	
	@SuppressWarnings("deprecation")
	public void showNotice(String content) {
		// 更新通知栏
		CharSequence tickerText = content;

//		mNotification = new Notification(R.mipmap.ic_launcher, tickerText, System.currentTimeMillis());
//		mNotification.flags |= Notification.FLAG_AUTO_CANCEL;
//		// 设置默认声音
//		mNotification.defaults |= Notification.DEFAULT_SOUND;
//		// 设定震动(需加VIBRATE权限)
//		mNotification.defaults |= Notification.DEFAULT_VIBRATE;
//		// LED灯
//		mNotification.defaults |= Notification.DEFAULT_LIGHTS;
//		mNotification.ledARGB = 0xff00ff00;
//		mNotification.ledOnMS = 500;
//		mNotification.ledOffMS = 1000;
//		mNotification.flags |= Notification.FLAG_SHOW_LIGHTS;
//		//mNotification.setLatestEventInfo(context, "新消息", tickerText, null);
//		mNotification.tickerText=tickerText;
//		mNotification.category="新消息";
//		mNotification.
//		mNotificationManager.notify(Config.NOTIFY_ID, mNotification);// 通知
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);

		mBuilder.setContentTitle("新消息")//设置通知栏标题
				.setContentText(tickerText) //设置通知栏显示内容
//				.setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL)) //设置通知栏点击意图
				//  .setNumber(number) //设置通知集合的数量
				.setTicker(tickerText) //通知首次出现在通知栏，带上升动画效果的
				.setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
				.setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
				//  .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
				.setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
				.setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
						//Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
				.setSmallIcon(R.mipmap.ic_launcher);//设置通知小ICON
		 mNotification = mBuilder.build();
		mNotification.flags = Notification.FLAG_AUTO_CANCEL;
		mNotificationManager.notify(Config.NOTIFY_ID, mNotification);// 通知
	}
	
 }


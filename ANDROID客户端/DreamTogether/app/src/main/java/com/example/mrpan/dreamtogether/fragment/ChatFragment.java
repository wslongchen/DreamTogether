package com.example.mrpan.dreamtogether.fragment;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mrpan.dreamtogether.MyApplication;
import com.example.mrpan.dreamtogether.R;
import com.example.mrpan.dreamtogether.adapter.ChatAdapter;
import com.example.mrpan.dreamtogether.adapter.FaceVPAdapter;
import com.example.mrpan.dreamtogether.entity.Msg;
import com.example.mrpan.dreamtogether.entity.User;
import com.example.mrpan.dreamtogether.utils.Config;
import com.example.mrpan.dreamtogether.utils.DateUtils;
import com.example.mrpan.dreamtogether.utils.ExpressionUtils;
import com.example.mrpan.dreamtogether.utils.MySharePreference;
import com.example.mrpan.dreamtogether.view.DropdownListView;
import com.example.mrpan.dreamtogether.view.TitleBar;
import com.example.mrpan.dreamtogether.xmpp.MySystemService;
import com.example.mrpan.dreamtogether.xmpp.XmppUtil;

import org.jivesoftware.smack.XMPPException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by mrpan on 16/5/10.
 */
public class ChatFragment extends Fragment implements View.OnClickListener,DropdownListView.OnRefreshListenerHeader {
    public static final String TAG="Chat";

    private Context context;

    private TitleBar titleBar=null;

    private String I,YOU;

    private View currentView=null;

    private User toUser=null;

    private EditText input_sms;

    private ImageView face,add;

    private TextView send_sms;

    private LinearLayout chat_face_container,chat_add_container;
    private ImageView image_face;//表情图标
    //表情图标每页6列4行
    private int columns = 6;
    private int rows = 4;
    //每页显示的表情view
    private List<View> views = new ArrayList<View>();
    //表情列表
    private List<String> staticFacesList;
    //消息
    private List<Msg> listMsg;
    private int offset;

    private LayoutInflater inflater;
    private ViewPager mViewPager;
    private LinearLayout mDotsLayout;

    private DropdownListView mListView;
    private ChatAdapter mLvAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView=inflater.inflate(R.layout.chat_fragment,container,false);
        ViewGroup viewGroup= (ViewGroup) container.getParent();
        if(viewGroup!=null){
            viewGroup.removeView(currentView);
        }

        context=getActivity();
        initConfig();
        initView();
        initData();
        initViewPager();
        return currentView;
    }

    private void initConfig(){
        MySharePreference sharePreference=new MySharePreference(context);
        String myName=sharePreference.getString("username","");
        if(!TextUtils.isEmpty(myName)){
            I=myName;
        }
        if(null!=toUser) {
            YOU = toUser.getUser_login();
        }
        staticFacesList=ExpressionUtils.initStaticFaces(context);
        inflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
    }

    public void initData(){
        offset=0;
        //listMsg=msgDao.queryMsg(YOU,I,offset);
        listMsg=new ArrayList<>();
        Msg msg=new Msg();
        msg.setDate(DateUtils.getCurrentTimeStr());
        msg.setContent("i love you");
        msg.setFromUser("longchen2");
        msg.setToUser("longchen");
        msg.setType(Config.MSG_TYPE_TEXT);
        msg.setIsComing(0);
        listMsg.add(msg);
        offset=listMsg.size();

        mLvAdapter = new ChatAdapter(context, listMsg);
        mListView.setAdapter(mLvAdapter);
        mListView.setSelection(listMsg.size());
    }

    private void initView(){
        titleBar=(TitleBar)currentView.findViewById(R.id.top_bar);
        titleBar.showLeft("与" + toUser.getUser_nickname() + "聊天", R.mipmap.ic_launcher, this);
        mViewPager = (ViewPager) currentView.findViewById(R.id.face_viewpager);
        mViewPager.setOnPageChangeListener(new PageChange());
        //表情下小圆点
        mDotsLayout = (LinearLayout) currentView.findViewById(R.id.face_dots_container);
        image_face=(ImageView)  currentView.findViewById(R.id.image_face);
        image_face.setOnClickListener(this);
        chat_face_container=(LinearLayout) currentView.findViewById(R.id.chat_face_container);
        chat_add_container=(LinearLayout)currentView.findViewById(R.id.chat_add_container);
        input_sms=(EditText)currentView.findViewById(R.id.input_sms);
        input_sms.setOnClickListener(this);
        send_sms=(TextView)currentView.findViewById(R.id.send_sms);
        send_sms.setOnClickListener(this);
        face=(ImageView)currentView.findViewById(R.id.image_face);
        face.setOnClickListener(this);
        add=(ImageView)currentView.findViewById(R.id.image_add);
        add.setOnClickListener(this);
        mListView = (DropdownListView)currentView.findViewById(R.id.message_chat_listview);
        mListView.setOnRefreshListenerHead(this);
        mListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
                    if (chat_face_container.getVisibility() == View.VISIBLE) {
                        chat_face_container.setVisibility(View.GONE);
                    }
                    if (chat_add_container.getVisibility() == View.VISIBLE) {
                        chat_add_container.setVisibility(View.GONE);
                    }
                    hideSoftInputView();
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.titleBarLeftImage:
                getActivity().finish();
                break;
            case R.id.image_face:
                hideSoftInputView();//隐藏软键盘
                if(chat_add_container.getVisibility()==View.VISIBLE){
                    chat_add_container.setVisibility(View.GONE);
                }
                if(chat_face_container.getVisibility()==View.GONE){
                    chat_face_container.setVisibility(View.VISIBLE);
                }else{
                    chat_face_container.setVisibility(View.GONE);
                }
                break;
            case R.id.image_add:
                hideSoftInputView();//隐藏软键盘
                if(chat_face_container.getVisibility()==View.VISIBLE){
                    chat_face_container.setVisibility(View.GONE);
                }
                if(chat_add_container.getVisibility()==View.GONE){
                    chat_add_container.setVisibility(View.VISIBLE);
                }else{
                    chat_add_container.setVisibility(View.GONE);
                }
                break;
            case R.id.input_sms:
                if(chat_face_container.getVisibility()==View.VISIBLE) {
                    chat_face_container.setVisibility(View.GONE);
                }
            if(chat_add_container.getVisibility()==View.VISIBLE) {
                chat_add_container.setVisibility(View.GONE);
            }
                break;
            case R.id.send_sms:
                String content=input_sms.getText().toString();
                if(TextUtils.isEmpty(content)){
                    return;
                }
                sendMsgText(content);
                break;
            default:
                break;
        }
    }

    /**
     * 弹出输入法窗口
     */
    private void showSoftInputView(final View v) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ((InputMethodManager) v.getContext().getSystemService(Service.INPUT_METHOD_SERVICE)).toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 0);
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftInputView() {
        InputMethodManager manager = ((InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 执行发送消息 文本类型
     * @param content
     */
    void sendMsgText(String content){
        Msg msg=getChatInfoTo(content, Config.MSG_TYPE_TEXT);
        //msg.setMsgId(msgDao.insert(msg));
        listMsg.add(msg);
        offset=listMsg.size();
        mLvAdapter.notifyDataSetChanged();
        input_sms.setText("");
        final String message=YOU+Config.SPLIT+I+Config.SPLIT+Config.MSG_TYPE_TEXT+Config.SPLIT+content+Config.SPLIT+ DateUtils.getCurrentTimeStr();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    XmppUtil.sendMessage(MyApplication.xmppConnection, message, YOU);
                } catch (XMPPException e) {
                    e.printStackTrace();
                    Looper.prepare();
                    Toast.makeText(context, "发送失败",Toast.LENGTH_LONG).show();
                    Looper.loop();
                }
            }
        }).start();
    }

    /**
     * 发送的信息
     *  from为收到的消息，to为自己发送的消息
     * @param message => 接收者卍发送者卍消息类型卍消息内容卍发送时间
     * @return
     */
    private Msg getChatInfoTo(String message,String msgtype) {
        String time=DateUtils.getCurrentTimeStr();
        Msg msg = new Msg();
        msg.setFromUser(YOU);
        msg.setToUser(I);
        msg.setType(msgtype);
        msg.setIsComing(1);
        msg.setContent(message);
        msg.setDate(time);
        return msg;
    }

    /**
     * 初始化表情
     */
    private void initViewPager() {
        int pagesize = ExpressionUtils.getPagerCount(staticFacesList.size(), columns, rows);
        // 获取页数
        for (int i = 0; i <pagesize; i++) {
            views.add(ExpressionUtils.viewPagerItem(context, i, staticFacesList,columns, rows, input_sms));
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(16, 16);
            mDotsLayout.addView(dotsItem(i), params);
        }
        FaceVPAdapter mVpAdapter = new FaceVPAdapter(views);
        mViewPager.setAdapter(mVpAdapter);
        mDotsLayout.getChildAt(0).setSelected(true);
    }

    /**
     * 表情页切换时，底部小圆点
     * @param position
     * @return
     */
    private ImageView dotsItem(int position) {
        View layout = inflater.inflate(R.layout.dot_image, null);
        ImageView iv = (ImageView) layout.findViewById(R.id.face_dot);
        iv.setId(position);
        return iv;
    }

    @Override
    public void onRefresh() {

    }

    /**
     * 表情页改变时，dots效果也要跟着改变
     * */
    class PageChange implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }
        @Override
        public void onPageSelected(int arg0) {
            for (int i = 0; i < mDotsLayout.getChildCount(); i++) {
                mDotsLayout.getChildAt(i).setSelected(false);
            }
            mDotsLayout.getChildAt(arg0).setSelected(true);
        }
    }

    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }
}

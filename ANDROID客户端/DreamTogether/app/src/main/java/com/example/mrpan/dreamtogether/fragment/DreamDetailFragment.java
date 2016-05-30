package com.example.mrpan.dreamtogether.fragment;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mrpan.dreamtogether.OtherActivity;
import com.example.mrpan.dreamtogether.R;
import com.example.mrpan.dreamtogether.adapter.DreamCommentsAdapter;
import com.example.mrpan.dreamtogether.adapter.DreamImageGridAdapter;
import com.example.mrpan.dreamtogether.adapter.FaceVPAdapter;
import com.example.mrpan.dreamtogether.entity.Comment;
import com.example.mrpan.dreamtogether.entity.CommentPosts;
import com.example.mrpan.dreamtogether.entity.Dream;
import com.example.mrpan.dreamtogether.entity.DreamPosts;
import com.example.mrpan.dreamtogether.entity.Meta;
import com.example.mrpan.dreamtogether.entity.User;
import com.example.mrpan.dreamtogether.http.HttpHelper;
import com.example.mrpan.dreamtogether.http.HttpResponseCallBack;
import com.example.mrpan.dreamtogether.utils.CacheUtils;
import com.example.mrpan.dreamtogether.utils.Config;
import com.example.mrpan.dreamtogether.utils.DateUtils;
import com.example.mrpan.dreamtogether.utils.ExpressionUtils;
import com.example.mrpan.dreamtogether.utils.GsonUtils;
import com.example.mrpan.dreamtogether.utils.OtherUtils;
import com.example.mrpan.dreamtogether.view.LXiuXiu;
import com.example.mrpan.dreamtogether.view.NoScrollGridView;
import com.example.mrpan.dreamtogether.view.TitleBar;
import com.example.mrpan.dreamtogether.view.WaterRefreshView.WaterDropListView;
import com.example.mrpan.dreamtogether.view.WaterRefreshView.WaterDropListViewHeader;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by mrpan on 16/4/30.
 */
public class DreamDetailFragment extends Fragment implements View.OnClickListener, WaterDropListView.IWaterDropListViewListener{
    public final static String TAG="DreamDetail";

    private Context context;

    private TitleBar titleBar;

    private View currentView;

    private Dream dream;

    private FragmentTransaction transaction;

    //private ListView commentsList;
    private WaterDropListView commentsList;

    private TextView dream_author,dream_date,dream_content,dream_deviceinfo;

    private HttpHelper httpHelper;

    private ImageView user_img,comment_img;

    private NoScrollGridView gridView;

    private TextView comment;

    private EditText commentText;

    private LinearLayout chat_face_container;
    private ImageView image_face;//表情图标
    //表情图标每页6列4行
    private int columns = 6;
    private int rows = 4;
    //每页显示的表情view
    private List<View> views = new ArrayList<View>();
    //表情列表
    private List<String> staticFacesList;

    private LayoutInflater inflater;
    private ViewPager mViewPager;
    private LinearLayout mDotsLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView=inflater.inflate(R.layout.dream_info_fragment,container,false);
        ViewGroup viewGroup= (ViewGroup) currentView.getParent();
        if(viewGroup!=null){
            viewGroup.removeView(currentView);
        }
        context=getActivity();
        initView();
        initViewPager();
        return currentView;
    }

    private void initView(){
        staticFacesList=ExpressionUtils.initStaticFaces(context);
        comment=(TextView)currentView.findViewById(R.id.comments_btn);
        inflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        comment.setOnClickListener(this);

        mViewPager = (ViewPager) currentView.findViewById(R.id.face_viewpager);
        mViewPager.setOnPageChangeListener(new PageChange());
        //表情下小圆点
        mDotsLayout = (LinearLayout) currentView.findViewById(R.id.face_dots_container);
        image_face=(ImageView)  currentView.findViewById(R.id.image_face);
        image_face.setOnClickListener(this);
        chat_face_container=(LinearLayout) currentView.findViewById(R.id.chat_face_container);
        commentText=(EditText)currentView.findViewById(R.id.comment_content);
        commentText.setOnClickListener(this);
        httpHelper=HttpHelper.getInstance();
        commentsList=(WaterDropListView)currentView.findViewById(R.id.comments_list);
        View headerview=View.inflate(context,R.layout.dream_info_headview,(ViewGroup)currentView.getParent());
        commentsList.addHeaderView(headerview);

        titleBar=(TitleBar)currentView.findViewById(R.id.top_bar);
        titleBar.showLeftAndRight("详情", R.drawable.btn_back, R.drawable.btn_more, this, this);
        dream_author=(TextView)currentView.findViewById(R.id.dream_author);
        dream_date=(TextView)currentView.findViewById(R.id.dream_date);
        dream_content=(TextView)currentView.findViewById(R.id.content);
        dream_deviceinfo=(TextView)currentView.findViewById(R.id.dream_deviceinfo);
        user_img=(ImageView)currentView.findViewById(R.id.user_img);
        gridView=(NoScrollGridView)currentView.findViewById(R.id.gridView);
        comment_img=(ImageView)currentView.findViewById(R.id.comment_img);
        comment_img.setOnClickListener(this);
        showData();
        User user=new User();
        user.setID(1);
        user.setUser_nickname("傻叼");
        List<Comment> comments=new ArrayList<>();
        Comment comment=new Comment();
        comment.setComment_content("哟，不错哦～");
        comment.setComment_time("2012-12-02 12:30");
        comment.setComment_user_id(user);
        comments.add(comment);
        comments.add(comment);
        comments.add(comment);
        comments.add(comment);

        user.setID(3);
        comment.setComment_user_id(user);
        comments.add(comment);
        user=new User();
        user.setUser_nickname("傻叼");
        user.setID(5);
        comment=new Comment();
        comment.setComment_user_id(user);
        comments.add(comment);
        DreamCommentsAdapter dreamCommentsAdapter=new DreamCommentsAdapter(comments,context);
        dreamCommentsAdapter.setAuthorID(5);
        commentsList.setAdapter(dreamCommentsAdapter);
        commentsList.setWaterDropListViewListener(this);
        commentsList.setPullLoadEnable(false);
        httpHelper.asyHttpGetRequest(Config.GetCommentByID(dream.getID()),new DetailHttpListener(1));

    }

    public void hideSoftInputView() {
        InputMethodManager manager = ((InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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

    void showData(){
        if(dream!=null){
            User user=dream.getPost_author();
            dream_author.setText(user.getUser_nickname());
            ImageLoader.getInstance().displayImage("http://" + user.getUser_img(), user_img);
            dream_content.setText(dream.getPost_content());
            dream_date.setText(DateUtils.getAutoTimeStr(dream.getPost_date()));
            //System.out.println("id:"+user.getID());
            final String[] imgs;
            if(dream.getPost_imgs()!=null && !dream.getPost_imgs().equals("")) {
                if (dream.getPost_imgs().length() > 0 && !dream.getPost_imgs().trim().isEmpty() && !dream.getPost_imgs().equals("")) {
                    gridView.setVisibility(View.VISIBLE);
                    imgs = dream.getPost_imgs().split(",");
                    List<HashMap<String, String>> lists = new ArrayList<>();
                    for (int j = 0; j < imgs.length; j++) {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("url", "http://" + imgs[j]);
                        lists.add(map);
                    }
                    gridView.setAdapter(new DreamImageGridAdapter(context, lists));
                    gridView
                            .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent,
                                                        View view, int position, long id) {
                                    Intent intent = new Intent();
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("type", Config.PHOTO_TYPE);
                                    bundle.putStringArray("imgs", imgs);
                                    bundle.putInt("ID", position);
                                    intent.putExtras(bundle);
                                    intent.setClass(context, OtherActivity.class);
                                    context.startActivity(intent);
                                }
                            });
                }
            }
        }
    }

    public Dream getDream() {
        return dream;
    }

    public void setDream(Dream dream) {
        this.dream = dream;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.titleBarLeftImage:
                //getFragmentManager().popBackStack();
                getActivity().finish();
                break;
            case R.id.titleBarRightImage:
                break;
            case R.id.comments_btn:
                String content=commentText.getText().toString();
                if(TextUtils.isEmpty(content)){
                    return;
                }
                Comment comment=new Comment();
                User user=(User) CacheUtils.readHttpCache(Config.DIR_CACHE_PATH, "user_info");
                comment.setComment_time(DateUtils.getCurrentTimeStr());
                comment.setComment_content(commentText.getText().toString().trim());
                comment.setPost_id(String.valueOf(dream.getID()));
                comment.setComment_user_id(user);
                httpHelper.asyHttpPostRequest(Config.CREATE_COMMENT, OtherUtils.CommentToMap(comment,"postComment"), new DetailHttpListener(2));
                hideSoftInputView();
                break;
            case R.id.comment_content:
                if(chat_face_container.getVisibility()==View.VISIBLE){
                    chat_face_container.setVisibility(View.GONE);
                }
                break;
            case R.id.image_face:
                hideSoftInputView();//隐藏软键盘
                if(chat_face_container.getVisibility()==View.GONE){
                    chat_face_container.setVisibility(View.VISIBLE);
                }else{
                    chat_face_container.setVisibility(View.GONE);
                }
                break;
            case R.id.comment_img:
                //让输入框获取焦点
                commentText.requestFocus();
                showSoftInputView(commentText);
                break;
            default:
                hideSoftInputView();
                break;
        }
    }

    @Override
    public void onRefresh() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    Message message=new Message();
                    message.arg1=1;
                    myHander.sendMessage(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onLoadMore() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    Message message=new Message();
                    message.arg1=2;
                    myHander.sendMessage(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    class DetailHttpListener implements HttpResponseCallBack {

        private int flag;

        public DetailHttpListener(int flag){
            this.flag=flag;
        }

        @Override
        public void onSuccess(String url, String result) {
            Message message=new Message();
            message.arg1= Config.HTTP_REQUEST_SUCCESS;
            message.arg2=flag;
            message.obj=result;
            myHander.sendMessage(message);
        }

        @Override
        public void onFailure(int httpResponseCode, int errCode, String err) {
            Message message=new Message();
            message.arg1=Config.HTTP_REQUEST_ERROR;
            myHander.sendMessage(message);
        }
    }

    Handler myHander=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1){
                case Config.HTTP_REQUEST_SUCCESS:
                    switch (msg.arg2){
                        case 1:
                            if(msg.obj!=null) {
                                int ret = 0;
                                try {
                                    JSONObject jsonObject = new JSONObject(msg.obj.toString().replace("\uFEFF", ""));
                                    ret = jsonObject.getInt("ret");
                                    if (ret == Config.RESULT_RET_SUCCESS) {
                                        CommentPosts commentPosts = (CommentPosts) GsonUtils.getEntity(msg.obj.toString(), CommentPosts.class);
                                        List<Comment> comments=commentPosts.getPost();
                                        DreamCommentsAdapter dreamCommentsAdapter=new DreamCommentsAdapter(comments,context);
                                        dreamCommentsAdapter.setAuthorID(dream.getPost_author().getID());
                                       // if(comments.size()>0){
                                            commentsList.setAdapter(dreamCommentsAdapter);
                                        commentsList.notifyStateChanged(WaterDropListViewHeader.STATE.ready, WaterDropListViewHeader.STATE.end);
                                            //commentsList.setAdapter(dreamCommentsAdapter);
                                            // commentsList.setWaterDropListViewListener(g);
                                        //}

                                    } else {
                                        //Toast.makeText(context, "再咻咻说不定能有！", Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                   // Toast.makeText(context, "再咻咻说不定能有！", Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }
                            }
                            break;

                        case 2:
                            if(msg.obj!=null) {
                                int ret = 0;
                                try {
                                    JSONObject jsonObject = new JSONObject(msg.obj.toString().replace("\uFEFF", ""));
                                    ret = jsonObject.getInt("ret");
                                    if (ret == Config.RESULT_RET_SUCCESS) {
                                        Toast.makeText(context, "评论成功！", Toast.LENGTH_LONG).show();
                                        commentText.setText("");
                                        httpHelper.asyHttpGetRequest(Config.GetCommentByID(dream.getID()), new DetailHttpListener(1));
                                    } else {
                                        Toast.makeText(context, "评论失败！", Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    // Toast.makeText(context, "再咻咻说不定能有！", Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }
                            }
                            break;
                        default:
                            break;
                    }

                    break;
                case Config.HTTP_REQUEST_ERROR:
                    Toast.makeText(context,"联网失败！",Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    commentsList.stopRefresh();
                    break;
                case 2:
                    commentsList.stopLoadMore();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };


    /**
     * 初始化表情
     */
    private void initViewPager() {
        int pagesize = ExpressionUtils.getPagerCount(staticFacesList.size(), columns, rows);
        // 获取页数
        for (int i = 0; i <pagesize; i++) {
            views.add(ExpressionUtils.viewPagerItem(context, i, staticFacesList,columns, rows, commentText));
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

}

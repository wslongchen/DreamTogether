package com.example.mrpan.dreamtogether.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mrpan.dreamtogether.R;
import com.example.mrpan.dreamtogether.entity.Dream;
import com.example.mrpan.dreamtogether.entity.DreamPosts;
import com.example.mrpan.dreamtogether.entity.User;
import com.example.mrpan.dreamtogether.http.HttpHelper;
import com.example.mrpan.dreamtogether.http.HttpResponseCallBack;
import com.example.mrpan.dreamtogether.utils.CacheUtils;
import com.example.mrpan.dreamtogether.utils.Config;
import com.example.mrpan.dreamtogether.utils.DateUtils;
import com.example.mrpan.dreamtogether.utils.GsonUtils;
import com.example.mrpan.dreamtogether.utils.MyLog;
import com.example.mrpan.dreamtogether.adapter.WorldCircleListAdapter;
import com.example.mrpan.dreamtogether.utils.OtherUtils;
import com.example.mrpan.dreamtogether.view.TitleBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by mrpan on 16/3/17.
 */
public class WorldCircleFragment extends Fragment implements View.OnClickListener{

    public final static String TAG="WorldCircle";

    private View currentView=null;

    private RecyclerView recyclerView;

    private HttpHelper httpHelper;

    private DreamPosts dreams;

    private TitleBar titleBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView=inflater.inflate(R.layout.dreams_demo,container,false);
        ViewGroup viewGroup= (ViewGroup) currentView.getParent();
        if(viewGroup!=null){
            viewGroup.removeView(currentView);
        }
        initView();
        return currentView;
    }

    void initView(){
        titleBar=(TitleBar)currentView.findViewById(R.id.top_bar);
        titleBar.showRight("梦想圈",R.mipmap.ic_launcher,this);

        recyclerView=(RecyclerView)currentView.findViewById(R.id.dream_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // 设置ItemAnimator
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        // 设置固定大小
        recyclerView.setHasFixedSize(true);
        httpHelper=HttpHelper.getInstance();
        httpHelper.asyHttpGetRequest(Config.REQUEST_ALL_DREAM, new DreamHttpResponseCallBack(Config.ALL_DREAM));
//        List<String> fs=new ArrayList<>();
//        fs.add("/storage/emulated/0/DCIM/browser-photos/1458103871032.jpg");///storage/09E4-361E/Pictures/icon_news.png
//        fs.add("/storage/F10A-1818/网络图片/03.jpg");
//        fs.add("/storage/emulated/0/DCIM/browser-photos/1458103871032.jpg");
//        Dream dream=new Dream();
//        User user=new User();
//        user.setID(2);
//        dream.setPost_author(user);
//        dream.setPost_content("SSSSS");
//        dream.setPost_date(DateUtils.getCurrentTimeStr());
//        dream.setPost_comment_count("0");
//        dream.setPost_titile("null");
//        dream.setPost_comment_status("0");
//        dream.setPost_type("0");
//        dream.setPost_status("0");
//        dream.setPost_password("null");
//        dream.setPost_guid("null");
//        Map map=OtherUtils.DreamToMap(dream);

//        Map<String,String> map=new HashMap<>();
//        map.put("author", "2");
//        map.put("date", "2016-02-02");
//        map.put("content","sss");
//        map.put("title", "ddd");
//        map.put("status", "0");
//        map.put("password", "null");
//        map.put("guid", "null");
//        map.put("type", "0");
//        map.put("commentstatus", "0");
//        map.put("commentcount","0");
//        httpHelper.asyHttpPostRequest("http://dream.mrpann.com/allpost.php?action=postDreamImg",map, fs, new DreamHttpResponseCallBack(12));
//        DreamPosts cache= (DreamPosts) CacheUtils.readHttpCache(Config.DIR_CACHE_PATH,"all_dream");
//        if(cache!=null)
//            dreams=cache;
//        if(cache.getPost().size()>0)
  //          dreams=cache;
    }

    private void showData(){
        if(dreams!=null&&dreams.getPost().size()>0){
            WorldCircleListAdapter worldCircleListAdapter=new WorldCircleListAdapter(getActivity(),dreams.getPost());
            recyclerView.setAdapter(worldCircleListAdapter);
        }
    }


    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case Config.DATA_SHOW:
                    if(msg.obj!=null)
                    {
                        switch (msg.arg2)
                        {
                            case Config.ALL_DREAM:
//                                JSONObject jsonObject = null;
//                                try {
//                                    jsonObject = new JSONObject(msg.obj.toString().replace("\uFEFF\uFEFF\uFEFF", ""));
//                                    int ret = jsonObject.getInt("ret");
//                                    System.out.println(ret+":ddd");
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
                                DreamPosts dream= (DreamPosts) GsonUtils.getEntity("{\"ret\":0,\"post\":[{\"ID\":\"12\",\"post_author\":{\"ID\":\"5\",\"user_login\":\"longchen\",\"user_pass\":\"662e0103accf08eecbefc3d51302d182\",\"user_nickname\":\"\\u6f58\\u5b89\",\"user_img\":\"\",\"user_phone\":\"15574968443\",\"user_email\":\"wslongchen@qq.com\",\"user_url\":\"\",\"user_registered\":\"2016-04-09 14:44:08\",\"user_activation_key\":\"\",\"user_status\":\"0\",\"user_display_name\":\"\"},\"post_date\":\"2016-04-09 14:49:42\",\"post_content\":\"\\u5475\\u5475\",\"post_titile\":\"\",\"post_imgs\":null,\"post_status\":\"0\",\"post_password\":\"\",\"post_guid\":\"\",\"post_type\":\"0\",\"post_comment_status\":\"0\",\"post_comment_count\":\"0\"}]}",DreamPosts.class);
                                dreams=(DreamPosts) GsonUtils.getEntity(msg.obj.toString().trim(), DreamPosts.class);
                                List<Dream> dreamList=dreams.getPost();
                                //CacheUtils.saveHttpCache(Config.DIR_CACHE_PATH, "all_dream", dreams);
                                showData();
                                break;
                            default:
                                break;
                        }
                    }
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onClick(View v) {

    }

    class DreamHttpResponseCallBack implements HttpResponseCallBack {
        private int position;

        public DreamHttpResponseCallBack(int position) {
            super();
            this.position = position;

        }

        @Override
        public void onSuccess(String url, String result) {

            MyLog.i("all", result);
            Message msg = new Message();
            msg.arg1 = Config.DATA_SHOW;
            msg.obj=result;
            msg.arg2=position;
            handler.sendMessage(msg);
        }

        @Override
        public void onFailure(int httpResponseCode, int errCode, String err) {
            MyLog.i("all2", err);
            if(httpResponseCode==-1)
            {

            }
        }
    }
}

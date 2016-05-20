package com.example.mrpan.dreamtogether.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.mrpan.dreamtogether.OtherActivity;
import com.example.mrpan.dreamtogether.R;
import com.example.mrpan.dreamtogether.adapter.WorldCircleListAdapter;
import com.example.mrpan.dreamtogether.entity.Dream;
import com.example.mrpan.dreamtogether.entity.DreamPosts;
import com.example.mrpan.dreamtogether.http.HttpHelper;
import com.example.mrpan.dreamtogether.http.HttpResponseCallBack;
import com.example.mrpan.dreamtogether.utils.CacheUtils;
import com.example.mrpan.dreamtogether.utils.Config;
import com.example.mrpan.dreamtogether.utils.DateUtils;
import com.example.mrpan.dreamtogether.utils.GsonUtils;
import com.example.mrpan.dreamtogether.utils.MyLog;
import com.example.mrpan.dreamtogether.view.AnimRecycleView.view.AnimRefreshRecyclerView;
import com.example.mrpan.dreamtogether.view.AnimRecycleView.view.decoration.DividerItemDecoration;
import com.example.mrpan.dreamtogether.view.AnimRecycleView.view.manage.AnimRefreshLinearLayoutManager;
import com.example.mrpan.dreamtogether.view.TitleBar;

import java.util.List;

/**
 * Created by mrpan on 16/3/17.
 */
public class WorldCircleFragment extends Fragment implements View.OnClickListener{

    public final static String TAG="WorldCircle";

    private View currentView=null;

    //private RecyclerView recyclerView;
    private AnimRefreshRecyclerView recyclerView;

    private HttpHelper httpHelper;

    private DreamPosts dreams;

    private TitleBar titleBar;

    private Context context;
    private  WorldCircleListAdapter worldCircleListAdapter;
    private boolean isFresh=false;

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
        titleBar.setBgColor(getResources().getColor(R.color.dreamBlack));
        titleBar.showRight("梦想圈", R.mipmap.xiuxiu, this);
        context=getActivity();
        recyclerView=(AnimRefreshRecyclerView)currentView.findViewById(R.id.dream_list);
        //recyclerView=(AnimRefreshRecyclerView)currentView.findViewById(R.id.dream_list);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // 设置ItemAnimator
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
        // 设置固定大小
        //recyclerView.setHasFixedSize(true);
        //headerView = LayoutInflater.from(getActivity()).inflate(R.layout.recyle_header_view, null);
        // 脚部
        //footerView = LayoutInflater.from(getActivity()).inflate(R.layout.recyle_footer_view, null);
        // 使用重写后的线性布局管理器
        AnimRefreshLinearLayoutManager manager = new AnimRefreshLinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), manager.getOrientation(), true));
//            // 添加头部和脚部，如果不添加就使用默认的头部和脚部//
        //recyclerView.addHeaderView(headerView);
            // 设置头部的最大拉伸倍率，默认1.5f，必须写在setHeaderImage()之前
//            recyclerView.setScaleRatio    (1.7f);
//            // 设置下拉时拉伸的图片，不设置就使用默认的
//              recyclerView.setHeaderImage((ImageView) headerView.findViewById(R.id.iv_hander));
//            recyclerView.addFootView(footerView);
        // 设置刷新动画的颜色
        //recyclerView.setColor(Color.RED, Color.BLUE);
        // 设置头部恢复动画的执行时间，默认500毫秒
        recyclerView.setHeaderImageDurationMillis(500);
        // 设置拉伸到最高时头部的透明度，默认0.5f
        recyclerView.setHeaderImageMinAlpha(0.6f);

        DreamPosts cache= (DreamPosts) CacheUtils.readHttpCache(Config.DIR_CACHE_PATH, "all_dream");
        if(cache!=null)
        {
            dreams=cache;
//            if(cache.getPost().size()>0)
//                dreams=cache;

            showData(cache);
            //     List<Dream> ps=cache.getPost();
            //    MyLog.i("world","not null"+ps.size());
        }

        // 设置刷新和加载更多数据的监听，分别在onRefresh()和onLoadMore()方法中执行刷新和加载更多操作
        recyclerView.setLoadDataListener(new AnimRefreshRecyclerView.LoadDataListener() {
            @Override
            public void onRefresh() {
                new Thread(new MyRunnable(true)).start();
                //newData();
            }

            @Override
            public void onLoadMore() {
                new Thread(new MyRunnable(false)).start();
               //addData();
            }
        });


        recyclerView.setRefresh(true);
        isFresh=true;
        httpHelper=HttpHelper.getInstance();
        httpHelper.asyHttpGetRequest(Config.REQUEST_ALL_DREAM, new DreamHttpResponseCallBack(Config.ALL_DREAM));


    }

    private void showData(final DreamPosts dreamPosts){
        if(dreamPosts!=null&&dreamPosts.getPost().size()>0){
            worldCircleListAdapter=new WorldCircleListAdapter(getActivity(),dreamPosts.getPost());
            worldCircleListAdapter.setOnItemClickListener(new WorldCircleListAdapter.MyItemClickListener() {
                @Override
                public void onItemClick(View view, int postion) {
                    Intent intent=new Intent();
                    Bundle bundle=new Bundle();
                    bundle.putInt("type", Config.DREAM_DETAILS_TYPE);
                    Dream dream=dreams.getPost().get(postion-1);
                    bundle.putSerializable("data",dream);
                    //Toast.makeText(context,""+dream.getPost_content(),Toast.LENGTH_LONG).show();
                  intent.putExtras(bundle);
                  intent.setClass(context, OtherActivity.class);
                  startActivity(intent);
                  getActivity().overridePendingTransition(R.anim.top_in, R.anim.top_out);
                }
            });
            recyclerView.setAdapter(worldCircleListAdapter);
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }


    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case Config.HTTP_REQUEST_SUCCESS:
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
                               // DreamPosts dream= (DreamPosts) GsonUtils.getEntity("{\"ret\":0,\"post\":[{\"ID\":\"12\",\"post_author\":{\"ID\":\"5\",\"user_login\":\"longchen\",\"user_pass\":\"662e0103accf08eecbefc3d51302d182\",\"user_nickname\":\"\\u6f58\\u5b89\",\"user_img\":\"\",\"user_phone\":\"15574968443\",\"user_email\":\"wslongchen@qq.com\",\"user_url\":\"\",\"user_registered\":\"2016-04-09 14:44:08\",\"user_activation_key\":\"\",\"user_status\":\"0\",\"user_display_name\":\"\"},\"post_date\":\"2016-04-09 14:49:42\",\"post_content\":\"\\u5475\\u5475\",\"post_titile\":\"\",\"post_imgs\":null,\"post_status\":\"0\",\"post_password\":\"\",\"post_guid\":\"\",\"post_type\":\"0\",\"post_comment_status\":\"0\",\"post_comment_count\":\"0\"}]}",DreamPosts.class);
                                dreams=(DreamPosts) GsonUtils.getEntity(msg.obj.toString().trim(), DreamPosts.class);
                                //List<Dream> dreamList=dreams.getPost();
                                CacheUtils.saveHttpCache(Config.DIR_CACHE_PATH, "all_dream", dreams);
                                if(isFresh) {
                                    if(recyclerView!=null) {
                                        refreshComplate();
                                        recyclerView.refreshComplate();
                                    }
                                    // 刷新完成后调用，必须在UI线程中
                                   // recyclerView.refreshComplate();
                                }
                                showData(dreams);
                                break;
                            case Config.SHOW_NEXT:
                                DreamPosts dreamNewLists=(DreamPosts) GsonUtils.getEntity(msg.obj.toString().trim(), DreamPosts.class);
                                if(dreamNewLists!=null){
                                    dreams.getPost().addAll(dreamNewLists.getPost());
                                    CacheUtils.saveHttpCache(Config.DIR_CACHE_PATH, "all_dream", dreams);
                                }
                                showData(dreams);
                                loadMoreComplate();
                                // 加载更多完成后调用，必须在UI线程中
                                recyclerView.loadMoreComplate();
                                break;
                            default:
                                break;
                        }
                    }
                    break;
                case Config.HTTP_REQUEST_ERROR:
                    Toast.makeText(context, "联网失败！", Toast.LENGTH_LONG).show();
                    if(isFresh){
                        recyclerView.refreshComplate();
                    }
                    //recyclerView.setRefresh(false);

                    recyclerView.loadMoreComplate();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.titleBarRightImage:
                //clickcount++;
                Intent intent=new Intent();
                Bundle bundle=new Bundle();
                bundle.putInt("type", Config.XIUXIU_TYPE);
                intent.putExtras(bundle);
                intent.setClass(context, OtherActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.top_in, R.anim.top_out);
//                List<Integer> it=new ArrayList<>();
//                for(int i=0;i<100;i++){
//                    it.add(i);
//                }
//                List<Integer> integers=getRand(it,1);
//                if(integers.get(0)==0){
//                    Toast.makeText(context,"你点击了"+clickcount+"次，才摇到"+integers.get(0),Toast.LENGTH_LONG).show();
//                    clickcount=0;
//                }
                break;
            default:
                break;
        }
    }

    class MyRunnable implements Runnable {

        boolean isRefresh;

        public MyRunnable(boolean isRefresh) {
            this.isRefresh = isRefresh;
        }

        @Override
        public void run() {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isRefresh) {
                        newData();
                        refreshComplate();
                        // 刷新完成后调用，必须在UI线程中
                        recyclerView.refreshComplate();
                    } else {
                        addData();
                        loadMoreComplate();
                        recyclerView.loadMoreComplate();
                    }
                }
            }, 2000);
        }
    }

    public void refreshComplate() {
        if(recyclerView!=null){
            recyclerView.getAdapter().notifyDataSetChanged();
        }

    }

    public void loadMoreComplate() {
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    /**
     * 添加数据
     */
    private void addData() {
        httpHelper.asyHttpGetRequest(Config.GetNextDream(dreams.getPost().size()), new DreamHttpResponseCallBack(Config.SHOW_NEXT));
    }

    public void newData() {
        isFresh=true;
        httpHelper.asyHttpGetRequest(Config.REQUEST_ALL_DREAM, new DreamHttpResponseCallBack(Config.ALL_DREAM));
    }

    class DreamHttpResponseCallBack implements HttpResponseCallBack {
        private int position;

        public DreamHttpResponseCallBack(int position) {
            super();
            this.position = position;

        }

        @Override
        public void onSuccess(String url, String result) {

            Message message = new Message();
            message.arg1 = Config.HTTP_REQUEST_SUCCESS;
            message.arg2=position;
            message.obj = result;
            MyLog.i("HHH",result);
            handler.sendMessage(message);
        }

        @Override
        public void onFailure(int httpResponseCode, int errCode, String err) {
            Message message = new Message();
            message.arg1 = Config.HTTP_REQUEST_ERROR;
            handler.sendMessage(message);
        }
    }
}

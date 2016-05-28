package com.example.mrpan.dreamtogether.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.mrpan.dreamtogether.entity.Meta;
import com.example.mrpan.dreamtogether.entity.SiteInfoBean;
import com.example.mrpan.dreamtogether.http.HttpHelper;
import com.example.mrpan.dreamtogether.http.HttpResponseCallBack;
import com.example.mrpan.dreamtogether.http.HttpUtil;
import com.example.mrpan.dreamtogether.utils.CacheUtils;
import com.example.mrpan.dreamtogether.utils.Config;
import com.example.mrpan.dreamtogether.utils.DateUtils;
import com.example.mrpan.dreamtogether.utils.DialogUtils;
import com.example.mrpan.dreamtogether.utils.GsonUtils;
import com.example.mrpan.dreamtogether.utils.MyLog;
import com.example.mrpan.dreamtogether.utils.OtherUtils;
import com.example.mrpan.dreamtogether.view.AnimRecycleView.view.AnimRefreshRecyclerView;
import com.example.mrpan.dreamtogether.view.AnimRecycleView.view.decoration.DividerItemDecoration;
import com.example.mrpan.dreamtogether.view.AnimRecycleView.view.manage.AnimRefreshLinearLayoutManager;
import com.example.mrpan.dreamtogether.view.CustomProgressDialog;
import com.example.mrpan.dreamtogether.view.TitleBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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

    int current;

    private Context context;
    private  WorldCircleListAdapter worldCircleListAdapter;
    private boolean isFresh=false;
    HttpUtil fileFetch;

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

    //测试下载
    public void TestMethod()
    {
        try{
            SiteInfoBean bean = new SiteInfoBean("http://qd2.cache.baidupcs.com/file/9c10249845d3abdeda6a1df0102c4d90?bkt=p3-0000a2d94d1f06956fd4f4ab7bf1938cc7a7&xcode=3997050783b092cf8f887117b779155538b2461c09c82b090b2977702d3e6764&fid=237621224-250528-855846111132216&time=1464164845&sign=FDTAXGERLBH-DCb740ccc5511e5e8fedcff06b081203-OcmUNMshAqdxY8bmTuqO0j0tbKY%3D&to=qc2&fm=Nan,B,U,nc&sta_dx=55&sta_cs=0&sta_ft=exe&sta_ct=4&fm2=Nanjing02,B,U,nc&newver=1&newfm=1&secfm=1&flow_ver=3&pkey=0000a2d94d1f06956fd4f4ab7bf1938cc7a7&sl=74317902&expires=8h&rt=pr&r=174042734&mlogid=3378862890857777325&vuk=237621224&vbdid=272158236&fin=openfire_4_0_2.exe&slt=pm&uta=0&rtype=1&iv=0&isw=0&dp-logid=3378862890857777325&dp-callid=0.1.1", Config.DIR_CACHE_PATH,"weblogic60b2_win.exe",5);
            fileFetch = new HttpUtil(bean);
            fileFetch.start();


        }
        catch(Exception e){e.printStackTrace ();}
    }

    void initView(){
        titleBar=(TitleBar)currentView.findViewById(R.id.top_bar);
        titleBar.setBgColor(getResources().getColor(R.color.dreamBlack));
        titleBar.showRight("梦想圈", R.mipmap.xiuxiu2, this);
        context=getActivity();
        recyclerView=(AnimRefreshRecyclerView)currentView.findViewById(R.id.dream_list);
        // 使用重写后的线性布局管理器
        AnimRefreshLinearLayoutManager manager = new AnimRefreshLinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), manager.getOrientation(), true));
        // 设置头部恢复动画的执行时间，默认500毫秒
        recyclerView.setHeaderImageDurationMillis(500);
        // 设置拉伸到最高时头部的透明度，默认0.5f
        recyclerView.setHeaderImageMinAlpha(0.6f);

        DreamPosts cache= (DreamPosts) CacheUtils.readHttpCache(Config.DIR_CACHE_PATH, "all_dream");
        if(cache!=null)
        {
            dreams=cache;
            showData(cache);
        }

        // 设置刷新和加载更多数据的监听，分别在onRefresh()和onLoadMore()方法中执行刷新和加载更多操作
        recyclerView.setLoadDataListener(new AnimRefreshRecyclerView.LoadDataListener() {
            @Override
            public void onRefresh() {
                // new Thread(new MyRunnable(true)).start();
                newData();
            }

            @Override
            public void onLoadMore() {
                //new Thread(new MyRunnable(false)).start();
                addData();
            }
        });
        isFresh=true;
        httpHelper=HttpHelper.getInstance();
        recyclerView.setRefresh(true);


    }

    private void showData(final DreamPosts dreamPosts){
        if(dreamPosts!=null&&dreamPosts.getPost().size()>0){
            worldCircleListAdapter=new WorldCircleListAdapter(getActivity(),dreamPosts.getPost());
            worldCircleListAdapter.setOnItemClickListener(new WorldCircleListAdapter.MyItemClickListener() {
                @Override
                public void onItemClick(View view, int postion) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", Config.DREAM_DETAILS_TYPE);
                    Dream dream = dreams.getPost().get(postion - 1);
                    bundle.putSerializable("data", dream);
                    intent.putExtras(bundle);
                    intent.setClass(context, OtherActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.top_in, R.anim.top_out);
                }
            });
            worldCircleListAdapter.setRemoveListener(new WorldCircleListAdapter.RemoveItemListener() {
                @Override
                public void removeItem(View view, final int position) {

                    final int id = dreams.getPost().get(position).getID();
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("是否删除?");
                    builder.setTitle("提示");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            HttpHelper.getInstance().asyHttpGetRequest(Config.DeleteDreamByID(id), new HttpResponseCallBack() {
                                @Override
                                public void onSuccess(String url, String result) {
                                    int ret = 0;

                                    try {
                                        JSONObject jsonObject = new JSONObject(result.toString().replace("\uFEFF", ""));
                                        ret = jsonObject.getInt("ret");
                                        if (ret == Config.RESULT_RET_SUCCESS) {
                                            dreams.getPost().remove(position);
                                            Message message = new Message();
                                            message.arg1 = 1003;
                                            message.arg2 = position;
                                            handler.sendMessage(message);
                                            //Toast.makeText(mContext,"删除成功！",Toast.LENGTH_LONG).show();
                                        } else {
                                            //Toast.makeText(mContext, "删除失败！", Toast.LENGTH_LONG).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(int httpResponseCode, int errCode, String err) {

                                }
                            });
                        }
                    });

                    builder.setNegativeButton("取消",
                            new android.content.DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                    builder.create().show();
                }
            });
            recyclerView.setAdapter(worldCircleListAdapter);
            //recyclerView.getAdapter().notifyDataSetChanged();
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
                                //dreams.getPost().clear();

                                dreams=(DreamPosts) GsonUtils.getEntity(msg.obj.toString().trim(), DreamPosts.class);
                                //List<Dream> dreamList=dreams.getPost();
                                CacheUtils.saveHttpCache(Config.DIR_CACHE_PATH, "all_dream", dreams);
//                                if(isFresh) {
//                                    if(recyclerView!=null) {
//                                        refreshComplate();
//                                        recyclerView.refreshComplate();
//                                    }
//                                    // 刷新完成后调用，必须在UI线程中
//                                   // recyclerView.refreshComplate();
//                                }
                                refreshComplate();
                                // 刷新完成后调用，必须在UI线程中
                                recyclerView.refreshComplate();
                                //showData(dreams);
                                break;
                            case Config.SHOW_NEXT:
                                current=dreams.getPost().size();
                                DreamPosts dreamNewLists=(DreamPosts) GsonUtils.getEntity(msg.obj.toString().trim(), DreamPosts.class);
                                if(dreamNewLists.getPost().size()>0){
                                    dreams.getPost().addAll(dreamNewLists.getPost());
                                    //worldCircleListAdapter.addData(dreamNewLists.getPost());
                                    //showData(dreams);
                                    loadMoreComplate();
                                }

                                recyclerView.loadMoreComplate();
                                MyLog.i(TAG,dreams.getPost().size()+":size,newListSize:"+dreamNewLists.getPost().size()+",adapterSize:"+worldCircleListAdapter.getItemCount());
                                //showData(dreams);
                               // loadMoreComplate();
//                                // 加载更多完成后调用，必须在UI线程中
//                                recyclerView.loadMoreComplate();
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
                    }else{
                    //recyclerView.setRefresh(false);

                    recyclerView.loadMoreComplate();
                    }
                    break;
                case 1003:
                    worldCircleListAdapter.notifyDataSetChanged();
                    recyclerView.getAdapter().notifyDataSetChanged();
                    recyclerView.scrollToPosition(msg.arg2);
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
                Intent intent=new Intent();
                Bundle bundle=new Bundle();
                bundle.putInt("type", Config.XIUXIU_TYPE);
                intent.putExtras(bundle);
                intent.setClass(context, OtherActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.top_in, R.anim.top_out);
                break;
            default:
                break;
        }
    }

//    class MyRunnable implements Runnable {
//
//        boolean isRefresh;
//
//        public MyRunnable(boolean isRefresh) {
//            this.isRefresh = isRefresh;
//        }
//
//        @Override
//        public void run() {
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    if (isRefresh) {
//                        newData();
//                    } else {
//                        current=dreams.getPost().size();
//                        addData();
//                    }
//                }
//            }, 2000);
//        }
//    }

    public void refreshComplate() {
        if(recyclerView!=null){
            if(recyclerView.getAdapter()!=null){
                //recyclerView.getAdapter().notifyDataSetChanged();
                worldCircleListAdapter.notifyDataSetChanged();
            }
        }

    }

    public void loadMoreComplate() {
        //recyclerView.getAdapter().notifyDataSetChanged();
        worldCircleListAdapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(current);
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

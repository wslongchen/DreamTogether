package com.example.mrpan.dreamtogether.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mrpan.dreamtogether.MainActivity;
import com.example.mrpan.dreamtogether.OtherActivity;
import com.example.mrpan.dreamtogether.R;
import com.example.mrpan.dreamtogether.entity.Dream;
import com.example.mrpan.dreamtogether.entity.DreamPosts;
import com.example.mrpan.dreamtogether.entity.OneStatusEntity;
import com.example.mrpan.dreamtogether.entity.TwoStatusEntity;
import com.example.mrpan.dreamtogether.entity.User;
import com.example.mrpan.dreamtogether.entity.UserPosts;
import com.example.mrpan.dreamtogether.http.HttpHelper;
import com.example.mrpan.dreamtogether.http.HttpResponseCallBack;
import com.example.mrpan.dreamtogether.utils.CacheUtils;
import com.example.mrpan.dreamtogether.utils.Config;
import com.example.mrpan.dreamtogether.utils.GsonUtils;
import com.example.mrpan.dreamtogether.utils.MyLog;
import com.example.mrpan.dreamtogether.adapter.StatusExpandAdapter;
import com.example.mrpan.dreamtogether.adapter.TimeLineAdapter;
import com.example.mrpan.dreamtogether.utils.MySharePreference;
import com.example.mrpan.dreamtogether.view.TitleBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrpan on 16/3/26.
 */
public class UserDreamListFragment extends Fragment implements View.OnClickListener{
    
    public static final String TAG="UserDreamList";
    
    private View currentView;
    private Context context;

    private List<OneStatusEntity> oneList;
    private ExpandableListView expandlistView;
    private StatusExpandAdapter statusAdapter;
    private ListView dream_recent_list;

    private List<Dream> dreamList;

    private Button contanct;

    private TextView dream_count,dream_user;

    private int AuthorID=0;
    private User user;
    private TitleBar titleBar;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView=inflater.inflate(R.layout.user_dreamlist_recent, container, false);
        ViewGroup viewGroup= (ViewGroup) currentView.getParent();
        if(viewGroup!=null){
            viewGroup.removeView(currentView);
        }
        context=getActivity();

        //initView();

        init();

        return currentView;

    }

    private void init(){
        titleBar=(TitleBar)currentView.findViewById(R.id.top_bar);
        titleBar.showLeft("梦想",R.drawable.btn_back,this);
        dream_recent_list=(ListView)currentView.findViewById(R.id.dream_recent_list);
        dream_recent_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(context,"click",Toast.LENGTH_LONG).show();
            }
        });
        dream_user=(TextView)currentView.findViewById(R.id.dream_recent_user);
        dream_count=(TextView)currentView.findViewById(R.id.dream_recent_count);
        contanct=(Button)currentView.findViewById(R.id.contanct_dreamer);
        contanct.setOnClickListener(this);
        if(AuthorID==0){

        }else{
            HttpHelper.getInstance().asyHttpGetRequest(Config.GetDreamByAuthor(AuthorID), new RecentListHttpResponse(0));
            //HttpHelper.getInstance().asyHttpGetRequest(Config.GetUserByID(AuthorID), new RecentListHttpResponse(1));
        }


//        List<Dream> dreams=new ArrayList<>();
//        Dream dream;
//        dream=new Dream();
//        dream.setPost_content("我去，这是什么梦想！");
//        dreams.add(dream);
//        dream=new Dream();
//        dream.setPost_content("我去，这是什么梦想！");
//        dreams.add(dream);
//        dream=new Dream();
//        dream.setPost_content("我去，这是什么梦想！");
//        dreams.add(dream);
//        TimeLineAdapter timeLineAdapter=new TimeLineAdapter(dreams,context);
//        dream_recent_list.setAdapter(timeLineAdapter);
    }

    void showData(){
        if(dreamList!=null){
            TimeLineAdapter timeLineAdapter=new TimeLineAdapter(dreamList,context);
            dream_recent_list.setAdapter(timeLineAdapter);
        }
    }

    private void initView() {
        expandlistView = (ExpandableListView) currentView.findViewById(R.id.expandlist);
        putInitData();

        statusAdapter = new StatusExpandAdapter(context, oneList);
        expandlistView.setAdapter(statusAdapter);
        expandlistView.setGroupIndicator(null); // 去掉默认带的箭头

        // 遍历所有group,将所有项设置成默认展开
        int groupCount = expandlistView.getCount();
        for (int i = 0; i < groupCount; i++) {

        }
        expandlistView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                // TODO Auto-generated method stub
                if(expandlistView.isGroupExpanded(groupPosition))
                    expandlistView.collapseGroup(groupPosition);
                else
                {
                    expandlistView.expandGroup(groupPosition);
                }
                return true;
            }
        });

    }

    private void putInitData() {
        String[] strArray = new String[]{"贷款", "更名", "交接"};
        String[] str1 = new String[]{"经理送件", "银行送件审核", "银行评估", "买卖双方签约"};
        String[] str2 = new String[]{"更名", "划首付", "买方取产权证", "物业维修基金更名", "土地证更名"};
        String[] str3 = new String[]{"买方到银行抵押手续", "买方取他向权利证", "银行给卖方划尾款", "全部办结"};

        String[] timeStr1 = new String[]{"2013-11-02 13:16:22", "2013-11-02 13:16:22", "2013-11-02 13:16:22", "2013-11-02 13:16:22"};
        String[] timeStr2 = new String[]{"2013-11-02 13:16:22", "2013-11-02 13:16:22", "", "", ""};
        String[] timeStr3 = new String[]{"", "", "", ""};

        oneList = new ArrayList<OneStatusEntity>();
        for(int i=0 ; i<strArray.length ; i++){
            OneStatusEntity one = new OneStatusEntity();
            one.setStatusName(strArray[i]);
            List<TwoStatusEntity> twoList = new ArrayList<TwoStatusEntity>();
            String[] order = str1;
            String[] time = timeStr1;
            switch (i) {
                case 0:
                    order = str1;
                    time = timeStr1;
                    Log.i(TAG, "str1");
                    break;
                case 1:
                    order = str2;
                    time = timeStr2;
                    Log.i(TAG, "str2");
                    break;
                case 2:
                    order = str3;
                    time = timeStr3;
                    Log.i(TAG, "str3");
                    break;
            }

            for(int j=0 ; j<order.length ; j++){
                TwoStatusEntity two = new TwoStatusEntity();
                two.setStatusName(order[j]);
                if(time[j].equals("")){
                    two.setCompleteTime("暂无");
                    two.setIsfinished(false);
                }else{
                    two.setCompleteTime(time[j]+" 完成");
                    two.setIsfinished(true);
                }

                twoList.add(two);
            }
            one.setTwoList(twoList);
            oneList.add(one);
        }
        Log.i(TAG, "二级状态：" + oneList.get(0).getTwoList().get(0).getStatusName());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.titleBarLeftImage:
                getActivity().finish();
                break;
            case R.id.contanct_dreamer:
                FragmentTransaction transaction;
                String name=new MySharePreference(context).getString("username","");
                if(!name.equals("")){
                    transaction=getFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.anim.left_in, R.anim.left_out, R.anim.right_in, R.anim.right_out);
                    ((ChatFragment) OtherActivity.fragmentHashMap.get(ChatFragment.TAG)).setToUser(user);
                    transaction.replace(R.id.other_layout, OtherActivity.fragmentHashMap.get(ChatFragment.TAG));
                    transaction.addToBackStack(null);
                    transaction.commit();
                }else{
                    Toast.makeText(context,"请先登录",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    class RecentListHttpResponse implements HttpResponseCallBack {

        private int position;

        public RecentListHttpResponse(int position){
            this.position=position;
        }

        @Override
        public void onSuccess(String url, String result) {
            Message msg = new Message();
            msg.arg1 = Config.HTTP_REQUEST_SUCCESS;
            msg.arg2=position;
            msg.obj=result;
            handler.sendMessage(msg);
        }

        @Override
        public void onFailure(int httpResponseCode, int errCode, String err) {
            Message msg = new Message();
            msg.arg1 = Config.HTTP_REQUEST_ERROR;
            handler.sendMessage(msg);
        }
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1){
                case Config.HTTP_REQUEST_SUCCESS:
                    switch (msg.arg2){
                        case 0:
                            if(msg.obj!=null) {
                                try {
                                    DreamPosts dreams = (DreamPosts) GsonUtils.getEntity(msg.obj.toString(), DreamPosts.class);

                                    if (dreams.getRet() == Config.RESULT_RET_SUCCESS) {
                                        dreamList = dreams.getPost();
                                        user=dreams.getPost().get(0).getPost_author();
                                        dream_user.setText(dreams.getPost().get(0).getPost_author().getUser_nickname());
                                        dream_count.setText("一共发表了"+dreams.getPost().size()+"个梦想");
                                        showData();
                                    } else {
                                        Toast.makeText(context, "获取用户梦想失败！", Toast.LENGTH_LONG).show();
                                    }
                                }catch (Exception ex){
                                    Toast.makeText(context, "获取用户梦想失败！", Toast.LENGTH_LONG).show();
                                }

                            }
                            break;
                        case 1:
                            if(msg.obj!=null) {
                                try {
                                    UserPosts users = (UserPosts) GsonUtils.getEntity(msg.obj.toString(), UserPosts.class);

                                    if (users.getRet() == Config.RESULT_RET_SUCCESS) {
                                        user= users.getPost().get(0);
                                        dream_user.setText(user.getUser_nickname());
                                    } else {
                                        Toast.makeText(context, "获取用户信息失败！", Toast.LENGTH_LONG).show();
                                    }
                                }catch (Exception ex){
                                    Toast.makeText(context, "获取用户信息失败！", Toast.LENGTH_LONG).show();
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
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public int getAuthorID() {
        return AuthorID;
    }

    public void setAuthorID(int authorID) {
        AuthorID = authorID;
    }
}

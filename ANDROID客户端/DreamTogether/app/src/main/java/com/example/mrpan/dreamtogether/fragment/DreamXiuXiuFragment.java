package com.example.mrpan.dreamtogether.fragment;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mrpan.dreamtogether.OtherActivity;
import com.example.mrpan.dreamtogether.R;
import com.example.mrpan.dreamtogether.adapter.TestFragPagerAdapter;
import com.example.mrpan.dreamtogether.entity.Dream;
import com.example.mrpan.dreamtogether.entity.DreamPosts;
import com.example.mrpan.dreamtogether.http.HttpHelper;
import com.example.mrpan.dreamtogether.http.HttpResponseCallBack;
import com.example.mrpan.dreamtogether.utils.Config;
import com.example.mrpan.dreamtogether.utils.GsonUtils;
import com.example.mrpan.dreamtogether.view.LXiuXiu;
import com.example.mrpan.dreamtogether.view.TitleBar;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Vector;

/**
 * Created by mrpan on 16/4/29.
 */
public class DreamXiuXiuFragment extends Fragment implements View.OnClickListener{
    public final static String TAG="DreamXiuXiu";

    private Context context;

    private TitleBar titleBar;

    private View currentView;

    private LXiuXiu dreamXiu;

    private FragmentTransaction transaction;

    private TextView xiuxiutext;
    int clickcount=0;

    private SoundPool sp;//声明一个SoundPool
    private int[] music=new int[2];//定义一个整型用load（）；来设置suondID
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView=inflater.inflate(R.layout.dream_xiuxiu_fragment,container,false);
        ViewGroup viewGroup= (ViewGroup) currentView.getParent();
        if(viewGroup!=null){
            viewGroup.removeView(currentView);
        }
        context=getActivity();
        initView();
        return currentView;
    }

    private void initView(){
        xiuxiutext=(TextView)currentView.findViewById(R.id.xiuxiu_text);
        titleBar=(TitleBar)currentView.findViewById(R.id.top_bar);
        titleBar.showLeftStrAndRightStr("咻一咻", "关闭", "", this, this);
        titleBar.setBackgroundColor(getResources().getColor(R.color.dreamWhite));
        dreamXiu = (LXiuXiu) currentView.findViewById(R.id.dream_xiuxiu);
        dreamXiu.setWaveType(LXiuXiu.WaveType.SHADE);
        dreamXiu.setXiuXiuType(LXiuXiu.XiuXiuType.IN);
        dreamXiu.setImgRatio(0.2f);
        for(int i=0;i<10;i++){
            RandData.add(i);
        }
        final String[] texts=new String[]{"加油，就快咻到了～","哟，小伙子手速不够快嘛~","手速太慢了,咻不到了","咻都咻不到，还想要妹子～"};

        dreamXiu.setListener(new LXiuXiu.LXiuXiuOnClickListener() {
            @Override
            public void onXiu(View v) {
                List<Integer> item = getRand(RandData, 2);
                sp.play(music[0], 1, 1, 0, 0, 1);
                if (item.get(0) == 1) {

                    xiuxiutext.setText(texts[clickcount]);
                    clickcount++;
                    if(clickcount>=texts.length)
                        clickcount=0;
                    if(item.get(1) == 2) {
                        //Toast.makeText(context, "卧槽，你居然咻到了!", Toast.LENGTH_LONG).show();
                        HttpHelper.getInstance().asyHttpGetRequest(Config.REQUEST_RADOM_DREAM,new XiuxiuHttpListener(Config.XIUXIU_TYPE));
                        dreamXiu.setEnabled(false);
                        clickcount=0;
                    }
                }
            }
        });
        sp= new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);//第一个参数为同时播放数据流的最大个数，第二数据流类型，第三为声音质量
        music[0] = sp.load(getActivity(), R.raw.xiuxiu, 1); //把你的声音素材放到res/raw里，第2个参数即为资源文件，第3个为音乐的优先级
        music[1] = sp.load(getActivity(), R.raw.ding, 1);
    }

    private List<Integer> RandData=new ArrayList<>();


    public List<Integer> getRand(List<Integer> data, int m){
        int nSize=data.size();
        List<Integer> randData=new ArrayList<>();
        Random random=new Random();
        if(nSize<0 ||m<0)
            return null;
        for(int i=0,isSize=nSize;i<isSize;i++){
            float fRand=random.nextFloat();
            if(fRand<=(float)(m)/nSize){
                randData.add(data.get(i));
                m--;
            }
            nSize--;
        }
        return randData;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.titleBarLeftStr:
                getActivity().finish();
                break;
            default:
                break;
        }
    }

    class XiuxiuHttpListener implements HttpResponseCallBack {

        private int flag;

        public XiuxiuHttpListener(int flag){
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
                        case Config.XIUXIU_TYPE:
                            if(msg.obj!=null) {
                                int ret = 0;
                                try {
                                    JSONObject jsonObject = new JSONObject(msg.obj.toString().replace("\uFEFF", ""));
                                    ret = jsonObject.getInt("ret");

                                    if (ret == Config.RESULT_RET_SUCCESS) {
                                        sp.play(music[1], 1, 1, 0, 0, 1);
                                        DreamPosts dreamPosts = (DreamPosts) GsonUtils.getEntity(msg.obj.toString(), DreamPosts.class);
                                        List<Dream> dreams = dreamPosts.getPost();
                                        transaction = getFragmentManager().beginTransaction();
                                        transaction.setCustomAnimations(R.anim.left_in, R.anim.left_out, R.anim.right_in, R.anim.right_out);
                                        ((DreamRadomFragment)OtherActivity.fragmentHashMap.get(DreamRadomFragment.TAG)).setDreams(dreams);
                                        transaction.replace(R.id.other_layout, OtherActivity.fragmentHashMap.get(DreamRadomFragment.TAG));
                                        transaction.addToBackStack(null);
                                        transaction.commit();
                                        dreamXiu.setEnabled(true);
                                    } else {
                                        Toast.makeText(context, "再咻咻说不定能有！", Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(context, "再咻咻说不定能有！", Toast.LENGTH_LONG).show();
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
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
}

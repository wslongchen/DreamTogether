package com.example.mrpan.dreamtogether.fragment;

import android.app.Activity;
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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.example.mrpan.dreamtogether.OtherActivity;
import com.example.mrpan.dreamtogether.R;
import com.example.mrpan.dreamtogether.adapter.DreamImageGridAdapter;
import com.example.mrpan.dreamtogether.entity.Dream;
import com.example.mrpan.dreamtogether.entity.Location;
import com.example.mrpan.dreamtogether.entity.Share;
import com.example.mrpan.dreamtogether.http.HttpHelper;
import com.example.mrpan.dreamtogether.http.HttpResponseCallBack;
import com.example.mrpan.dreamtogether.utils.Config;
import com.example.mrpan.dreamtogether.utils.DateUtils;
import com.example.mrpan.dreamtogether.utils.MyLog;
import com.example.mrpan.dreamtogether.utils.OtherUtils;
import com.example.mrpan.dreamtogether.view.SharePopupWindows;
import com.example.mrpan.dreamtogether.view.TitleBar;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mrpan on 16/5/24.
 */
public class StarItemFragment extends Fragment {

    public static final String TAG="StarItem";

    private Context context;
    private View currentView;
    private Dream dream;

    private ImageView imageView,image_star,image_weather,image_xiaoji;
    private TextView temprature,dream_location,time,content,author;

    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    private BDLocation mLocation;

    private String image;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView=inflater.inflate(R.layout.xiaoji_fragment,container,false);
        ViewGroup viewGroup= (ViewGroup) currentView.getParent();
        if(viewGroup!=null){
            viewGroup.removeView(currentView);
        }
        context=getActivity();
        mLocationClient = new LocationClient(context);//声明LocationClient类

        initView();
        initLocation();
        mLocationClient.registerLocationListener(myListener);

        mLocationClient.start();
        initData();
        return currentView;
    }

    private void initData() {
        author.setText(dream.getPost_author().getUser_nickname());
        content.setText(dream.getPost_content());
        if (dream.getPost_imgs() != null && !dream.getPost_imgs().equals("")) {
            if (dream.getPost_imgs().length() > 0 && !dream.getPost_imgs().trim().isEmpty() && !dream.getPost_imgs().equals("")) {
                String[] imgs = dream.getPost_imgs().split(",");
                if (imgs.length > 0) {
                    ImageLoader.getInstance().displayImage("http://" + imgs[0], image_star);
                    image="http://" + imgs[0];
                }
            }
        } else {
            image_star.setImageResource(R.mipmap.bg_search);
        }
        time.setText(DateUtils.getDateStr(dream.getPost_date()));
        if (null != mLocation) {
            HttpHelper.getInstance().asyHttpGetRequest(Config.GET_WEATHER(mLocation.getCity()), new HttpResponseCallBack() {
                @Override
                public void onSuccess(String url, String result) {


                    Message message = new Message();
                    message.what = 1;
                    message.obj = result;
                    handler.sendMessage(message);

                }

                @Override
                public void onFailure(int httpResponseCode, int errCode, String err) {

                }
            });
        }
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString().replace("\uFEFF", ""));
                        int ret = jsonObject.getInt("status");
                        if(ret==1000){
                            JSONObject wendu=jsonObject.getJSONObject("data");

                            JSONArray info=wendu.getJSONArray("forecast");
                            temprature.setText(wendu.getString("wendu"));
                            String tianqi=info.getJSONObject(0).getString("type");
                            image_weather.setImageResource(OtherUtils.revertWeatherToImg(tianqi));
                            dream.setPost_guid(tianqi+","+wendu.getString("wendu")+","+mLocation.getCity());
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private void initView(){
        temprature=(TextView)currentView.findViewById(R.id.temprature);
        image_xiaoji=(ImageView)currentView.findViewById(R.id.image_xiaoji);
        image_weather=(ImageView)currentView.findViewById(R.id.image_weather);
        dream_location=(TextView)currentView.findViewById(R.id.dream_location);
        time=(TextView)currentView.findViewById(R.id.time);
        content=(TextView)currentView.findViewById(R.id.content);
        author=(TextView)currentView.findViewById(R.id.author);
        image_star=(ImageView)currentView.findViewById(R.id.image_star);
        imageView=(ImageView)currentView.findViewById(R.id.image_share);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Share share=new Share();
                share.setTITLE(dream.getPost_titile());
                share.setSUMMARY(dream.getPost_content());
                share.setAPP_NAME("梦想");
                share.setIMAGE_URL(image);
                share.setTARGET_URL("www.mrpann.com");
                new SharePopupWindows(context,currentView,2,share);
            }
        });
        image_xiaoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt("type", Config.SHARE_TYPE);
                bundle.putSerializable("data", dream);
                intent.putExtras(bundle);
                intent.setClass(context, OtherActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.top_in, R.anim.top_out);
            }
        });
    }

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        //int span=1000;
        //option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(false);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());// 单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            List<Poi> list = location.getPoiList();// POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            mLocation=location;
            dream_location.setText(location.getCity());
        }
    }

    public Dream getDream() {
        return dream;
    }

    public void setDream(Dream dream) {
        this.dream = dream;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null && mLocationClient.isStarted()){
            mLocationClient.stop();
            mLocationClient = null;
        }
    }
}

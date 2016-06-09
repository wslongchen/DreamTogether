package com.example.mrpan.dreamtogether.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.mrpan.dreamtogether.R;
import com.example.mrpan.dreamtogether.entity.Dream;
import com.example.mrpan.dreamtogether.entity.Location;
import com.example.mrpan.dreamtogether.entity.Meta;
import com.example.mrpan.dreamtogether.entity.Place;
import com.example.mrpan.dreamtogether.utils.GsonUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrpan on 16/6/6.
 */
public class DreamMapInfo extends Fragment {

    public static final String TAG="DreamMapInfo";
    private Context context;
    private View currentView;
    private List<Dream> dreams;

    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private MyLocationConfiguration.LocationMode mCurrentMode;
    boolean isFirstLoc = true; // 是否首次定位
    private BaiduMap baiduMap;
    private MapView mapView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView=inflater.inflate(R.layout.dream_mapinfo,container,false);
        ViewGroup viewGroup= (ViewGroup) currentView.getParent();
        if(viewGroup!=null){
            viewGroup.removeView(currentView);
        }
        context=getActivity();
        init();
        return currentView;
    }

    private void init() {
        mapView=(MapView)currentView.findViewById(R.id.dream_map);
        baiduMap=mapView.getMap();
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);
        baiduMap.setMapStatus(msu);
        //initOverlay();


        // 开启定位图层
        baiduMap.setMyLocationEnabled(true);

        // 隐藏logo
        View child = mapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)){
            child.setVisibility(View.INVISIBLE);
        }
        //地图上比例尺
        mapView.showScaleControl(false);
        // 隐藏缩放控件
        mapView.showZoomControls(false);

        // 定位初始化
        mLocClient = new LocationClient(context);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            public boolean onMarkerClick(final Marker marker) {
                TextView textView = new Button(context);
                Toast.makeText(context,"荒芜",Toast.LENGTH_SHORT).show();
                LatLng ll = marker.getPosition();
                return true;
            }
        });
    }

    private void initMarks(){
        List<Place> places=new ArrayList<>();
        for (Dream d: dreams) {
            List<Meta> metas=d.getMetas();
            if(metas.size()>0){
                for (Meta m:metas) {
                    if(m.getMeta_key().equals("location")){
                        Place p=(Place)GsonUtils.getEntity(m.getMeta_value().toString().trim(),Place.class);
                        MarkMap(Double.parseDouble(p.getLatitude()),Double.parseDouble(p.getLontitude()));
                    }
                }
            }
        }
    }

    public class MyLocationListenner implements BDLocationListener {


        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            baiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                baiduMap.animateMapStatus(u);
                initMarks();
            }

        }
    }

    private void MarkMap(double latitude,double longitude){
        //定义Maker坐标点
        LatLng point = new LatLng(latitude, longitude);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.ic_launcher);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        baiduMap.addOverlay(option);
    }

    public List<Dream> getDreams() {
        return dreams;
    }

    public void setDreams(List<Dream> dreams) {
        this.dreams = dreams;
    }
}

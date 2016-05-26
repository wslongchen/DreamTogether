package com.example.mrpan.dreamtogether;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.example.mrpan.dreamtogether.utils.Config;
import com.example.mrpan.dreamtogether.xmpp.XmppConnectionManager;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.L;
import com.nostra13.universalimageloader.utils.StorageUtils;

import org.jivesoftware.smack.XMPPConnection;

import java.io.File;


/**
 * Created by mrpan on 15/12/7.
 */
public class MyApplication extends Application {
    private static MyApplication instance;

    public static XMPPConnection xmppConnection;

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        // 在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现

        super.onCreate();
        instance = this;
        initImageLoader();
        SDKInitializer.initialize(getApplicationContext());
        //SDKInitializer.initialize(this);

    }

    void initImageLoader(){
        File cacheDir =StorageUtils.getOwnCacheDirectory(this,Config.DIR_IMAGE_PATH);
        DisplayImageOptions defaultOptions = new DisplayImageOptions
                .Builder()
                .showImageForEmptyUri(R.mipmap.logo)
                .showImageOnFail(R.mipmap.logo)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(getApplicationContext())
                .threadPoolSize(5)
                .threadPriority(Thread.MIN_PRIORITY + 3)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2000))
                .defaultDisplayImageOptions(defaultOptions)
                .diskCache(new UnlimitedDiskCache(cacheDir))
                .discCacheSize(50 * 1024 * 1024)//
                .discCacheFileCount(100)
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);
        if(Config.IS_DEBUG){
            L.disableLogging();
        }else{
            L.enableLogging();
        }
    }
}

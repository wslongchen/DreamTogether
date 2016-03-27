package com.example.mrpan.dreamtogether;

import android.app.Application;


/**
 * Created by mrpan on 15/12/7.
 */
public class MyApplication extends Application {
    private static MyApplication instance;

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
        //SDKInitializer.initialize(this);

    }
}

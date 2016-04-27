package com.example.mrpan.dreamtogether.view.AnimRecycleView.view.listener;

/**
 * Created by mrpan on 16/4/26.
 */
public interface OverScrollListener {

    /**
     * 滑动过度时调用的方法
     *
     * @param dy 每毫秒滑动的距离
     */
    void overScrollBy(int dy);

}
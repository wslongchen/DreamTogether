package com.example.mrpan.dreamtogether.view.AnimRecycleView.view.manage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

import com.example.mrpan.dreamtogether.view.AnimRecycleView.view.listener.OverScrollListener;

/**
 * Created by mrpan on 16/4/26.
 */
public class AnimRefreshStaggeredGridLayoutManager extends StaggeredGridLayoutManager {
    private OverScrollListener mListener;

    public AnimRefreshStaggeredGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public AnimRefreshStaggeredGridLayoutManager(int spanCount, int orientation) {
        super(spanCount, orientation);
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int scrollRange = super.scrollVerticallyBy(dy, recycler, state);

        mListener.overScrollBy(dy - scrollRange);

        return scrollRange;
    }

    /**
     * 设置滑动过度监听
     *
     * @param listener
     */
    public void setOverScrollListener(OverScrollListener listener) {
        mListener = listener;
    }

}

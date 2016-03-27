package com.example.mrpan.dreamtogether.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;

public class OverScrollWarpLayout extends LinearLayout {

    private static final float OVERSHOOT_TENSION = 0.75f;

    private Scroller mScroller;

    public OverScrollWarpLayout(Context context, AttributeSet attr) {
        super(context, attr);
        this.setOrientation(LinearLayout.VERTICAL);
        mScroller = new Scroller(getContext(), new OvershootInterpolator(OVERSHOOT_TENSION));
    }

    public OverScrollWarpLayout(Context context) {
        super(context);
        this.setOrientation(LinearLayout.VERTICAL);
        mScroller = new Scroller(getContext(), new OvershootInterpolator(OVERSHOOT_TENSION));
    }

    public void smoothScrollTo(int fx, int fy) {
        int dx = fx - mScroller.getFinalX();
        int dy = fy - mScroller.getFinalY();
        smoothScrollBy(dx, dy);
    }

    public void smoothScrollBy(int dx, int dy) {

        mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx, dy);
        invalidate();
    }

    @Override
    public void computeScroll() {

        if (mScroller.computeScrollOffset()) {

            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());

            postInvalidate();
        }
        super.computeScroll();
    }

    public final void smoothScrollToNormal() {
        smoothScrollTo(0, 0);
    }

    public final int getScrollerCurrY() {
        return mScroller.getCurrY();
    }
}
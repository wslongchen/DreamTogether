package com.example.mrpan.dreamtogether.view;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;


public class OverScrollView extends ScrollView {

	private static final float ELASTICITY_COEFFICIENT = 0.25f;

	private static final int NO_OVERSCROLL_STATE = 0;

	private static final int TOP_OVERSCROLL_STATE = 1;

	private static final int BOTTOM_OVERSCROLL_STATE = 2;

	private static final int OVERSCROLL_MAX_HEIGHT = 1200;
	
	/**
     * Sentinel value for no current active pointer.
     * Used by {@link #mActivePointerId}.
     */
    private static final int INVALID_POINTER = -1;

    private static final int TRIGGER_HEIGHT = 120;

	private int overScrollSate;

	private boolean mIsUseOverScroll = true;

	private boolean isRecord;

	private OverScrollWarpLayout mContentLayout;

	private OverScrollListener mOverScrollListener;

	private OverScrollTinyListener mOverScrollTinyListener;

	private OnScrollListener mScrollListener;

	private float mLastMotionY;

	private int overScrollDistance;

	private int mActivePointerId = INVALID_POINTER;

	private boolean isOnTouch;

	private boolean isInertance;

	private boolean mIsUseInertance = true;

	private boolean mIsBanQuickScroll;

	private int inertanceY;

	private int mOverScrollTrigger = TRIGGER_HEIGHT;
	
	public OverScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initScrollView();
	}

	public OverScrollView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public OverScrollView(Context context) {
		this(context, null);
	}

	private void initScrollView(){
		if(Build.VERSION.SDK_INT >= 9){
			setOverScrollMode(View.OVER_SCROLL_NEVER);
		}else{
			ViewCompat.setOverScrollMode(this, ViewCompat.OVER_SCROLL_NEVER);
		}
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if(!mIsUseOverScroll){
			return super.onInterceptTouchEvent(ev);
		}
		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (isOverScrolled()) {
					isRecord = true;
					// Remember where the motion event started
					mLastMotionY = (int) ev.getY();
					
					mActivePointerId = ev.getPointerId(0);
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if(isRecord && Math.abs(ev.getY() - mLastMotionY) > 20){
					return true;
				}
				break;
			case MotionEvent.ACTION_CANCEL:
				if(isRecord){
					isRecord = false;
				}
		}
		return super.onInterceptTouchEvent(ev);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		isOnTouch = true;
		if(ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL){
			if(mOverScrollTinyListener != null){
				mOverScrollTinyListener.scrollLoosen();
			}
			isOnTouch = false;
		}

		if(!mIsUseOverScroll){
			return super.onTouchEvent(ev);
		}
		
		if(!isOverScrolled()){
			mLastMotionY = (int) ev.getY();
			return super.onTouchEvent(ev);
		}
		
		switch (ev.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			mActivePointerId = ev.getPointerId(0);
			mLastMotionY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_POINTER_DOWN: 
            final int index = ev.getActionIndex();
            mLastMotionY = (int) ev.getY(index);
            mActivePointerId = ev.getPointerId(index);
            break;
        case MotionEvent.ACTION_POINTER_UP:
        	onSecondaryPointerUp(ev);
        	if(mActivePointerId != INVALID_POINTER){
        		mLastMotionY = (int) ev.getY(ev.findPointerIndex(mActivePointerId));
        	}
            break;
		case MotionEvent.ACTION_MOVE:
			if (isRecord) {
				final int activePointerIndex = ev.findPointerIndex(mActivePointerId);
                if (activePointerIndex == -1) {
                    break;
                }
				
                
				final float y = ev.getY(activePointerIndex);
				int deltaY = (int) (mLastMotionY - y);
				mLastMotionY = y;
				
				if(Math.abs(overScrollDistance) >= OVERSCROLL_MAX_HEIGHT && overScrollDistance * deltaY > 0){
					deltaY = 0;
				}

				if(overScrollDistance *(overScrollDistance + deltaY) < 0){
					mContentLayout.smoothScrollToNormal();
					overScrollDistance = 0;
					break;
				}

				if((!isOnBottom() && overScrollDistance > 0) || (!isOnTop() && overScrollDistance < 0)){
					mContentLayout.smoothScrollToNormal();
					overScrollDistance = 0;
					break;
				}
				
				if(overScrollDistance * deltaY > 0){
					deltaY = (int) (deltaY * ELASTICITY_COEFFICIENT);
				}
				
				if(overScrollDistance == 0){
					deltaY = (int) (deltaY * ELASTICITY_COEFFICIENT * 0.5f);
				}
				
				if(overScrollDistance == 0 && deltaY == 0){
					break;
				}

				if(Math.abs(deltaY) > 20){
					deltaY = deltaY > 0 ? 20 : -20;
				}

				overScrollDistance += deltaY;
				
				if(isOnTop() && overScrollDistance > 0 && !isOnBottom()){
					overScrollDistance = 0;
					break;
				}
				
				if(isOnBottom() && overScrollDistance < 0 && !isOnTop()){
					overScrollDistance = 0;
					break;
				}

				mContentLayout.smoothScrollBy(0, deltaY);
				
				if(mOverScrollTinyListener != null){
					mOverScrollTinyListener.scrollDistance(deltaY, overScrollDistance);
				}
				return true;
			}
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			mContentLayout.smoothScrollToNormal();
			overScrollTrigger();
			overScrollDistance = 0;
			isRecord = false;
			mActivePointerId = INVALID_POINTER;
			break;
			
		default:
			break;
		}
		return super.onTouchEvent(ev);
	}

	private void onSecondaryPointerUp(MotionEvent ev) {
		final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
		final int pointerId = ev.getPointerId(pointerIndex);
		if (pointerId == mActivePointerId) {
			// This was our active pointer going up. Choose a new
			// active pointer and adjust accordingly.
			// TODO: Make this decision more intelligent.
			final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
			mLastMotionY = (int) ev.getY(newPointerIndex);
			mActivePointerId = ev.getPointerId(newPointerIndex);
		}
		
	}

	public boolean isOverScrolled() {
		return isOnTop() || isOnBottom();
	}
	
	private boolean isOnTop(){
		return getScrollY() == 0;
	}
	
	private boolean isOnBottom(){
		return getScrollY() + getHeight() == mContentLayout.getHeight();
	}

	private void initOverScrollLayout() {
		setFillViewport(true);
		if(mContentLayout == null){
			View child = getChildAt(0);
			mContentLayout = new OverScrollWarpLayout(getContext());
			this.removeAllViews();
			mContentLayout.addView(child);
			this.addView(mContentLayout,new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		}
//		mIsUseOverScroll = true;
	}

	public void setOverScroll(boolean isOverScroll){
		mIsUseOverScroll = isOverScroll;
	}

	public void setUseInertance(boolean isInertance){
		mIsUseInertance = isInertance;
	}
	
	@Override
	protected void onAttachedToWindow() {
		initOverScrollLayout();
		super.onAttachedToWindow();
	}

	public int getScrollState(){
		invalidateState();
		return overScrollSate;
	}

	private void invalidateState(){
		
		if(mContentLayout.getScrollerCurrY() == 0){
			overScrollSate =  NO_OVERSCROLL_STATE;
		}
		
		if(mContentLayout.getScrollerCurrY() < 0){
			overScrollSate =  TOP_OVERSCROLL_STATE;
		}
		
		if(mContentLayout.getScrollerCurrY() > 0){
			overScrollSate =  BOTTOM_OVERSCROLL_STATE;
		}
	}
	
	@Override
	protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
			int scrollY, int scrollRangeX, int scrollRangeY,
			int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
//		Log.v("test", "deltaY "+deltaY+"   scrollY "+scrollY);
		return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
				scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
	}
	
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		if(mScrollListener != null && overScrollDistance == 0){
			mScrollListener.onScroll(l, t, oldl, oldt);
		}
		super.onScrollChanged(l, t, oldl, oldt);
	}
	
	@Override
	protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX,
			boolean clampedY) {
		if(mIsUseInertance && !isInertance && scrollY != 0){
			isInertance = true;
		}
		if(clampedY && !isOnTouch && isInertance){
			mContentLayout.smoothScrollBy(0, inertanceY);
			mContentLayout.smoothScrollToNormal();
			inertanceY = 0;
		}
		super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
	}

	public void setOverScrollListener(OverScrollListener listener){
		mOverScrollListener = listener;
	}

	public void setOverScrollTinyListener(OverScrollTinyListener listener){
		mOverScrollTinyListener = listener;
	}

	public void setOnScrollListener(OnScrollListener listener){
		mScrollListener = listener;
	}

	public void setOverScrollTrigger(int height){
		if(height >= 30){
			mOverScrollTrigger = height;
		}
	}
	
	private void overScrollTrigger(){
		if(mOverScrollListener == null){
			return;
		}
		
		if(overScrollDistance > mOverScrollTrigger && isOnBottom()){
			mOverScrollListener.footerScroll();
		}
		
		if(overScrollDistance < -mOverScrollTrigger && isOnTop()){
			mOverScrollListener.headerScroll();
		}
		
	}
	
	public void setQuickScroll(boolean isEnable){
		mIsBanQuickScroll = !isEnable;
	}
	
	@Override
	public void computeScroll() {
		if(!mIsBanQuickScroll){
			super.computeScroll();
		}
	}

	public int getScrollHeight(){
		return mContentLayout.getHeight() - getHeight();
	}
	
	@Override
	public void fling(int velocityY) {
		inertanceY = 50 * velocityY / 5000;
		super.fling(velocityY);
	}
	

	public interface OverScrollListener {

		void headerScroll();

		void footerScroll();
		
	}

	public interface OverScrollTinyListener{

		void scrollDistance(int tinyDistance, int totalDistance);

        void scrollLoosen();
	}
	

	public interface OnScrollListener{
		void onScroll(int l, int t, int oldl, int oldt);
	}
}

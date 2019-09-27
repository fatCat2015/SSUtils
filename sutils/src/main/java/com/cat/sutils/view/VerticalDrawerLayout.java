package com.cat.sutils.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.cat.sutils.R;


public class VerticalDrawerLayout extends ViewGroup implements VerticalDrawerAction {
    
    String TAG="VerticalDrawerLayout";


    private float dimRatio;
    private View vContent;
    private int drawerHandleViewId;
    private View vDrawerHandle;
    private View vDrawer;
    private View vLayer;

    private ViewDragHelper viewDragHelper;
    private DrawerListener drawerListener;

    private float slidePercent;

    private static final int FLING_VELOCITY_SLOP=800;



    public VerticalDrawerLayout(Context context) {
        super(context);
        obtainAttrs(context,null );
    }

    public VerticalDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        obtainAttrs(context,attrs);
    }

    public VerticalDrawerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainAttrs(context,attrs);
    }

    private void obtainAttrs(Context context,AttributeSet attributeSet){
        TypedArray typedArray= context.obtainStyledAttributes(attributeSet, R.styleable.VerticalDrawerLayout);
        dimRatio=typedArray.getFloat(R.styleable.VerticalDrawerLayout_dim_ratio,0.4F);
        drawerHandleViewId=typedArray.getResourceId(R.styleable.VerticalDrawerLayout_drawer_handle_id,View.NO_ID);
        typedArray.recycle();
    }




    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if(getChildCount()!=2){
            throw new IllegalStateException("VerticalDrawerLayout must host two direct child");
        }
        vContent=  getChildAt(0);
        vDrawer=  getChildAt(1);
        vDrawerHandle=vDrawer.findViewById(drawerHandleViewId);
        addContentLayer();
    }


    private void addContentLayer(){
        addView(vLayer=new View(getContext()),1,vContent.getLayoutParams());
        vLayer.setBackgroundColor(Color.argb((int)(255*dimRatio),0,0,0));
        vLayer.setAlpha(0);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        viewDragHelper=ViewDragHelper.create(this,callback);
        if(vDrawerHandle==null){
            viewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_BOTTOM);
        }
    }

    private ViewDragHelper.Callback callback=new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(@NonNull View child, int pointerId) {
            return child==vDrawer;
        }

        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            super.onEdgeDragStarted(edgeFlags, pointerId);
            viewDragHelper.captureChildView(vDrawer,pointerId);
        }

        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            int drawerMaxTop=vDrawerHandle==null?getHeight():getHeight()-vDrawerHandle.getHeight();
            int drawerMinTop=getHeight()-child.getHeight();
            return Math.min(drawerMaxTop,Math.max(top,drawerMinTop));
        }
        @Override
        public int getViewVerticalDragRange(@NonNull View child) {
            return vDrawerHandle==null?child.getHeight():child.getHeight()-vDrawerHandle.getHeight();
        }

        @Override
        public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
            slidePercent=1-(changedView.getTop()+changedView.getHeight()-getHeight())*1.0F/(changedView.getHeight()-(vDrawerHandle==null?0:vDrawerHandle.getHeight()));
            vLayer.setAlpha(slidePercent);
            if(drawerListener!=null){
                drawerListener.onDrawerSlide(slidePercent);
            }
        }

        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            boolean openFlag=verifyOpenOrNot(releasedChild,yvel);
            int destinationY;
            if(openFlag){
                destinationY=getHeight()-vDrawer.getHeight();
            }else{
                destinationY=getHeight()-(vDrawerHandle==null?0:vDrawerHandle.getHeight());
            }
            if(viewDragHelper.settleCapturedViewAt(0,destinationY)){
                invalidate();
            }
        }

        private boolean verifyOpenOrNot(View releasedChild,float yvel){
            boolean open;
            if(Math.abs(yvel)>FLING_VELOCITY_SLOP){
                if(yvel>0){
                    open=false;
                }else{
                    open=true;
                }
            }else{
                if(releasedChild.getTop()>=getHeight()-releasedChild.getHeight()/2){
                    open=false;
                }else{
                    open=true;
                }

            }
            return open;
        }
    };




    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize=MeasureSpec.getSize(widthMeasureSpec);
        int heightSize=MeasureSpec.getSize(heightMeasureSpec);
        vContent.measure(widthMeasureSpec,heightMeasureSpec);
        vLayer.measure(widthMeasureSpec,heightMeasureSpec);
        LayoutParams layoutParams=vDrawer.getLayoutParams();
        if(layoutParams.height>0){
            vDrawer.measure(widthMeasureSpec,MeasureSpec.makeMeasureSpec(layoutParams.height,MeasureSpec.EXACTLY));
        }else{
            vDrawer.measure(widthMeasureSpec,heightMeasureSpec);
        }
        setMeasuredDimension(widthSize,heightSize);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(changed){
            vContent.layout(0,0,getMeasuredWidth(),getMeasuredHeight());
            vLayer.layout(0,0,getMeasuredWidth(),getMeasuredHeight());
            int drawerTop=vDrawerHandle==null?getMeasuredHeight():getMeasuredHeight()-vDrawerHandle.getMeasuredHeight();
            vDrawer.layout(0,drawerTop,getMeasuredWidth(),drawerTop+vDrawer.getMeasuredHeight());
        }
    }


    public final void setDrawerListener(DrawerListener drawerListener) {
        this.drawerListener = drawerListener;
    }


    @Override
    public final void open(){
        if(viewDragHelper.smoothSlideViewTo(vDrawer,0,getHeight()-vDrawer.getHeight())){
            invalidate();
        }
    }

    @Override
    public final void close(){
        if(viewDragHelper.smoothSlideViewTo(vDrawer,0,vDrawerHandle==null?getHeight():getHeight()-vDrawerHandle.getHeight())){
            invalidate();
        }
    }

    @Override
    public final boolean handleBackPressed(){
        if (isOpened()) {
            close();
            return true;
        }
        return false;
    }


    private boolean isOpened(){
        return slidePercent==1;
    }

    private boolean isClosed(){
        return slidePercent==0;
    }



    @Override
    public void computeScroll() {
        if(viewDragHelper.continueSettling(true) ){
            invalidate();
        }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(isOpened()&&touchTopOfDrawer(ev)){  //drawer打开状态下 触摸drawer以上的区域 关闭drawer 同时拦截事件
            close();
            return true;
        }
        boolean intercept = viewDragHelper.shouldInterceptTouchEvent(ev);
        return intercept;
    }



    private boolean touchTopOfDrawer(MotionEvent ev){
        float x=ev.getX();
        float y=ev.getY();
        return y<vDrawer.getTop();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG, "onTouchEvent: ");
        boolean touchOutOfDrawerWhenClosed=vDrawerHandle!=null&&isClosed()&&touchOutOfDrawer(event);
        if(!touchOutOfDrawerWhenClosed){
            viewDragHelper.processTouchEvent(event);
        }
        return true;
    }

    private boolean touchOutOfDrawer(MotionEvent ev){
        float x=ev.getX();
        float y=ev.getY();
        if(vDrawerHandle==null){
            return y<vDrawer.getTop();
        }else{
            boolean result=y<vDrawer.getTop()||
                    (x<vDrawerHandle.getLeft()&&y<vDrawer.getTop()+vDrawerHandle.getHeight())||
                    (x>vDrawerHandle.getRight()&&y<vDrawer.getTop()+vDrawerHandle.getHeight());
            return result;
        }
    }



    public interface DrawerListener{
        void onDrawerSlide(float slidePercent);
    }






}

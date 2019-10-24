package com.cat.sutils.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.icu.util.IndianCalendar;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.customview.widget.ViewDragHelper;

import com.cat.sutils.R;

public class FloatingViewContainer  extends ConstraintLayout {

    public static final int AUTO_SETTLE_TYPE_NONE=0;
    public static final int AUTO_SETTLE_TYPE_AUTO=1;
    public static final int AUTO_SETTLE_TYPE_INSIDE=2;

    private static final int FLING_VELOCITY_SLOP=800;

    private ViewDragHelper viewDragHelper;
    private int floatingViewId;
    private View floatingView;

    private boolean enableDragOut;
    private int autoSettleType;




    public FloatingViewContainer(Context context) {
        super(context);
        initAttrs(context,null);
    }

    public FloatingViewContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context,attrs);
    }

    public FloatingViewContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context,attrs);
    }

    public void setOnFloatingViewClickListener(OnClickListener onFloatingViewClickListener) {
        if(floatingView!=null){
            floatingView.setOnClickListener(onFloatingViewClickListener);
        }
    }

    private void initAttrs(Context context, AttributeSet attributeSet){
        TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.FloatingViewContainer);
        floatingViewId=a.getResourceId(R.styleable.FloatingViewContainer_floating_view_id,View.NO_ID);
        enableDragOut=a.getBoolean(R.styleable.FloatingViewContainer_enable_drag_out,true);
        autoSettleType=a.getInt(R.styleable.FloatingViewContainer_auto_settle_type,AUTO_SETTLE_TYPE_INSIDE);
        a.recycle();;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        floatingView=findViewById(floatingViewId);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            floatingView.setTranslationZ(999);  //保证在最上层
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        viewDragHelper=ViewDragHelper.create(this,callback);
    }


    private ViewDragHelper.Callback callback=new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(@NonNull View child, int pointerId) {
            return child==floatingView;
        }

        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            if(!enableDragOut){
                top=Math.max(0,Math.min(getHeight()-child.getHeight(),top));
            }
            return top;
        }

        @Override
        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
            if(!enableDragOut){
                left=Math.max(0,Math.min(getWidth()-child.getWidth(),left));
            }
            return left;
        }

        @Override
        public int getViewVerticalDragRange(@NonNull View child) {
            return 1;
        }

        @Override
        public int getViewHorizontalDragRange(@NonNull View child) {
            return 1;
        }

        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            if(autoSettleType!=AUTO_SETTLE_TYPE_NONE &&enableDragOut){
                settleReleasedView(releasedChild,xvel,yvel);
            }
        }
    };


    private void settleReleasedView( View releasedChild, float xvel, float yvel){

        int left=releasedChild.getLeft();
        int top=releasedChild.getTop();

        if(autoSettleType==AUTO_SETTLE_TYPE_INSIDE){
            if(left<0){
                left=0;
            }else if(left>getWidth()-releasedChild.getWidth()){
                left=getWidth()-releasedChild.getWidth();
            }

            if(top<0){
                top=0;
            }else if(top>getHeight()-releasedChild.getHeight()){
                top=getHeight()-releasedChild.getHeight();
            }
        }else{

        }


        if(viewDragHelper.settleCapturedViewAt(left,top)){
            invalidate();
        }
    }




    @Override
    public void computeScroll() {
        super.computeScroll();
        if(viewDragHelper.continueSettling(true)){
            invalidate();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }
}

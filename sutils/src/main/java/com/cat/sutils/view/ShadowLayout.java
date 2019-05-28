package com.cat.sutils.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.cat.sutils.R;

public class ShadowLayout extends FrameLayout {

    public static final int SHADOW_SIDE_LEFT=1;
    public static final int SHADOW_SIDE_TOP=2;
    public static final int SHADOW_SIDE_RIGHT=4;
    public static final int SHADOW_SIDE_BOTTOM=8;
    public static final int SHADOW_SIDE_ALL=15;


    private float mShadowRadius;
    private int mShadowColor;
    private float mShadowDx;
    private float mShadowDy;
    private float mCornerRadius;
    private int mShadowSide;

    private RectF mContentRectF;

    public ShadowLayout( Context context) {
        super(context);
        obtainAttrs(context,null);
        init();
    }

    public ShadowLayout( Context context,  AttributeSet attrs) {
        super(context, attrs);
        obtainAttrs(context,attrs);
        init();
    }

    public ShadowLayout( Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainAttrs(context,attrs);
        init();
    }



    private void obtainAttrs(Context context, AttributeSet attrs){
        if(attrs!=null){
            TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.ShadowLayout);
            mShadowRadius=a.getDimension(R.styleable.ShadowLayout_sl_shadow_radius,0);
            mShadowColor=a.getColor(R.styleable.ShadowLayout_sl_shadow_color, Color.TRANSPARENT);
            mShadowDx=a.getDimension(R.styleable.ShadowLayout_sl_shadow_dx,0);
            mShadowDy=a.getDimension(R.styleable.ShadowLayout_sl_shadow_dy,0);
            mCornerRadius=a.getDimension(R.styleable.ShadowLayout_sl_corner_radius,0);
            mShadowSide=a.getInt(R.styleable.ShadowLayout_sl_shadow_side,SHADOW_SIDE_ALL);
            a.recycle();
        }
    }

    private void init(){
        int xPadding= (int) (mShadowRadius+Math.abs(mShadowDx));
        int yPadding= (int) (mShadowRadius+Math.abs(mShadowDy));
        setPadding(containsFlag(SHADOW_SIDE_LEFT)?xPadding:0,
                containsFlag(SHADOW_SIDE_TOP)?yPadding:0,
                containsFlag(SHADOW_SIDE_RIGHT)?xPadding:0,
                containsFlag(SHADOW_SIDE_BOTTOM)?yPadding:0
                );
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    private boolean containsFlag(int sideFlag){
        return (mShadowSide|sideFlag)==mShadowSide;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mContentRectF=new RectF(getPaddingLeft(),getPaddingTop(),w-getPaddingRight(),h-getPaddingBottom());

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        drawShadow(canvas);
        drawChildAndCorners(canvas);
    }


    private void drawShadow(Canvas canvas){
        canvas.save();
        Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setShadowLayer(mShadowRadius,mShadowDx,mShadowDy,mShadowColor);
        canvas.drawRoundRect(mContentRectF,mCornerRadius,mCornerRadius,paint);
        canvas.restore();
    }

    private void drawChildAndCorners(Canvas canvas){
        Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        canvas.saveLayer(0f, 0f, canvas.getWidth(), canvas.getHeight(), paint, Canvas.ALL_SAVE_FLAG);
        super.dispatchDraw(canvas);

        Path path=new Path();
        path.addRect(mContentRectF, Path.Direction.CW);
        path.addRoundRect(mContentRectF, mCornerRadius,mCornerRadius,Path.Direction.CW);
        path.setFillType(Path.FillType.EVEN_ODD);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        paint.setColor(mShadowColor);
        canvas.drawPath(path,paint);
    }
}

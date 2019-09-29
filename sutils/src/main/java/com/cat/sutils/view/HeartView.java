package com.cat.sutils.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.cat.sutils.BezierCurveUtils;
import com.cat.sutils.R;

import java.util.ArrayList;
import java.util.List;

public class HeartView extends View {

    private int solidColor;
    private int shadowColor;
    private float shadowRadius;

    public HeartView(Context context) {
        super(context);
        initAttrs(context,null);
    }

    public HeartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context,attrs);
    }

    public HeartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context,attrs);
    }

    private void initAttrs(Context context,AttributeSet attributeSet){
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.HeartView);
        solidColor=typedArray.getColor(R.styleable.HeartView_solid_color,Color.RED);
        shadowColor=typedArray.getColor(R.styleable.HeartView_shadow_color,Color.RED);
        shadowRadius=typedArray.getDimension(R.styleable.HeartView_shadow_radius,0);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize=MeasureSpec.getSize(widthMeasureSpec);
        int heightSize=MeasureSpec.getSize(heightMeasureSpec);
        int width= (int) (Math.max(widthSize,heightSize)+shadowRadius);
        setMeasuredDimension(width,width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(getWidth() / 2, getWidth() / 2);
        Path heartPath=generateHeartPath(BezierCurveUtils.getBezierHearPoints(getContext(), (int) (getWidth()-2*shadowRadius)));
        Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(solidColor);
        paint.setShadowLayer(shadowRadius,0,0,shadowColor);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        canvas.drawPath(heartPath, paint);
    }


    private Path generateHeartPath(List<PointF> heartControlPoints ){
        Path path=new Path();
        for (int i = 0; i < 4; i++) {
            if (i == 0) {
                path.moveTo(heartControlPoints.get(i * 3).x, heartControlPoints.get(i * 3).y);
            } else {
                path.lineTo(heartControlPoints.get(i * 3).x, heartControlPoints.get(i * 3).y);
            }
            int endPointIndex;
            if (i == 3) {
                endPointIndex = 0;
            } else {
                endPointIndex = i * 3 + 3;
            }
            path.cubicTo(heartControlPoints.get(i * 3 + 1).x, heartControlPoints.get(i * 3 + 1).y,
                    heartControlPoints.get(i * 3 + 2).x, heartControlPoints.get(i * 3 + 2).y,
                    heartControlPoints.get(endPointIndex).x, heartControlPoints.get(endPointIndex).y);
        }
        return path;
    }

}

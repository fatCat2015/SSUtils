package com.cat.sutils.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class HeartView extends View {

    public HeartView(Context context) {
        super(context);
    }

    public HeartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(getWidth() / 2, getHeight() / 2);
        Path heartPath=generateHeartPath(calculateHeartControlPoints(getWidth()));
        Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        paint.setShadowLayer(30,0,0,Color.BLUE);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        canvas.drawPath(heartPath, paint);
    }

    private List<PointF> calculateHeartControlPoints(int viewWidth){
        List<PointF> heartPointList = new ArrayList<>();
        heartPointList.add(new PointF(0, dpToPx(-38)));
        heartPointList.add(new PointF(dpToPx(50), dpToPx(-103)));
        heartPointList.add(new PointF(dpToPx(112), dpToPx(-61)));
        heartPointList.add(new PointF(dpToPx(112), dpToPx(-12)));
        heartPointList.add(new PointF(dpToPx(112), dpToPx(37)));
        heartPointList.add(new PointF(dpToPx(51), dpToPx(90)));
        heartPointList.add(new PointF(0, dpToPx(129)));
        heartPointList.add(new PointF(dpToPx(-51), dpToPx(90)));
        heartPointList.add(new PointF(dpToPx(-112), dpToPx(37)));
        heartPointList.add(new PointF(dpToPx(-112), dpToPx(-12)));
        heartPointList.add(new PointF(dpToPx(-112), dpToPx(-61)));
        heartPointList.add(new PointF(dpToPx(-50), dpToPx(-103)));

        for (PointF pointF:heartPointList
             ) {
            pointF.x=pointF.x*viewWidth*1.0F/dpToPx(258);
            pointF.y=pointF.y*viewWidth*1.0F/dpToPx(258);
        }

        return heartPointList;
    }

    private Path generateHeartPath( List<PointF> heartControlPoints ){
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


    private int dpToPx(int value){
        return (int) ((int) (getContext().getResources().getDisplayMetrics().density*value)+0.5F);
    }
}

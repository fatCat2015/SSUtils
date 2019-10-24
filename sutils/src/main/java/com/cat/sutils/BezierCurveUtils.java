package com.cat.sutils;

import android.content.Context;
import android.graphics.PointF;
import androidx.annotation.IntRange;

import java.util.ArrayList;
import java.util.List;

public class BezierCurveUtils {

    // x轴坐标
    public static final int X_TYPE = 1;
    // y轴坐标
    public static final int Y_TYPE = 2;

    /**
     * 构建贝塞尔曲线，具体点数由 参数frame 决定
     *
     * @param controlPointList 控制点的坐标
     * @param frame            帧数
     * @return
     */
    public static List<PointF> buildBezierPoint(List<PointF> controlPointList,
                                                int frame) {
        List<PointF> pointList = new ArrayList<>();

        // 此处注意，要用1f，否则得出结果为0
        float delta = 1f / frame;

        // 阶数，阶数=绘制点数-1
        int order = controlPointList.size() - 1;

        // 循环递增
        for (float u = 0; u <= 1; u += delta) {
            pointList.add(new PointF(calculatePointCoordinate(X_TYPE, u, order, 0, controlPointList),
                    calculatePointCoordinate(Y_TYPE, u, order, 0, controlPointList)));
        }

        return pointList;

    }
    
    
    
    

    /**
     * 计算坐标 [贝塞尔曲线的核心关键]
     *
     * @param type             {@link #X_TYPE} 表示x轴的坐标， {@link #Y_TYPE} 表示y轴的坐标
     * @param u                当前的比例
     * @param k                阶数
     * @param p                当前坐标（具体为 x轴 或 y轴）
     * @param controlPointList 控制点的坐标
     * @return
     */
    public static float calculatePointCoordinate(@IntRange(from = X_TYPE, to = Y_TYPE) int type,
                                                 float u,
                                                 int k,
                                                 int p,
                                                 List<PointF> controlPointList) {

        /**
         * 公式解说：（p表示坐标点，后面的数字只是区分）
         * 场景：有一条线p1到p2，p0在中间，求p0的坐标
         *      p1◉--------○----------------◉p2
         *            u    p0
         *
         * 公式：p0 = p1+u*(p2-p1) 整理得出 p0 = (1-u)*p1+u*p2
         */

        // 一阶贝塞尔，直接返回
        if (k == 1) {

            float p1;
            float p2;

            // 根据是 x轴 还是 y轴 进行赋值
            if (type == X_TYPE) {
                p1 = controlPointList.get(p).x;
                p2 = controlPointList.get(p + 1).x;
            } else {
                p1 = controlPointList.get(p).y;
                p2 = controlPointList.get(p + 1).y;
            }

            return (1 - u) * p1 + u * p2;

        } else {

            /**
             * 这里应用了递归的思想：
             * 1阶贝塞尔曲线的端点 依赖于 2阶贝塞尔曲线
             * 2阶贝塞尔曲线的端点 依赖于 3阶贝塞尔曲线
             * ....
             * n-1阶贝塞尔曲线的端点 依赖于 n阶贝塞尔曲线
             *
             * 1阶贝塞尔曲线 则为 真正的贝塞尔曲线存在的点
             */
            return (1 - u) * calculatePointCoordinate(type, u, k - 1, p, controlPointList)
                    + u * calculatePointCoordinate(type, u, k - 1, p + 1, controlPointList);

        }

    }

    /**
     * 一张画布宽高为258dp 以画布的中心点作为坐标原点 通过尝试得到12个点的坐标
     * 被x,y轴分为四部分 每部分是4个点
     * 以这四个点作为3阶贝塞尔曲线的控制点
     * 可以画出一条贝塞尔曲线,4个贝塞尔曲线组成一个封闭的心形
     * @param context
     * @param targetWidth 实际的画布宽度
     * @return 12个点的坐标
     */
    public static List<PointF> getBezierHearPoints(Context context,int targetWidth){
        List<PointF> heartPointList = new ArrayList<>();
        heartPointList.add(new PointF(0, DisplayUtils.dp2px(context,-38)));
        heartPointList.add(new PointF(DisplayUtils.dp2px(context,50), DisplayUtils.dp2px(context,-103)));
        heartPointList.add(new PointF(DisplayUtils.dp2px(context,112), DisplayUtils.dp2px(context,-61)));
        heartPointList.add(new PointF(DisplayUtils.dp2px(context,112), DisplayUtils.dp2px(context,-12)));
        heartPointList.add(new PointF(DisplayUtils.dp2px(context,112), DisplayUtils.dp2px(context,37)));
        heartPointList.add(new PointF(DisplayUtils.dp2px(context,51), DisplayUtils.dp2px(context,90)));
        heartPointList.add(new PointF(0, DisplayUtils.dp2px(context,129)));
        heartPointList.add(new PointF(DisplayUtils.dp2px(context,-51), DisplayUtils.dp2px(context,90)));
        heartPointList.add(new PointF(DisplayUtils.dp2px(context,-112), DisplayUtils.dp2px(context,37)));
        heartPointList.add(new PointF(DisplayUtils.dp2px(context,-112), DisplayUtils.dp2px(context,-12)));
        heartPointList.add(new PointF(DisplayUtils.dp2px(context,-112), DisplayUtils.dp2px(context,-61)));
        heartPointList.add(new PointF(DisplayUtils.dp2px(context,-50), DisplayUtils.dp2px(context,-103)));

        for (PointF pointF:heartPointList
        ) {
            pointF.x=pointF.x*targetWidth*1.0F/DisplayUtils.dp2px(context,258);
            pointF.y=pointF.y*targetWidth*1.0F/DisplayUtils.dp2px(context,258);
        }
        return heartPointList;
    }
    
}

package com.cat.sutils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import androidx.core.widget.NestedScrollView;

import android.view.View;
import android.widget.ScrollView;

public class ViewUtils {


    /**
     * 不可见的view生成bitmap
     * @param view
     * @param width
     * @return
     */
    public static Bitmap view2Bitmap(View view,int width){
        view.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),0);
        view.layout(0,0,view.getMeasuredWidth(),view.getMeasuredHeight());
        return view2Bitmap(view);
    }


    /**
     * 可见view生成bitmap
     * @param view
     * @return
     */
    public static Bitmap view2Bitmap(View view) {
            if (view instanceof ScrollView) {
                view=((ScrollView) view).getChildAt(0);
            } else if (view instanceof NestedScrollView) {
                view=((NestedScrollView) view).getChildAt(0);
            }
            Bitmap bmp = Bitmap.createBitmap(view.getWidth(),view.getHeight(),Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bmp);
            canvas.drawColor(Color.TRANSPARENT);
            view.draw(canvas);
            return bmp;
    }
}

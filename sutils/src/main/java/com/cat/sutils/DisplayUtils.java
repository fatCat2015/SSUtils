package com.cat.sutils;

import android.content.Context;

public class DisplayUtils {

    public static int dp2px(Context context,float dp){
        return (int) (context.getResources().getDisplayMetrics().density*dp+0.5F);

    }
}

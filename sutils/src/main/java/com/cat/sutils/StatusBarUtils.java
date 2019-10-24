package com.cat.sutils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

public class StatusBarUtils {


    /**
     * 效果: 内容布局向上顶 占用了状态栏
     * @param activity
     */
    public static void transparentStatusBar(Activity activity){
        transparentStatusBar(activity,0);
    }

    /**
     * 效果: 内容布局向上顶 占用了状态栏
     * @param activity
     * @param statusBarViewId   设置该view的高度为状态栏的高度 通常用于布局的第一个view (适用于DrawerLayout的内容布局部分,可以使内容和抽屉部分的状态栏颜色不一致)
     */
    public static void transparentStatusBar(Activity activity,int statusBarViewId){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            activity.getWindow().getDecorView().setSystemUiVisibility(option);
            activity.getWindow().setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
        } else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        if(statusBarViewId!=0){
            setViewHeightEqualsStatusBarHeight(activity,statusBarViewId);
        }

    }

    private static void setViewHeightEqualsStatusBarHeight(Activity activity, int statusBarViewId ) {
        View view = activity.findViewById(statusBarViewId);
        if (view != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                view.setVisibility(View.VISIBLE);
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                layoutParams.height = getStatusBarHeight(activity);
                view.setLayoutParams(layoutParams);
            } else {
                view.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 利用反射获取状态栏高度
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    /**
     * 改变状态栏的颜色
     * @param activity
     * @param color
     */
    public static void setStatusBarColor(Activity activity,int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0 以上直接设置状态栏颜色
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().setStatusBarColor(color);
        } else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT) {  //4.4-5.0
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);       //隐藏手机自带状态栏
            ViewGroup rootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
            rootView.setPadding(0, getStatusBarHeight(activity), 0, 0);   //设置paddingTop值为状态栏高度
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            View statusBarView = new View(activity);   //创建一个view 添加到布局里面 这个view的高度为手机状态栏高度 背景颜色为给定颜色
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,getStatusBarHeight(activity));
            statusBarView.setBackgroundColor(color);
            decorView.addView(statusBarView, lp);
        }
    }


    /**
     * 设置手机状态栏问题字体样式 白或者黑  仅支持6.0以上
     * @param activity
     * @param darkMode
     */
    public static void statusBarDarkOrLightMode(Activity activity,boolean darkMode){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            int current=activity.getWindow().getDecorView().getSystemUiVisibility();
            if(darkMode){
                activity.getWindow().getDecorView().setSystemUiVisibility(current&~View.SYSTEM_UI_FLAG_VISIBLE|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }else{
                activity.getWindow().getDecorView().setSystemUiVisibility(current&~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR|View.SYSTEM_UI_FLAG_VISIBLE);
            }
        }

    }


}



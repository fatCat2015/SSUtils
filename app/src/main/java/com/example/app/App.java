package com.example.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.cat.aop.login.ILoginCheck;
import com.cat.aop.login.LoginCheckProxy;

public class App extends Application {


    public static boolean isLoggedIn=false;

    @Override
    public void onCreate() {
        super.onCreate();

        LoginCheckProxy.getInstance().initLoginCheck(new ILoginCheck() {
            @Override
            public boolean isLoggedIn(Context context) {
                return isLoggedIn;
            }

            @Override
            public void onNotLoggedIn(Activity activity, int requestCode) {
                Log.i("sck220", "onNotLoggedIn: "+activity+" 跳转登录");
                activity.startActivityForResult(new Intent(activity,Main2Activity.class),requestCode);
            }

            @Override
            public void onNotLoggedIn(Fragment fragment, int requestCode) {
                Log.i("sck220", "onNotLoggedIn: "+fragment+" 跳转登录");
                fragment.startActivityForResult(new Intent(fragment.getActivity(),Main2Activity.class),requestCode);
            }
        });
    }
}

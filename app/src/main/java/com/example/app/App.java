package com.example.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.cat.aop.event.EventUploadProxy;
import com.cat.aop.event.IEventUpload;
import com.cat.aop.login.ILoginCheck;
import com.cat.aop.login.LoginCheckProxy;

import java.util.Arrays;

public class App extends Application {


    public static boolean isLoggedIn=false;


    @Override
    public void onCreate() {
        super.onCreate();


        try {
            LoginCheckProxy.getInstance().initLoginCheck(Main2Activity.class, new ILoginCheck() {
                @Override
                public boolean isLoggedIn(Context context) {
                    return false;
                }

                @Override
                public void onStartLoginActivity(Activity activity) {

                }
            });

            EventUploadProxy.getInstance().initEventUpload(new IEventUpload() {
                @Override
                public void uploadEvent(String eventName, Object[] args) {
                    Log.i("sck220", "uploadEvent: "+eventName+" "+ Arrays.toString(args));
                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }
}

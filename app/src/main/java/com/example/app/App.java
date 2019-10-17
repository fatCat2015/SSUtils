package com.example.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.cat.aop.event.EventParam;
import com.cat.aop.event.EventUploadProxy;
import com.cat.aop.event.IEventUpload;
import com.cat.aop.login.ILoginCheck;
import com.cat.aop.login.LoginCheckProxy;

import java.lang.reflect.Method;
import java.util.Arrays;

public class App extends Application {


    public static boolean isLoggedIn=false;


    @Override
    public void onCreate() {
        super.onCreate();



        EventUploadProxy.getInstance().initEventUpload(new IEventUpload() {
            @Override
            public void uploadEvent(String eventName, String eventJsonParams) {
                Log.i("sck220", "uploadEvent: "+eventName+" "+ eventJsonParams);
            }
        });

    }
}

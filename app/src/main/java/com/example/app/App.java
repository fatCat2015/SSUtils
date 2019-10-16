package com.example.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.cat.aop.login.ILoginCheck;
import com.cat.aop.login.LoginCheckProxy;

public class App extends Application {


    public static boolean isLoggedIn=false;


    @Override
    public void onCreate() {
        super.onCreate();

        LoginCheckProxy.getInstance().initLoginCheck(Main2Activity.class, context -> isLoggedIn);
    }
}

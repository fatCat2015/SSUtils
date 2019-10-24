package com.example.app;

import android.app.Application;
import android.util.Log;

import com.cat.aop.event.EventUploadProxy;

public class App extends Application {


    @Override
    public void onCreate() {
        super.onCreate();


        EventUploadProxy.getInstance().initEventUpload((eventName, eventJsonParams) -> Log.i("sck220", "uploadEvent: "+eventName+" "+ eventJsonParams));

    }
}

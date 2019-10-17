package com.example.app;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cat.aop.annotation.Async;
import com.cat.aop.annotation.Event;
import com.cat.aop.annotation.Permission;
import com.cat.aop.annotation.Trace;
import com.cat.aop.event.EventParam;
import com.cat.aop.login.ILoginCheck;
import com.cat.aop.login.LoginCheckProxy;
import com.cat.sutils.ViewUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity  {



    int a=10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        a("8955",1,3.14);

    }


    @Event( "test_upload")
    private void a(@EventParam("houseId") String a,  @EventParam("houseType") int b ,@EventParam("price") double price ){
        HandlerThread handlerThread=new HandlerThread("test");
        handlerThread.start();
        Handler handler=new Handler(handlerThread.getLooper());


        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.i("sck220", "run: "+Thread.currentThread().getId()+" "+Thread.currentThread().getName()+" "+ Looper.getMainLooper());

            }
        });
    }





}

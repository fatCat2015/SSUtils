package com.example.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.cat.aop.annotation.Event;
import com.cat.aop.annotation.RunOnMainThread;
import com.cat.aop.event.EventParam;

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
        new Thread(new Runnable() {
            @Override
            public void run() {
                b();
            }
        }).start();
    }


    @RunOnMainThread
    private void b(){
        Log.i("sck220", "b: "+Thread.currentThread().getId());
    }




}

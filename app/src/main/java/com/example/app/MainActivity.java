package com.example.app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Toast;

import com.sat.service.ServiceManager;

public class MainActivity extends AppCompatActivity  {


    private View bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ServiceManager.getInstance().init(this,"http://www.baidu.com",BuildConfig.DEBUG);

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.exit(0);
            }
        });



    }

    public void a(View view){
        ServiceManager.getInstance().test();
        Toast.makeText(this, "aa", Toast.LENGTH_SHORT).show();
    }






}

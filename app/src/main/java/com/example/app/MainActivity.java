package com.example.app;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.cat.aop.annotation.AvoidMultipleExecutions;
import com.cat.aop.annotation.NeedLog;
import com.cat.aop.annotation.Permission;
import com.cat.aop.permission.OnPermissionDenied;
import com.cat.aop.permission.OnPermissionDeniedWithNeverAskAgain;
import com.cat.aop.permission.OnPermissionGranted;
import com.cat.aop.permission.OnPermissionSettings;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);



        findViewById(R.id.bt1).setOnClickListener(this);
        findViewById(R.id.bt).setOnClickListener(this);



    }


    @NeedLog
    @Override
    public void onClick(View v) {
        test();
    }





    @Permission(value = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA},dispatchCheckResult = true)
    private void test(){
        Log.i("sck220", "test: ");
    }


    @OnPermissionGranted
    private void a(){
        Log.i("sck220", "a: ");
    }

    @OnPermissionDenied
    private void b(){
        Log.i("sck220", "b: ");
    }

    @OnPermissionDeniedWithNeverAskAgain
    private void c(){
        Log.i("sck220", "c: ");
    }

    @OnPermissionSettings
    private void d(){
        Log.i("sck220", "d: ");
    }



}

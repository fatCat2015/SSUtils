package com.example.app;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.cat.aop.annotation.CheckLogin;
import com.cat.aop.annotation.NeedLog;
import com.cat.aop.annotation.Permission;
import com.cat.aop.login.ILoginCheck;
import com.cat.aop.login.LoginCheckProxy;
import com.cat.aop.permission.annotation.OnPermissionDenied;
import com.cat.aop.permission.annotation.OnPermissionDeniedWithNeverAskAgain;
import com.cat.aop.permission.annotation.OnPermissionGranted;
import com.cat.aop.permission.annotation.OnPermissionSettings;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        findViewById(R.id.bt1).setOnClickListener(this);
        findViewById(R.id.bt).setOnClickListener(this);



    }



    @Override
    public void onClick(View v) {
        test();
    }

    @CheckLogin()
    private void test(){
        Log.i("sck220", "test: ");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==200&&resultCode==RESULT_OK){
            test();
        }
    }
}

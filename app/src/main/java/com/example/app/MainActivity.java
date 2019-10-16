package com.example.app;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.cat.aop.annotation.Async;
import com.cat.aop.annotation.Permission;
import com.cat.aop.annotation.Trace;
import com.cat.aop.login.ILoginCheck;
import com.cat.aop.login.LoginCheckProxy;
import com.cat.sutils.ViewUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }



    @Permission({Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void c(View view){
        a(view);
    }

    @Trace
    private void a(View view){
        Bitmap bitmap=ViewUtils.view2Bitmap(findViewById(R.id.sv));

        String path=new File(Environment.getExternalStorageDirectory(),"demo.png").getAbsolutePath();
        try {
            bitmap.compress(Bitmap.CompressFormat.PNG,100,new FileOutputStream(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this,Main2Activity.class).putExtra("image",byteArrayOutputStream.toByteArray()));
            }
        });
    }



}

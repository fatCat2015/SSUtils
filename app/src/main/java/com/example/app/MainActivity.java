package com.example.app;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.cat.aop.annotation.CheckLogin;
import com.cat.aop.annotation.NeedLog;
import com.cat.aop.annotation.Permission;
import com.cat.aop.login.ILoginCheck;
import com.cat.aop.login.LoginCheckProxy;
import com.cat.aop.permission.annotation.OnPermissionDenied;
import com.cat.aop.permission.annotation.OnPermissionDeniedWithNeverAskAgain;
import com.cat.aop.permission.annotation.OnPermissionGranted;
import com.cat.aop.permission.annotation.OnPermissionSettings;
import com.cat.sutils.ViewUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class MainActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void c(View view){
        Toast.makeText(this,BuildConfig.DEBUG+" app",Toast.LENGTH_SHORT).show();
        long a=System.currentTimeMillis();
        Bitmap bitmap=ViewUtils.view2Bitmap(findViewById(R.id.sv));
        Log.i("sck220", "onCreate: "+bitmap.getByteCount()/1024F+" "+(System.currentTimeMillis()-a));

        a=System.currentTimeMillis();
        String path=new File(Environment.getExternalStorageDirectory(),"demo.png").getAbsolutePath();
        try {
            bitmap.compress(Bitmap.CompressFormat.PNG,100,new FileOutputStream(path));
            Log.i("sck220", "onCreate: "+bitmap.getByteCount()/1024F+" "+(System.currentTimeMillis()-a));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);

        Log.i("sck220", "c: "+(byteArrayOutputStream.toByteArray().length/1024F));

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this,Main2Activity.class).putExtra("image",byteArrayOutputStream.toByteArray()));
            }
        });




    }


}

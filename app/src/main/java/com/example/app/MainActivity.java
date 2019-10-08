package com.example.app;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Consumer;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cat.aop.annotation.Permission;
import com.cat.aop.permission.OnPermissionDenied;
import com.cat.aop.permission.OnPermissionDeniedWithAskNeverAgain;
import com.cat.aop.permission.OnPermissionGranted;
import com.cat.aop.permission.OnPermissionSettings;
import com.cat.sutils.ColorTransition;
import com.cat.sutils.PaletteHelper;
import com.cat.sutils.StatusBarUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity {





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);




    }

    public void aa(View view){
        test();
    }


    @Permission({Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA})
    private void test(){
        Log.i("sck220", "test: ");
    }



    @OnPermissionDenied
    private void b(){
        Log.i("sck220", "b: ");
    }

    @OnPermissionDeniedWithAskNeverAgain
    private void c(){
        Log.i("sck220", "c: ");
    }

    @OnPermissionSettings
    private void d(){
        Log.i("sck220", "d: ");
    }



}

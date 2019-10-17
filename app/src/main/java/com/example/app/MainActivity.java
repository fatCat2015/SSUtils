package com.example.app;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cat.aop.annotation.Async;
import com.cat.aop.annotation.Event;
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




    }





}

package com.example.app;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.ArrayRes;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Toast;

import com.cat.aop.annotation.CheckLogin;
import com.cat.aop.annotation.Event;
import com.cat.aop.annotation.Permission;
import com.cat.aop.annotation.Trace;
import com.cat.aop.event.EventParam;
import com.cat.aop.event.EventUploadProxy;
import com.cat.aop.event.IEventUpload;
import com.cat.aop.login.ILoginCheck;
import com.cat.aop.login.LoginCheckProxy;
import com.cat.sutils.view.FloatingViewContainer;

public class MainActivity extends AppCompatActivity  {





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FloatingViewContainer floatingViewContainer=findViewById(R.id.fvc);
        floatingViewContainer.setOnFloatingViewClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "1111", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void a(View view){
        Toast.makeText(this, "a", Toast.LENGTH_SHORT).show();

    }

    public void b(View view){
        Toast.makeText(this, "b", Toast.LENGTH_SHORT).show();
    }





}

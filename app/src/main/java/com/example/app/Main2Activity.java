package com.example.app;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.cat.sutils.wheel.WheelView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main2Activity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        ImageView imageView=findViewById(R.id.imageView);

        byte[] bs = getIntent().getByteArrayExtra("image");
        imageView.setImageBitmap(BitmapFactory.decodeByteArray(bs,0,bs.length));

    }






}

package com.cat.sutils;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    int index=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatusBarUtils.transparentStatusBar(this);
    }

    public void aa(View view) {
        switch (index){
            case 0:
                StatusBarUtils.transparentStatusBar(this);
                break;
            case 1:
                StatusBarUtils.setStatusBarColor(this,ContextCompat.getColor(this,R.color.colorPrimary));
                break;
            case 2:
                StatusBarUtils.setStatusBarColor(this,Color.parseColor("#ff0000"));
                break;
            case 3:
                StatusBarUtils.setStatusBarColor(this,Color.parseColor("#00ff00"));
                break;
        }
        index++;
        if (index>=4) {
            index=0;

        }
    }
}

package com.example.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.cat.sutils.wheel.WheelView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main2Activity extends AppCompatActivity {


    private WheelView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        this.rv =  findViewById(R.id.rv);

        rv.setData(mockData("哈哈哈哈",15),new Random().nextInt(15));

        rv.setOnSelectedChangListener((wheelView, position) -> {
            Log.i("sck220", "setOnSelectedChangListener: "+wheelView.getSelectedItem());
        });

    }


    private List<String> mockData(String prefix,int count){
        List<String> data=new ArrayList<>();
        for (int i = 0; i < count; i++) {
            data.add(String.format("%s%s",prefix,i));
        }
        return data;
    }

    public void a(View view) {
        App.isLoggedIn=true;
        setResult(RESULT_OK);
        finish();
    }





}

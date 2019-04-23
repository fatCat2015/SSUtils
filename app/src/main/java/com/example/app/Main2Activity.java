package com.example.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.cat.sutils.wheel.WheelDataItem;
import com.cat.sutils.wheel.WheelView;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {


    private com.cat.sutils.wheel.WheelView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        this.rv = (WheelView) findViewById(R.id.rv);

        rv.setData(mockData("哈哈哈哈",15));

    }


    private List<Item> mockData(String prefix,int count){
        List<Item> data=new ArrayList<>();
        for (int i = 0; i < count; i++) {
            data.add(new Item(String.format("%s%s",prefix,i)));
        }
        return data;
    }

    public void a(View view) {
        rv.setData(mockData("呵呵呵呵呵呵",8));
    }


    private class Item extends WheelDataItem{

        public Item(String name) {
            super(name);
        }
    }



}

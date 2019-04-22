package com.example.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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

        rv.setData(mockData());

    }


    private List<Item> mockData(){
        List<Item> data=new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            data.add(new Item(String.format("哈哈哈哈哈哈%s",i)));
        }
        return data;
    }




    private class Item extends WheelDataItem{

        public Item(String name) {
            super(name);
        }
    }



}

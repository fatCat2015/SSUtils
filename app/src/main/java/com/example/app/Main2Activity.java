package com.example.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.cat.sutils.wheel.OnSelectedChangListener;
import com.cat.sutils.wheel.WheelDataItem;
import com.cat.sutils.wheel.WheelView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main2Activity extends AppCompatActivity {


    private com.cat.sutils.wheel.WheelView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        this.rv = (WheelView) findViewById(R.id.rv);

        rv.setData(mockData("哈哈哈哈",15),new Random().nextInt(15));

        rv.setOnSelectedChangListener((wheelView, position) -> {
            Log.i("sck220", "setOnSelectedChangListener: "+wheelView.getSelectedItem().getName());
        });

    }


    private List<Item> mockData(String prefix,int count){
        List<Item> data=new ArrayList<>();
        for (int i = 0; i < count; i++) {
            data.add(new Item(String.format("%s%s",prefix,i)));
        }
        return data;
    }

    public void a(View view) {
        Item item = rv.getSelectedItem();
        Log.i("sck220", "a: "+item.getName());
    }


    private class Item extends WheelDataItem{

        public Item(String name) {
            super(name);
        }
    }



}

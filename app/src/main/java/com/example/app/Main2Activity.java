package com.example.app;

import android.graphics.Color;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxItemDecoration;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main2Activity extends AppCompatActivity {


    private List<Integer> data = new ArrayList<>();
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        this.rv = (RecyclerView) findViewById(R.id.rv);

        for (int i = 0; i < 50; i++) {
            data.add(i);
        }



        rv.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        rv.setAdapter(new A());

    }


    private class A extends RecyclerView.Adapter<V> {


        @NonNull
        @Override
        public V onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new V(LayoutInflater.from(Main2Activity.this).inflate(R.layout.item, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull V v, int i) {
            v.textView.setText(String.valueOf(data.get(i)));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }


    private class V extends RecyclerView.ViewHolder {

        private TextView textView;


        public V(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv);

            ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
            layoutParams.height = (int) (Math.random() * 180 + 60);
            layoutParams.width=540;
            textView.setLayoutParams(layoutParams);
            textView.setBackgroundColor(randomColor());
        }


    }

    private int randomColor(){
        Random random=new Random();
        return Color.rgb(random.nextInt(256),random.nextInt(256),random.nextInt(256));
    }


}

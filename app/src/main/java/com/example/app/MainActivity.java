package com.example.app;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.SpannableString;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.cat.sutils.span.ClickableSpan;
import com.sat.service.ServiceManager;

public class MainActivity extends AppCompatActivity  {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        String a="点击";
        String b="哈哈哈哈 点击 哈哈哈";

        TextView textView=findViewById(R.id.tv);

        SpannableString spannableString=new SpannableString(b);
        int start=b.indexOf(a);
        int end=start+a.length();

        spannableString.setSpan(new ClickableSpan(Color.GREEN, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "AAAAAAAAAAAAAAA", Toast.LENGTH_SHORT).show();
            }
        }).attachToTextView(textView),start,end,SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);



        textView.setText(spannableString);


    }

    public void a(View view){
    }






}

package com.example.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var drawerLayout=findViewById<DrawerLayout>(R.id.drawerLayout)
        drawerLayout.onDrawOpen={
            Log.i("SlideBackLayout", "onDrawOpen: ")
        }
        drawerLayout.onDrawClose={
            Log.i("SlideBackLayout", "onDrawClose: ")
        }
        drawerLayout.onDrawSlide={
            Log.i("SlideBackLayout", "onDrawSlide:$it ")
        }


    }
}
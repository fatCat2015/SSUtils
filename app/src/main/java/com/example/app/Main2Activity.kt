package com.example.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class Main2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        var a=findViewById<DrawerLayout>(R.id.sb)
    }
}
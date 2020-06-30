package com.example.app

import android.os.Bundle
import android.view.Gravity
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.sck.guide.GuideManager
import com.sck.guide.GuideParams

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tv =
            findViewById<TextView>(R.id.tv)
        val tv1 =
            findViewById<TextView>(R.id.tv1)




        GuideManager.init(this, true)
        tv.setOnClickListener {
            GuideManager.begin()
                .add(GuideParams(tv, R.layout.guide_demo_bottom,"a"))
                .add(GuideParams(tv1, R.layout.guide_demo_bottom, "b",gravity = Gravity.LEFT))
                .show(this)
        }
    }

}
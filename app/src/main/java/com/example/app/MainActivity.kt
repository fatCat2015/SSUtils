package com.example.app

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var viewPager=findViewById<ViewPager>(R.id.viewPager)
        viewPager.adapter=ViewPageAdapter()

        var rv=findViewById<RecyclerView>(R.id.rv)
        rv.adapter=Adapter()
    }




}

private class Adapter:RecyclerView.Adapter<ItemHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        var tv=TextView(parent.context)
        tv.textSize=20F
        tv.setPadding(60,30,60,30)
        return ItemHolder(tv)
    }

    override fun getItemCount(): Int {
        return 20
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.tv.text="${position}个item"
    }

}

private class ItemHolder(view:View):RecyclerView.ViewHolder(view){
    val tv=itemView as TextView
}

private class ViewPageAdapter:PagerAdapter(){


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var tv=TextView(container.context)
        tv.textSize=20F
        tv.text="${position}个item"
        container.addView(tv)
        return tv
    }

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
        container.removeView(view as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view==`object`
    }

    override fun getCount(): Int {
        return 5
    }

}

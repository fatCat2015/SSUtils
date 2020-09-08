package com.example.app.adapter

import android.view.View
import android.view.ViewGroup
import androidx.collection.SparseArrayCompat

open class WrappedAdapter<T>(data:List<T>?): BaseAdapter<T>(data){


    private var headerItemViewType=100

    private var footerItemViewType=200

    private val headerViews= SparseArrayCompat<View>()

    private val footerViews= SparseArrayCompat<View>()

    fun addHeader(view:View,notify:Boolean=false){
        if(!headerViews.containsValue(view)){
            headerViews.putIfAbsent(headerItemViewType++,view)
            if(notify){
                notifyItemInserted(getHeaderCount()-1)
            }
        }
    }

    fun removeHeader(view:View,notify:Boolean=false){
        val index=headerViews.indexOfValue(view)
        if(index>=0){
            headerViews.removeAt(index)
            headerItemViewType--
            if(notify){
                notifyItemRemoved(index)
            }
        }
    }

    fun addFooter(view:View,notify:Boolean=false){
        if(!footerViews.containsValue(view)){
            footerViews.putIfAbsent(footerItemViewType++,view)
            if(notify){
                notifyItemInserted(itemCount-1)
            }
        }
    }

    fun removeFooter(view:View,notify:Boolean=false){
        val index=footerViews.indexOfValue(view)
        if(index>=0){
            footerViews.removeAt(index)
            footerItemViewType--
            if(notify){
                notifyItemRemoved(getHeaderCount()+super.getItemCount()+index)
            }
        }
    }


    fun getHeaderCount()=headerViews.size()

    fun getFooterCount()=footerViews.size()

    override fun getItemCount(): Int {
        return getHeaderCount()+super.getItemCount()+getFooterCount()
    }

    override fun getItemViewType(position: Int): Int {
        val a=when{
            position<getHeaderCount() ->headerViews.keyAt(position)
            position>=getHeaderCount()+super.getItemCount() -> footerViews.keyAt(position -getHeaderCount()-super.getItemCount())
            else -> super.getItemViewType(position)
        }
        return a
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when {
            viewType<100 -> {
                super.onCreateViewHolder(parent, viewType)
            }
            viewType<200 -> {
                BaseViewHolder(DefaultItemView(headerViews.valueAt(viewType-100)))
            }
            else -> {
                BaseViewHolder(DefaultItemView(footerViews.valueAt(viewType-200)))
            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int, payloads: MutableList<Any>) {
        val viewType=getItemViewType(position)
        if(viewType<100){
            super.onBindViewHolder(holder, position ,payloads)
        }
    }

    override fun getItem(position: Int): T {
        return super.getItem(position-getHeaderCount())
    }
}
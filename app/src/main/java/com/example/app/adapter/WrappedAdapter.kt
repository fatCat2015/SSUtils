package com.example.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class WrappedAdapter<T>(data:List<T>?): BaseAdapter<T>(data){


    private var headerItemViewType=100

    private var footerItemViewType=200

    private val headerViews= SparseArrayCompat<ExtraView<*>>()

    private val footerViews= SparseArrayCompat<ExtraView<*>>()

    fun <B:ViewDataBinding> addHeader(view:ExtraView<B>,notify:Boolean=false){
        if(!headerViews.containsValue(view)){
            headerViews.putIfAbsent(headerItemViewType++,view)
            if(notify){
                notifyItemInserted(getHeaderCount()-1)
            }
        }
    }

    fun removeHeader(view:ExtraView<*>,notify:Boolean=false){
        val index=headerViews.indexOfValue(view)
        if(index>=0){
            headerViews.removeAt(index)
            headerItemViewType--
            if(notify){
                notifyItemRemoved(index)
            }
        }
    }

    fun <B:ViewDataBinding> addFooter(view:ExtraView<B>,notify:Boolean=false){
        if(!footerViews.containsValue(view)){
            footerViews.putIfAbsent(footerItemViewType++,view)
            if(notify){
                notifyItemInserted(itemCount-1)
            }
        }
    }

    fun removeFooter(view:ExtraView<*>,notify:Boolean=false){
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when {
            viewType<100 -> {
                super.onCreateViewHolder(parent, viewType)
            }
            viewType<200 -> {
                BaseViewHolder(DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(parent.context),getHeaderView(viewType).layoutResId(),parent,false))
            }
            else -> {
                BaseViewHolder(DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(parent.context),getFooterView(viewType).layoutResId(),parent,false))
            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int, payloads: MutableList<Any>) {
        val viewType=getItemViewType(position)
        when {
            viewType < 100 -> {
                super.onBindViewHolder(holder, position, payloads)
            }
            viewType < 200 -> {
                getHeaderView(viewType)._onBindView(holder.dataBinding)
            }
            else -> {
                getFooterView(viewType)._onBindView(holder.dataBinding)
            }
        }
    }

    override fun getItem(position: Int): T {
        return super.getItem(position-getHeaderCount())
    }

    private fun getHeaderView(viewType:Int):ExtraView<*>{
        return headerViews.valueAt(viewType-100)
    }

    private fun getFooterView(viewType:Int):ExtraView<*>{
        return footerViews.valueAt(viewType-200)
    }

}


abstract class ExtraView<B:ViewDataBinding>{
    abstract fun layoutResId():Int
    abstract fun onBindView(dataBinding:B)
    fun _onBindView(dataBinding:ViewDataBinding){
        onBindView(dataBinding as B)
    }

}



package com.example.app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class SimpleAdapter<T>(private val layoutResId:Int,data:List<T>?): WrappedAdapter<T>(data) {

    init {
        addItemDelegate(object : ItemDelegate<T> {
            override fun createItemView(layoutInflater: LayoutInflater, parent: ViewGroup): ItemView {
                return DefaultItemView(layoutInflater.inflate(layoutResId,parent,false))
            }

            override fun apply(item: T): Boolean {
                return true
            }

            override fun bindData(viewHolder: BaseViewHolder, item: T, position: Int) {
                 this@SimpleAdapter.bindData(viewHolder,item,position)
            }
        })
    }

    abstract fun bindData(viewHolder: BaseViewHolder, item: T, position: Int)


}


abstract class SimpleVDBindingAdapter<T,B:ViewDataBinding>(private val layoutResId:Int,data:List<T>?): WrappedAdapter<T>(data) {

    init {
        addItemDelegate(object : ItemDelegate<T> {
            override fun createItemView(layoutInflater: LayoutInflater, parent: ViewGroup): ItemView {
                return VDBindingItemView(DataBindingUtil.inflate<B>(layoutInflater,layoutResId,parent,false))
            }
            override fun apply(item: T): Boolean {
                return true
            }

            override fun bindData(viewHolder: BaseViewHolder, item: T, position: Int) {
                this@SimpleVDBindingAdapter.bindData((viewHolder.view as VDBindingItemView).viewDataBinding as B,item,position)
            }
        })
    }

    abstract fun bindData(dataBinding:B, item: T, position: Int)


}
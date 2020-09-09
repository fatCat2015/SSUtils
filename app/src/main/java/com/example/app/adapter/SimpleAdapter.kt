package com.example.app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class SimpleAdapter<T,B:ViewDataBinding>(private val layoutResId:Int,data:List<T>?): WrappedAdapter<T>(data) {

    override fun addItemDelegates() {



        addItemDelegate(object :ItemDelegate<T,B>(){
            override fun layoutResId(): Int {
                return layoutResId
            }

            override fun apply(item: T): Boolean {
                return true
            }

            override fun bindData(viewHolder: BaseViewHolder<B>, item: T, position: Int) {
                this@SimpleAdapter.bindData(viewHolder,item,position)
            }

            override fun bindData(viewHolder: BaseViewHolder<B>, item: T, position: Int, payloads: MutableList<Any>) {
                this@SimpleAdapter.bindData(viewHolder,item,position,payloads)
            }
        })
    }

    abstract fun bindData(viewHolder: BaseViewHolder<B>, item: T, position: Int)

    open fun bindData(viewHolder: BaseViewHolder<B>, item: T, position: Int,payloads: MutableList<Any>){

    }


}


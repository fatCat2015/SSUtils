package com.example.app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import java.lang.IllegalArgumentException

/**
 * 每一个ItemDelegate代表一种布局类型
 */
abstract class ItemDelegate<T,B:ViewDataBinding> {

    /**
     * 布局itemView resId
     */
    abstract fun layoutResId():Int

    /**
     * @return true 表示 数据item使用该布局
     */
    abstract fun apply(item:T):Boolean

    /**
     * @return true 表示 该布局可以进行点击
     */
    open fun clickAble():Boolean{
        return true
    }

    /**
     * 布局和数据进行绑定
     */
    fun _bindData(viewHolder: BaseViewHolder<*>, item: T, position:Int){
        bindData(viewHolder as  BaseViewHolder<B>,item,position)
    }

    abstract fun bindData(viewHolder: BaseViewHolder<B>, item: T, position:Int)

    /**
     * 布局和数据进行绑定 一般用于局部刷新
     */
    fun _bindData(viewHolder: BaseViewHolder<*>, item: T, position:Int, payloads: MutableList<Any>){
        bindData(viewHolder as  BaseViewHolder<B>,item,position,payloads)
    }

    open fun bindData(viewHolder: BaseViewHolder<B>, item: T, position:Int,payloads: MutableList<Any>){

    }
}


internal class ItemDelegateHolder<T>{

    /**
     * key对应的是itemViewType
     */
    private val itemDelegateMap= SparseArrayCompat<ItemDelegate<T,*>>()

    fun addItemDelegate(itemDelegate: ItemDelegate<T,*>){
        itemDelegateMap.putIfAbsent(itemDelegateMap.size(),itemDelegate)
    }

    fun getItemViewType(item:T):Int{
        val size=itemDelegateMap.size()
        for(index in 0 until size){
            val itemViewType=itemDelegateMap.keyAt(index)
            val itemDelegate=itemDelegateMap.valueAt(index)
            if(itemDelegate.apply(item)){
                return itemViewType
            }
        }
        throw IllegalStateException("each item has to specify a item type ")
    }

    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val itemDelegate=getItemDelegateByItemViewType(viewType)
        val dataBinding=DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(parent.context),itemDelegate.layoutResId(),parent,false)
        return BaseViewHolder(dataBinding)
    }

    fun onBindViewHolder(holder: BaseViewHolder<*>, item:T, position:Int, viewType:Int, onItemClickListener: OnItemClickListener<T>?){
        val itemDelegate=getItemDelegateByItemViewType(viewType)
        itemDelegate._bindData(holder,item,position)
        if(itemDelegate.clickAble()){
            holder.itemView.setOnClickListener {
                onItemClickListener?.invoke(holder,item,position)
            }
        }else{
            holder.itemView.setOnClickListener(null)
        }
    }

    fun onBindViewHolder(holder: BaseViewHolder<*>, item:T, position:Int, viewType:Int, payloads: MutableList<Any>){
        val itemDelegate=getItemDelegateByItemViewType(viewType)
        itemDelegate._bindData(holder,item,position,payloads)
    }

    private fun getItemDelegateByItemViewType(itemViewType:Int): ItemDelegate<T,*> {
        val itemDelegate=itemDelegateMap[itemViewType]
        return itemDelegate?.let { it } ?:throw IllegalArgumentException("illegal itemViewType: $itemViewType")
    }




}
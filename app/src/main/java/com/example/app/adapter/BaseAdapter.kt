package com.example.app.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import androidx.collection.SparseArrayCompat
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T>(listData:List<T>?):RecyclerView.Adapter<BaseViewHolder<*>>() {

    val data= mutableListOf<T>()

    private val itemDelegateHolder= ItemDelegateHolder<T>()

    private var onItemClickListener: OnItemClickListener<T>?=null

    init {
        listData?.let {
            data.addAll(it)
        }
        addItemDelegates()
    }


    fun setOnItemClickListener(onItemClickListener: OnItemClickListener<T>?){
        this.onItemClickListener=onItemClickListener
    }


    fun <B:ViewDataBinding> addItemDelegate(itemDelegate: ItemDelegate<T,B>){
        itemDelegateHolder.addItemDelegate(itemDelegate)
    }

    abstract fun addItemDelegates()

    override fun getItemViewType(position: Int): Int {
        return itemDelegateHolder.getItemViewType(getItem(position))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return itemDelegateHolder.onCreateViewHolder(parent,viewType)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val itemViewType=getItemViewType(position)
        val item=getItem(position)
        itemDelegateHolder.onBindViewHolder(holder,item,position,itemViewType,onItemClickListener)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int, payloads: MutableList<Any>) {
        if(payloads.isEmpty()){
            onBindViewHolder(holder,position)
        }else{
            val itemViewType=getItemViewType(position)
            val item=getItem(position)
            itemDelegateHolder.onBindViewHolder(holder,item,position,itemViewType,payloads)
        }
    }

    open fun getItem(position:Int)= data[position]
}


class BaseViewHolder<B:ViewDataBinding>(val dataBinding:B):RecyclerView.ViewHolder(dataBinding.root)

typealias OnItemClickListener<T> = (viewHolder: BaseViewHolder<*>, item:T, position:Int) ->Unit

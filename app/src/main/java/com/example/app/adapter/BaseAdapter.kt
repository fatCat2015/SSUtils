package com.example.app.adapter

import android.view.View
import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.RecyclerView

open class BaseAdapter<T>(listData:List<T>?):RecyclerView.Adapter<BaseViewHolder>() {

    val data= mutableListOf<T>()

    init {
        listData?.let {
            data.addAll(it)
        }
    }

    private var onItemClickListener: OnItemClickListener<T>?=null

    private val itemDelegateHolder= ItemDelegateHolder<T>()

    fun addItemDelegate(itemDelegate: ItemDelegate<T>){
        itemDelegateHolder.addItemDelegate(itemDelegate)
    }


    fun setOnItemClickListener(onItemClickListener: OnItemClickListener<T>?){
        this.onItemClickListener=onItemClickListener
    }

    override fun getItemViewType(position: Int): Int {
        return itemDelegateHolder.getItemViewType(getItem(position))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return itemDelegateHolder.onCreateViewHolder(parent,viewType)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val itemViewType=getItemViewType(position)
        val item=getItem(position)
        itemDelegateHolder.onBindViewHolder(holder,item,position,itemViewType,onItemClickListener)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int, payloads: MutableList<Any>) {
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


class BaseViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
    private val views=SparseArrayCompat<View>()

    fun <T:View> getView(viewId:Int):T{
        var view=views[viewId]
        if(view==null){
            view=itemView.findViewById(viewId)
            views.put(viewId,view)
        }
        return view as T
    }
}

typealias OnItemClickListener<T> = (viewHolder: BaseViewHolder, item:T, position:Int) ->Unit

package com.example.app.adapter

abstract class SimpleAdapter<T>(private val layoutResId:Int,data:List<T>?): WrappedAdapter<T>(data) {

    init {
        addItemDelegate(object : ItemDelegate<T> {
            override fun itemLayoutResId(): Int {
                return layoutResId
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
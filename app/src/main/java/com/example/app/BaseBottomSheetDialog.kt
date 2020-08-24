package com.example.app

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetDialog:BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(layoutResId(),container,false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHeightFullScreen()
        setBgTransparent()
        setData(arguments)
        setListeners()
    }

    private fun setHeightFullScreen(){
        var container=view?.parent as View?
        var lp=container?.layoutParams
        lp?.height=ViewGroup.LayoutParams.MATCH_PARENT
        container?.layoutParams=lp

    }

    private fun setBgTransparent(){
        var container=view?.parent as View?
        container?.setBackgroundColor(Color.TRANSPARENT)
    }

    abstract fun layoutResId():Int

    abstract fun setData(arguments:Bundle?)

    abstract fun setListeners()

}
package com.sck.guide

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.graphics.Color

object GuideManager {

    var screenOrientation=ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

    var guideBgColor= Color.parseColor("#99000000")

    internal var cancelable=false

    internal var cancelableOnTouchOutside=true

    private var debugModel=true

    private var sp:SharedPreferences?=null

    fun init(context: Context,debugModel:Boolean=false){
        this.debugModel=debugModel
        sp=context.getSharedPreferences("${context.packageName}.guide",Context.MODE_PRIVATE)
    }


    private val guideList=ArrayList<GuideParams>()

    fun begin(cancelable:Boolean=false,cancelableOnTouchOutside:Boolean=true): GuideManager {
        this.cancelable=cancelable
        this.cancelableOnTouchOutside=cancelableOnTouchOutside
        guideList.clear()
        return this
    }

    fun add(guideParams: GuideParams): GuideManager {
        guideList.add(guideParams)
        return this
    }

    fun add(vararg guideParamsList: GuideParams): GuideManager {
        guideList.addAll(guideParamsList)
        return this
    }

    fun add( guideParamsList: List<GuideParams>): GuideManager {
        guideList.addAll(guideParamsList)
        return this
    }


    fun show(activity:Activity){
        var guideList=ArrayList(guideList.filter {guide ->
            if(debugModel){
                true
            }else{
                sp?.getBoolean(guide.showTag,true)?:true
            }
        })
        if(guideList.isNotEmpty()){
            GuideActivity.open(activity, guideList)
        }

    }

    internal fun saveShowState(guide:GuideParams){
        if(!debugModel){
            sp?.edit()?.putBoolean(guide.showTag,false)?.apply()
        }
    }


}
package com.example.app

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout

class ShadowViewContainer:ConstraintLayout {

    private var shadowRadius=0

    private var shadowColor= Color.TRANSPARENT

    private var shadowSlideFlag=0


    private val paint:Paint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color=Color.WHITE
            style=Paint.Style.FILL
            setShadowLayer(shadowRadius.toFloat(),0F,0F,shadowColor)
        }
    }

    constructor(context: Context,attributes: AttributeSet):super(context,attributes){
        obtainAttrs(context,attributes)
        setPadding()
    }

    private fun obtainAttrs(context: Context,attributes: AttributeSet){
        val attrs=context.obtainStyledAttributes(attributes,R.styleable.ShadowViewContainer)
        shadowRadius=attrs.getDimensionPixelSize(R.styleable.ShadowViewContainer_svc_shadow_radius,
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,5F, Resources.getSystem().displayMetrics).toInt())
        shadowColor=attrs.getColor(R.styleable.ShadowViewContainer_svc_shadow_color,Color.GRAY)
        shadowSlideFlag=attrs.getInt(R.styleable.ShadowViewContainer_svc_shadow_slide,
            SHADOW_SLIDE_START or SHADOW_SLIDE_TOP or SHADOW_SLIDE_END or SHADOW_SLIDE_BOTTOM)
        attrs.recycle()

    }

    private fun setPadding(){
        setPadding(if(containsFlag(SHADOW_SLIDE_START)) shadowRadius else 0,
            if(containsFlag(SHADOW_SLIDE_TOP)) shadowRadius else 0,
            if(containsFlag(SHADOW_SLIDE_END)) shadowRadius else 0,
            if(containsFlag(SHADOW_SLIDE_BOTTOM)) shadowRadius else 0
        )
    }


    private fun containsFlag(flag:Int):Boolean{
        return shadowSlideFlag and flag==flag
    }


    override fun onFinishInflate() {
        super.onFinishInflate()
        if(childCount>1){
            throw IllegalStateException("ShadowViewContainer can host only one direct child")
        }
    }

    override fun dispatchDraw(canvas: Canvas?) {
        canvas?.drawRect(Rect(paddingLeft,paddingTop,measuredWidth-paddingRight,measuredHeight-paddingBottom),paint)
        super.dispatchDraw(canvas)
    }

    companion object{
        const val SHADOW_SLIDE_START=1
        const val SHADOW_SLIDE_TOP=2
        const val SHADOW_SLIDE_END=4
        const val SHADOW_SLIDE_BOTTOM=8
    }
}

package com.example.app

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.customview.widget.ViewDragHelper

class SwipeLayout:ConstraintLayout {

    private lateinit var viewDragHelper:ViewDragHelper

    private lateinit var contentView:View

    private lateinit var menuView:View

    private val flingVelValue=1000

    private var openFlag=false

    var onSlideStateChangedCallback:((open:Boolean)->Unit)?=null

    private var closeWhenClickContent=true

    private var consumeEventWhenIntercept=true


    constructor(context: Context,attributeSet: AttributeSet):super(context,attributeSet){
        initViewDragHelper()
    }

    private fun initViewDragHelper(){
        viewDragHelper= ViewDragHelper.create(this,callback)
    }

    private val callback=object:ViewDragHelper.Callback(){
        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            return true
        }

        override fun onViewCaptured(capturedChild: View, activePointerId: Int) {
            super.onViewCaptured(capturedChild, activePointerId)
        }

        override fun onEdgeDragStarted(edgeFlags: Int, pointerId: Int) {
            super.onEdgeDragStarted(edgeFlags, pointerId)
        }


        override fun getViewHorizontalDragRange(child: View): Int {
            return 1
        }

        override fun getViewVerticalDragRange(child: View): Int {
            return 1
        }

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            return if(child==menuView){
                Math.max((width-menuView.width),Math.min(left,width))
            }else{
                Math.min(0,Math.max(left,-menuView.width))
            }
        }

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            return 0
        }

        override fun onViewPositionChanged(changedView: View, left: Int, top: Int, dx: Int, dy: Int) {
            if(changedView==contentView){
                menuView.offsetLeftAndRight(dx)
            }else if(changedView==menuView){
                contentView.offsetLeftAndRight(dx)
            }
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            var left=releasedChild.left
            var top=releasedChild.top
            left=if(releasedChild==contentView){
                when{
                    xvel>flingVelValue -> 0
                    xvel <- flingVelValue -> -menuView.width
                    Math.abs(left) <menuView.width/2 -> 0
                    else -> -menuView.width
                }
            }else{
                when{
                    xvel>flingVelValue -> width
                    xvel <- flingVelValue -> width-menuView.width
                    Math.abs(left) >width-menuView.width/2 -> width
                    else -> width-menuView.width
                }
            }
            if(viewDragHelper.settleCapturedViewAt(left,top)){
                invalidate()
            }
        }

        override fun onViewDragStateChanged(state: Int) {
            super.onViewDragStateChanged(state)
            if(state==ViewDragHelper.STATE_IDLE){
                var currentState=false
                if(contentView.left==0){
                    currentState=false
                }else if(contentView.left==-menuView.width){
                    currentState=true
                }
                dispatchSlideState(currentState)

            }
        }

    }

    private fun dispatchSlideState(currentState:Boolean){
        if(currentState!=openFlag){
            openFlag=currentState
            onSlideStateChangedCallback?.invoke(openFlag)
        }
    }

    fun open(smooth:Boolean=true){
        if(smooth){
            if(viewDragHelper.smoothSlideViewTo(contentView,-menuView.width,0)){
                invalidate()
            }
        }else{
           //todo
        }

    }

    fun close(smooth:Boolean=true){
        if(smooth){
            if(viewDragHelper.smoothSlideViewTo(contentView,0,0)){
                invalidate()
            }
        }else{
            //todo
        }

    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        contentView=getChildAt(0)
        menuView=getChildAt(1)
    }

    override fun computeScroll() {
        if(viewDragHelper.continueSettling(true)){
            invalidate()
        }
    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        menuView.layout(measuredWidth,0,measuredWidth+menuView.measuredWidth,menuView.measuredHeight)
    }


    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return ev?.let {
            if(touchOnContentView(it)&&closeWhenClickContent&&openFlag){
                close()
                consumeEventWhenIntercept=false
                true
            }else{
                consumeEventWhenIntercept=true
                viewDragHelper.shouldInterceptTouchEvent(it)
            }
        }?:super.onInterceptTouchEvent(ev)
    }

    private fun touchOnContentView(ev: MotionEvent):Boolean{
        var rect=Rect(contentView.left,contentView.top,contentView.right,contentView.bottom)
        return rect.contains(ev.x.toInt(),ev.y.toInt())
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(consumeEventWhenIntercept){
            if (event != null) {
                viewDragHelper.processTouchEvent(event)
            }
            return true
        }else{
            return false
        }
    }


}
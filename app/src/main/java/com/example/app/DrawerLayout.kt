package com.example.app

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.customview.widget.ViewDragHelper

class DrawerLayout:ConstraintLayout {

    private val TAG="SlideBackLayout"

    private val flingVelValue=1000

    private lateinit var viewDragHelper:ViewDragHelper

    private var drawDirection= DRAWER_DIRECTION_LEFT

    private var drawerView:View?=null

    private var drawerViewId=0

    private var dimView:View?=null

    private var maxDimValue=0.3F

    var onDrawOpen:(()->Unit)?=null

    var onDrawSlide:((fraction:Float)->Unit)?=null

    var onDrawClose:(()->Unit)?=null



    constructor(context: Context,attributeSet: AttributeSet):super(context,attributeSet){
        obtainAttrs(context,attributeSet)
        initViewDragHelper()
    }

    private fun obtainAttrs(context: Context,attributeSet: AttributeSet){
        var attrs=context.obtainStyledAttributes(attributeSet,R.styleable.DrawerLayout)
        drawDirection=attrs.getInt(R.styleable.DrawerLayout_drawer_direction, DRAWER_DIRECTION_LEFT)
        drawerViewId=attrs.getResourceId(R.styleable.DrawerLayout_drawer_view_id,0)
        maxDimValue=attrs.getFloat(R.styleable.DrawerLayout_max_dim_value,0.35F)
        attrs.recycle()
    }

    private fun initViewDragHelper(){
        viewDragHelper= ViewDragHelper.create(this,viewDragHelperCallback)
        when(drawDirection){
            DRAWER_DIRECTION_LEFT ->{
                viewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_RIGHT)
            }
            DRAWER_DIRECTION_TOP ->{
                viewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_BOTTOM)
            }
            DRAWER_DIRECTION_RIGHT ->{
                viewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT)
            }
            DRAWER_DIRECTION_BOTTOM ->{
                viewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_TOP)
            }
        }
    }


    private val viewDragHelperCallback=object:ViewDragHelper.Callback(){
        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            return drawerView==child
        }

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            return when(drawDirection){
                DRAWER_DIRECTION_LEFT ->{
                    Math.max(0,Math.min(left,width))
                }
                DRAWER_DIRECTION_RIGHT ->{
                    Math.max(-width,Math.min(left,0))
                }
                else -> 0
            }
        }

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            return when(drawDirection){
                DRAWER_DIRECTION_TOP ->{
                    Math.max(0,Math.min(top,height))
                }
                DRAWER_DIRECTION_BOTTOM ->{
                    Math.max(-height,Math.min(top,0))
                }
                else -> 0
            }
        }

        override fun getViewHorizontalDragRange(child: View): Int {
            return 1
        }

        override fun getViewVerticalDragRange(child: View): Int {
            return 1
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            slideDrawerWhenRelease(releasedChild,xvel,yvel)
        }

        override fun onViewDragStateChanged(state: Int) {
            super.onViewDragStateChanged(state)
            if(state==ViewDragHelper.STATE_IDLE){
                when(drawDirection){
                    DRAWER_DIRECTION_LEFT ->{
                        if(drawerView?.left==0){
                            onDrawOpen?.invoke()
                        }else if(drawerView?.left==width){
                            onDrawClose?.invoke()
                        }
                    }
                    DRAWER_DIRECTION_TOP ->{
                        if(drawerView?.top==0){
                            onDrawOpen?.invoke()
                        }else if(drawerView?.top==height){
                            onDrawClose?.invoke()
                        }
                    }
                    DRAWER_DIRECTION_RIGHT ->{
                        if(drawerView?.left==0){
                            onDrawOpen?.invoke()
                        }else if(drawerView?.left==-width){
                            onDrawClose?.invoke()
                        }
                    }
                    DRAWER_DIRECTION_BOTTOM ->{
                        if(drawerView?.top==0){
                            onDrawOpen?.invoke()
                        }else if(drawerView?.top==-height){
                            onDrawClose?.invoke()
                        }
                    }
                }

            }
        }

        override fun onViewPositionChanged(changedView: View, left: Int, top: Int, dx: Int, dy: Int) {
            super.onViewPositionChanged(changedView, left, top, dx, dy)
            var slideFraction= when(drawDirection){
                DRAWER_DIRECTION_LEFT ->{
                    left*1F/width
                }
                DRAWER_DIRECTION_TOP ->{
                    top*1F/height
                }
                DRAWER_DIRECTION_RIGHT ->{
                    Math.abs(left)*1F/width
                }
                DRAWER_DIRECTION_BOTTOM ->{
                    Math.abs(top)*1F/height
                }
                else ->{
                    1F
                }
            }
            onDrawSlide?.invoke(slideFraction)
            setDimValue(1-slideFraction)
        }


        override fun onEdgeDragStarted(edgeFlags: Int, pointerId: Int) {
            drawerView?.let { viewDragHelper.captureChildView(it,pointerId) }
        }

    }


    private fun slideDrawerWhenRelease(releasedChild: View, xvel: Float, yvel: Float){
        var left=releasedChild.left
        var top=releasedChild.top
        when(drawDirection){
            DRAWER_DIRECTION_LEFT ->{
                left = when{
                    xvel>flingVelValue ->{
                        width
                    }
                    xvel<-flingVelValue ->{
                        0
                    }
                    left<width/2 ->{
                        0
                    }
                    else ->{
                        width
                    }
                }
            }
            DRAWER_DIRECTION_TOP ->{
                top = when{
                    yvel>flingVelValue ->{
                        height
                    }
                    yvel<-flingVelValue ->{
                        0
                    }
                    top<height/2 ->{
                        0
                    }
                    else ->{
                        height
                    }
                }
            }
            DRAWER_DIRECTION_RIGHT ->{
                left = when{
                    xvel>flingVelValue ->{
                        0
                    }
                    xvel<-flingVelValue ->{
                        -width
                    }
                    Math.abs(left)<width/2 ->{
                        0
                    }
                    else ->{
                        -width
                    }
                }
            }
            DRAWER_DIRECTION_BOTTOM ->{
                top = when{
                    yvel>flingVelValue ->{
                        0
                    }
                    yvel<-flingVelValue ->{
                        -height
                    }
                    Math.abs(top)<height/2 ->{
                        0
                    }
                    else ->{
                        -height
                    }
                }
            }
        }
        viewDragHelper.settleCapturedViewAt(left,top)
        invalidate()
    }


    override fun onFinishInflate() {
        super.onFinishInflate()
        drawerView=findViewById(drawerViewId)
        addDimView()
    }

    private fun addDimView(){
        var index=indexOfChild(drawerView)
        if(index>=0){
            dimView=View(context)
            addView(dimView,index,LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
            setDimValue(1F)
        }
    }


    private fun setDimValue(fraction:Float){
        dimView?.let {
            var dimViewBgColor=Color.argb((fraction*maxDimValue*255).toInt(),0,0,0)
            it.setBackgroundColor(dimViewBgColor)
        }
    }

    override fun computeScroll() {
        if(viewDragHelper.continueSettling(true)){
            invalidate()
        }
    }



    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return ev?.let { viewDragHelper.shouldInterceptTouchEvent(it) }?:super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            viewDragHelper.processTouchEvent(event)
        }
        return true;
    }

    companion object{
        const val DRAWER_DIRECTION_LEFT=0
        const val DRAWER_DIRECTION_TOP=1
        const val DRAWER_DIRECTION_RIGHT=2
        const val DRAWER_DIRECTION_BOTTOM=3
    }

}
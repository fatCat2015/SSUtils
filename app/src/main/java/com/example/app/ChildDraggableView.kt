package com.example.app

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.customview.widget.ViewDragHelper
import java.util.*

class ChildDraggableView : ConstraintLayout {

    private var draggableChildResId=0

    private var actionWhenRelease= KEEP_STATE_WHEN_RELEASE

    private var oriLeft=0

    private var oriTop=0

    private lateinit var viewDragHelper:ViewDragHelper

    constructor(context: Context?) : super(context) {
        obtainAttrs(context,null)
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        obtainAttrs(context,attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        obtainAttrs(context,attrs)
    }

    private fun obtainAttrs(context: Context?,attrs: AttributeSet?){
        var a=context?.obtainStyledAttributes(attrs,R.styleable.ChildDraggableView)
        draggableChildResId=a?.getResourceId(R.styleable.ChildDraggableView_draggable_child_id,0)?:0
        actionWhenRelease=a?.getInt(R.styleable.ChildDraggableView_action_when_release, KEEP_STATE_WHEN_RELEASE)?:KEEP_STATE_WHEN_RELEASE
        a?.recycle()
    }

    private var dragCallback=object: ViewDragHelper.Callback() {
        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            return child.id==draggableChildResId
        }

        override fun onViewCaptured(capturedChild: View, activePointerId: Int) {
            capturedChild.isClickable=true
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
                capturedChild.translationZ=1F
            }
            oriLeft=capturedChild.left
            oriTop=capturedChild.top
        }

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            return left
        }

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            return top
        }

        override fun getViewHorizontalDragRange(child: View): Int {
            return 1
        }

        override fun getViewVerticalDragRange(child: View): Int {
            return 1
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            when(actionWhenRelease){
                KEEP_STATE_WHEN_RELEASE->{}
                BACK_TO_ORI_WHEN_RELEASE->{
                    viewDragHelper.settleCapturedViewAt(oriLeft,oriTop)
                    invalidate()
                }
                ATTACH_TO_SIDE_WHEN_RELEASE->{
                    var targetLeft=releasedChild.left
                    var targetTop=releasedChild.top
                    var left=targetLeft
                    var top=targetTop
                    var right=width-releasedChild.right
                    var bottom=height-releasedChild.bottom
                    var tempList= listOf(left,top,right,bottom)
                    Collections.sort(tempList)
                    var minValue=tempList[0]
                    when(minValue){
                        left ->{
                            targetLeft=0
                        }
                        top ->{
                            targetTop=0
                        }
                        right ->{
                            targetLeft=width-releasedChild.width
                        }
                        bottom ->{
                            targetTop=height-releasedChild.height
                        }
                    }

                    viewDragHelper.settleCapturedViewAt(targetLeft,targetTop)
                    invalidate()
                }
            }
        }
    }


    init{
        viewDragHelper= ViewDragHelper.create(this,dragCallback)
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
        return true
    }

    companion object{
        const val KEEP_STATE_WHEN_RELEASE=0
        const val BACK_TO_ORI_WHEN_RELEASE=1
        const val ATTACH_TO_SIDE_WHEN_RELEASE=2
    }

}


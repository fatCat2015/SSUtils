package com.example.app

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.AttributeSet
import android.view.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.DialogFragment
import androidx.transition.TransitionSet

abstract class BaseTopFilterFragment:DialogFragment() {

    private var offsetY=0

    open val maxDimValue=0.5F

    open val animationDuration=300L

    open val maxHeightRatio=0.5F

    private var clTopFilterLayout:ConstraintLayout?=null
    private var clTopFilterContent:ConstraintLayout?=null
    private var vDim:View?=null


    companion object {
        const val offsetY_key="offsetY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        offsetY=arguments?.getInt(offsetY_key,0)?:0
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL)
        return inflater.inflate(R.layout.dialog_fragment_base_top_filter,container,false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clTopFilterLayout=view.findViewById(R.id.clTopFilterLayout)
        clTopFilterContent=view.findViewById(R.id.clTopFilterContent)
        var constraintSet=ConstraintSet()
        constraintSet.clone(clTopFilterLayout)
        constraintSet.constrainMaxHeight(clTopFilterContent!!.id,((getContentLayoutHeight()?:0)*maxHeightRatio).toInt())
        constraintSet.applyTo(clTopFilterLayout)
        layoutInflater.inflate(layoutResId(),clTopFilterContent)
        showAnimations(true)
        vDim?.setOnClickListener {
            dismiss(true)
        }

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setData()
        setListeners()
    }

    abstract fun layoutResId():Int

    abstract fun setData()

    abstract fun setListeners()


    override fun onStart() {
        super.onStart()
        var lp=dialog?.window?.attributes
        lp?.width=WindowManager.LayoutParams.MATCH_PARENT
        lp?.height=getContentLayoutHeight()
        lp?.dimAmount=0F
        lp?.gravity=Gravity.TOP
        lp?.y=offsetY
        dialog?.window?.attributes=lp
    }

    private fun getContentLayoutHeight():Int?{
        return activity?.resources?.displayMetrics?.heightPixels?.minus(offsetY)
    }


    private fun showAnimations(open:Boolean){
        clTopFilterContent?.post {
            var heightAnimator=ValueAnimator.ofInt(if(open) 1 else clTopFilterContent?.height?:0, if(open) clTopFilterContent?.height?:0 else 1)
            heightAnimator.addUpdateListener {
                var lp=clTopFilterContent?.layoutParams
                lp?.height=it.animatedValue as Int
                clTopFilterContent?.layoutParams=lp
                vDim?.alpha=if(open) it.animatedFraction*maxDimValue else (1-it.animatedFraction)*maxDimValue
            }
            heightAnimator.addListener(object :AnimatorListenerAdapter(){
                override fun onAnimationEnd(animation: Animator?) {
                    if(!open){
                        dismissAllowingStateLoss()
                    }
                }
            })
            heightAnimator.duration=animationDuration
            heightAnimator.start()
        }
    }


    fun dismiss(showAnimator:Boolean) {
        if(showAnimator){
            showAnimations(false)
        }else{
            dismiss()
        }
    }

}
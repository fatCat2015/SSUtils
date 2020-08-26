package com.example.app

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Choreographer
import android.view.Gravity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener

class CodeEditText:ConstraintLayout {


    private var codeLength=6

    private var textBackGround:Drawable?=null

    private var codeSpace=0

    private var textColor= ContextCompat.getColor(context, R.color.colorAccent)

    private var textSize=0F

    private var textBold=false

    private var editText:EditText?=null

    private var showTextViews=ArrayList<TextView>()

    var inputCompleteCallback:((String)->Unit)?=null

    private var drawCursorFlag=true

    private var cursorColor=0

    private var cursorWidth=0

    private var cursorHeight=0

    private val cursorDrawable:Drawable by lazy {
        val drawable=GradientDrawable()
        drawable.setColor(cursorColor)
        drawable.cornerRadius=cursorWidth.toFloat()
        drawable.setSize(cursorWidth,cursorHeight)
        drawable
    }

    constructor(context: Context):this(context,null){
    }

    constructor(context: Context, attributeSet: AttributeSet?):super(context,attributeSet){
        obtainAttrs(context,attributeSet)
        initViews(context)
        setListeners()
    }

    private fun obtainAttrs(context: Context, attributeSet: AttributeSet?){
        var attrs=context.obtainStyledAttributes(attributeSet,R.styleable.CodeEditText)
        textBackGround=attrs.getDrawable(R.styleable.CodeEditText_code_text_background)
        codeLength=attrs.getInt(R.styleable.CodeEditText_code_length,6)
        codeSpace=attrs.getDimensionPixelSize(R.styleable.CodeEditText_code_interval,dip2px(10F))
        textColor=attrs.getColor(R.styleable.CodeEditText_code_text_color, ContextCompat.getColor(context, R.color.colorAccent))
        textSize=attrs.getDimension(R.styleable.CodeEditText_code_text_size,dip2px(18F).toFloat())
        textBold=attrs.getBoolean(R.styleable.CodeEditText_code_text_bold,false)
        cursorColor=attrs.getColor(R.styleable.CodeEditText_code_cursor_color,textColor)
        cursorWidth=attrs.getDimensionPixelSize(R.styleable.CodeEditText_code_cursor_width,dip2px(2F))
        cursorHeight=attrs.getDimensionPixelSize(R.styleable.CodeEditText_code_cursor_height,dip2px(20F))
        attrs.recycle()
    }


    private fun initViews(context: Context){
        var linearLayout=LinearLayout(context)
        linearLayout.orientation=LinearLayout.HORIZONTAL
        linearLayout.showDividers=LinearLayout.SHOW_DIVIDER_MIDDLE
        linearLayout.dividerDrawable=GradientDrawable().apply { setSize(codeSpace,codeSpace) }
        addView(linearLayout,LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT))
        var textViewLayoutParams=LinearLayout.LayoutParams(0,LayoutParams.MATCH_PARENT)
        textViewLayoutParams.weight=1F
        for(index in 0 until codeLength){
            showTextViews.add(TextView(context).also {
                it.setTextSize(TypedValue.COMPLEX_UNIT_PX,this@CodeEditText.textSize)
                it.setTextColor(textColor)
                if(textBold){
                    it.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                }
                it.gravity=Gravity.CENTER
                it.background=textBackGround
                linearLayout.addView(it,textViewLayoutParams)
            })
        }
        addView(CodeEditText(codeLength,context).also { editText=it },LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT))
    }


    private fun setListeners() {
        editText?.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                showTextViews.forEach {
                    it.text=""
                }
                s?.let {
                    for((index,character) in s.withIndex()){
                        showTextViews[index].text=character.toString()
                    }
                }
                if(s?.length==codeLength){
                    inputCompleteCallback?.invoke(s.toString())
                }
                postInvalidate()
            }
        })
    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        if(canvas==null) return
        if(drawCursorFlag){
            var textLength=editText?.text?.length?:0
            if(textLength<codeLength){
                val tvTemp=showTextViews[textLength]
                val tvLinear=tvTemp.parent as View
                val left=(tvTemp.width-cursorWidth)/2+tvTemp.left+tvLinear.left
                val top=(tvTemp.height-cursorHeight)/2+tvTemp.top+tvLinear.top
                val right=left+cursorWidth
                val bottom=top+cursorHeight
                cursorDrawable.setBounds(left,top,right,bottom)
                cursorDrawable.draw(canvas)
            }
        }
        stopDrawCursor()
        startDrawCursor()
    }

    private fun startDrawCursor(){
        Choreographer.getInstance().postFrameCallbackDelayed (drawCursorCallback,500)
    }

    private fun stopDrawCursor(){
        Choreographer.getInstance().removeFrameCallback (drawCursorCallback)
    }

    private val drawCursorCallback= Choreographer.FrameCallback {
        drawCursorFlag=!drawCursorFlag
        postInvalidate()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopDrawCursor()
    }


    private fun dip2px(dipValue: Float): Int {
        val displayMetricsDensity = context.resources.displayMetrics.density
        return (dipValue * displayMetricsDensity + 0.5f).toInt()
    }

    fun setText(text:String){
        editText?.setText(text)
    }


    private class CodeEditText:AppCompatEditText{
        constructor(maxLength:Int,context: Context):super(context){
            isLongClickable=false
            filters= arrayOf(InputFilter.LengthFilter(maxLength))
            inputType=EditorInfo.TYPE_CLASS_NUMBER
            background=null
            setTextColor(Color.TRANSPARENT)
            isCursorVisible=false
        }
        override fun onSelectionChanged(selStart: Int, selEnd: Int) {
            super.onSelectionChanged(selStart, selEnd)
            setSelection(text?.length?:0)
        }

    }
}
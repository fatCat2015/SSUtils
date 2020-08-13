package com.example.app

import android.content.Context
import android.text.SpannableString
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.cat.sutils.span.ClickableSpan
import java.util.regex.Pattern

class AutoLinkTextView : AppCompatTextView {


    var webClickCallback:((String)->Unit)?= null

    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        text?.let {
            super.setText(formatWebUrlText(it.toString()), type)
        }?:  super.setText(text, type)

    }
    private fun formatWebUrlText(text:String):SpannableString{
        val spannableString=SpannableString(text)
        val webUrlPattern=Pattern.compile("((http[s]{0,1}|ftp)://[a-zA-Z0-9\\.\\-]+\\.([a-zA-Z]{2,4})(:\\d+)?(/[a-zA-Z0-9\\.\\-~!@#$%^&*+?:_/=<>]*)?)|(www.[a-zA-Z0-9\\.\\-]+\\.([a-zA-Z]{2,4})(:\\d+)?(/[a-zA-Z0-9\\.\\-~!@#$%^&*+?:_/=<>]*)?)")
        val matcher=webUrlPattern.matcher(text)
        var startIndex=0
        var endIndex=0
        while (matcher.find()){
            val webUrl=matcher.group()
            startIndex=text.indexOf(webUrl,endIndex)
            endIndex=startIndex+webUrl.length
            spannableString.setSpan(ClickableSpan(ContextCompat.getColor(context,R.color.colorPrimary)) {
                webClickCallback?.invoke(webUrl)
            }.attachToTextView(this),startIndex,endIndex,SpannableString.SPAN_INCLUSIVE_EXCLUSIVE)
        }
        return spannableString

    }





}
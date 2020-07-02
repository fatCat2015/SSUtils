package com.example.app

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory

class CircleImageView : AppCompatImageView {

    private var borderColor= Color.TRANSPARENT

    private var borderWidth:Float=0F


    constructor(context: Context?) : super(context) {

    }
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    ) {
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
    }


    override fun setImageBitmap(bitmap: Bitmap?) {
        bitmap?.let {
            //1.裁剪图片区域 生成一个正方形Bitmap
            var bitmapWidth=bitmap.width
            var bitmapHeight=bitmap.height
            var width= bitmapWidth.coerceAtMost(bitmapHeight)
            var squareBitmap=Bitmap.createBitmap(bitmap,(bitmapWidth-width)/2,(bitmapHeight-width)/2,width,width)

            //2.创建一个包含边框区域的空Bitmap并关联一个Canvas
            var realWidth=(width+2*borderWidth).toInt()
            var targetBitmap=Bitmap.createBitmap(realWidth,realWidth,Bitmap.Config.ARGB_8888)
            var canvas= Canvas(targetBitmap)

            //3.画上正方形Bitmap
            canvas.drawBitmap(squareBitmap,borderWidth,borderWidth, Paint())

            //4.画上边框圆
            if(borderWidth>0.05){
                var paint= Paint(Paint.ANTI_ALIAS_FLAG)
                paint.strokeWidth=borderWidth
                paint.style= Paint.Style.STROKE
                paint.color=borderColor
                canvas.drawCircle((realWidth/2).toFloat(), (realWidth/2).toFloat(), (realWidth/2).toFloat()-borderWidth/2,paint)
            }

            //5.转换为原型图片
            var circleDrawable= RoundedBitmapDrawableFactory.create(resources,targetBitmap)
            circleDrawable.isCircular=true

            //6.展示图片
            setImageDrawable(circleDrawable)
        }
    }


}
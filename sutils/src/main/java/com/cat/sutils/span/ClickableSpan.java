package com.cat.sutils.span;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class ClickableSpan extends android.text.style.ClickableSpan {

    private int clickableTextColor;

    private View.OnClickListener onClickListener;

    public ClickableSpan(int clickableTextColor, View.OnClickListener onClickListener){
        this.clickableTextColor=clickableTextColor;
        this.onClickListener=onClickListener;
    }

    @Override
    public void onClick(@NonNull View widget) {
        if(onClickListener!=null){
            onClickListener.onClick(widget);
        }
    }

    @Override
    public void updateDrawState(@NonNull TextPaint ds) {
        super.updateDrawState(ds);
        ds.setColor(clickableTextColor);
        ds.setUnderlineText(false);
    }

    public ClickableSpan attachToTextView(TextView textView){
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setHighlightColor(Color.TRANSPARENT);
        return this;
    }
}

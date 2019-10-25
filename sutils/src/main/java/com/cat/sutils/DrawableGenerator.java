package com.cat.sutils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

public class DrawableGenerator {


    //solid
    private int color;
    private int highlightColor;
    private int disableColor;

    //corner
    private float radius;
    private float[] radii;

    //stroke
    private int borderColor;
    private int borderWidth;
    private int dashWidth;
    private int dashGap;

    private boolean enableRipple;


    public static Builder builder(){
        return new Builder();
    }


    private DrawableGenerator(Builder builder){
        color=builder.color;
        highlightColor=builder.highlightColor;
        disableColor=builder.disableColor;

        borderColor=builder.borderColor;
        borderWidth=builder.borderWidth;
        dashWidth=builder.dashWidth;
        dashGap=builder.dashGap;

        radius=builder.radius;
        radii=builder.radii;

        enableRipple=builder.enableRipple;
    }


    public Drawable generate(){
        StateListDrawable stateListDrawable=new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_enabled},generateClickDrawable());
        stateListDrawable.addState(new int[]{-android.R.attr.state_enabled},generateDrawable(disableColor==0?color:disableColor));
        return stateListDrawable;
    }

    private  Drawable generateClickDrawable(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP&&enableRipple){
            return generateClickRippleDrawable();
        }else{
            return generateClickDrawableBefore21();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private  Drawable generateClickRippleDrawable(){
        RippleDrawable rippleDrawable=new RippleDrawable(ColorStateList.valueOf(highlightColor),generateDrawable(color),null);
        return rippleDrawable;
    }

    private  Drawable generateClickDrawableBefore21(){
        StateListDrawable stateListDrawable=new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed},generateDrawable(highlightColor));
        stateListDrawable.addState(new int[]{-android.R.attr.state_pressed},generateDrawable(color));
        return stateListDrawable;
    }


    private Drawable generateDrawable(int color){
        GradientDrawable gradientDrawable=new GradientDrawable();
        gradientDrawable.setColor(color);
        if(radii==null){
            gradientDrawable.setCornerRadius(radius);
        }else{
            gradientDrawable.setCornerRadii(radii);
        }
        if(borderWidth!=0){
            gradientDrawable.setStroke(borderWidth,borderColor,dashWidth,dashGap);
        }
        return gradientDrawable;
    }


    public static class Builder{

        private int color;
        private int highlightColor;
        private int disableColor;

        private float radius;
        private float[] radii;

        private int borderColor;
        private int borderWidth;
        private int dashWidth;
        private int dashGap;

        private boolean enableRipple;

        public Builder setColor(int color) {
            this.color = color;
            return this;
        }

        public Builder setHighlightColor(int highlightColor) {
            this.highlightColor = highlightColor;
            return this;
        }

        public Builder setDisableColor(int disableColor) {
            this.disableColor = disableColor;
            return this;
        }

        public Builder setCornerRadius(float radius) {
            this.radius = radius;
            return this;
        }

        public Builder settCornerRadii(float[] radii) {
            this.radii = radii;
            return this;
        }

        public Builder setBorderColor(int borderColor) {
            this.borderColor = borderColor;
            return this;
        }

        public Builder setBorderWidth(int borderWidth) {
            this.borderWidth = borderWidth;
            return this;
        }

        public Builder setEnableRipple(boolean enableRipple) {
            this.enableRipple = enableRipple;
            return this;
        }

        public Builder setDashWidth(int dashWidth) {
            this.dashWidth = dashWidth;
            return this;
        }

        public Builder setDashGap(int dashGap) {
            this.dashGap = dashGap;
            return this;
        }

        public DrawableGenerator build(){
            return new DrawableGenerator(this);
        }
    }



    public static Drawable generateImageClickDrawable(Context context,int normalDrawableId,int clickedDrawableId){
        StateListDrawable stateListDrawable=new StateListDrawable();
        stateListDrawable.addState(new int[]{-android.R.attr.state_pressed}, ContextCompat.getDrawable(context,normalDrawableId));
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed},ContextCompat.getDrawable(context,clickedDrawableId));
        return stateListDrawable;
    }





}

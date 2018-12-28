package com.cat.sutils;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;

import java.sql.Driver;

public class ColorTransition {


    private long duration;
    private int startColor;
    private int endColor;
    private OnColorUpdateListener onColorUpdateListener;
    private ValueAnimator valueAnimator;


    private ColorTransition(Builder builder) {
        duration=builder.duration;
        startColor=builder.startColor;
        endColor=builder.endColor;
        onColorUpdateListener=builder.onColorUpdateListener;

    }



    public void translate(){
        destroy();
        if(duration<=0){
            duration=1000;
        }
        if(startColor==0||endColor==0){
            throw new IllegalArgumentException("startColor or endColor needed");
        }
        valueAnimator=createColorTransitionAnimator(startColor,endColor,duration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currentColor= (int) animation.getAnimatedValue();
                if(onColorUpdateListener!=null){
                    onColorUpdateListener.onColorUpdate(currentColor);
                }
            }
        });
        valueAnimator.start();
    }

    private ValueAnimator createColorTransitionAnimator(int startColor,int endColor,long duration ){
        ValueAnimator valueAnimator=ValueAnimator.ofObject(new ArgbEvaluator(),startColor,endColor);
        valueAnimator.setDuration(duration);
        return valueAnimator;
    }


    public void destroy(){
        if(valueAnimator!=null&&valueAnimator.isRunning()){
            valueAnimator.cancel();
        }
    }



    public static Builder builder(){
        return new Builder();
    }



    public static class Builder{
        private long duration;
        private int startColor;
        private int endColor;
        private OnColorUpdateListener onColorUpdateListener;

        public Builder duration(long duration) {
            this.duration = duration;
            return this;
        }

        public Builder startColor(int startColor) {
            this.startColor = startColor;
            return this;
        }

        public Builder endColor(int endColor) {
            this.endColor = endColor;
            return this;
        }

        public Builder onColorUpdateListener(OnColorUpdateListener onColorUpdateListener) {
            this.onColorUpdateListener = onColorUpdateListener;
            return this;
        }

        public ColorTransition build(){
            return new ColorTransition(this);
        }

    }


    public interface OnColorUpdateListener{
        void onColorUpdate(int currentColor);
    }



}

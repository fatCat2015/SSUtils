package com.cat.sutils;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import androidx.core.util.Consumer;

import java.util.Objects;

public class ColorTransition {


    private long duration;
    private int startColor;
    private int endColor;
    private ValueAnimator valueAnimator;


    private ColorTransition(Builder builder) {
        duration=builder.duration;
        startColor=builder.startColor;
        endColor=builder.endColor;

    }



    public ColorTransition translate(Consumer<Integer> consumer){
        Objects.requireNonNull(consumer);
        destroy();
        if(duration<=0){
            duration=1000;
        }
        if(startColor==0||endColor==0){
            throw new IllegalArgumentException("startColor or endColor needed");
        }
        valueAnimator=createColorTransitionAnimator(startColor,endColor,duration);
        valueAnimator.addUpdateListener(animation -> {
            int currentColor= (int) animation.getAnimatedValue();
            consumer.accept(currentColor);
        });
        valueAnimator.start();
        return this;
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


        public ColorTransition build(){
            return new ColorTransition(this);
        }

    }





}

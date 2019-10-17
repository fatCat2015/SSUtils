package com.cat.aop.aspect;


import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.cat.aop.BuildConfig;
import com.cat.aop.annotation.Trace;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
public class RunOnMainThreadAspect {


    private Handler handler=new Handler(Looper.getMainLooper());

    private static final String POINTCUT_METHOD = "execution(@com.cat.aop.annotation.RunOnMainThread * *(..))";

    @Pointcut(POINTCUT_METHOD)
    public void methodWithUIThreadAnnotation() {

    }

    @Around("methodWithUIThreadAnnotation()")
    public void executeOnUiThread(final ProceedingJoinPoint joinPoint) throws Throwable {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    joinPoint.proceed();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });
    }

}

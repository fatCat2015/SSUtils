package com.cat.aop.aspect;


import android.util.Log;

import com.cat.aop.BuildConfig;
import com.cat.aop.annotation.Event;
import com.cat.aop.annotation.Trace;
import com.cat.aop.event.EventUploadProxy;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
public class EventAspect {



    private static final String POINTCUT_METHOD = "execution(@com.cat.aop.annotation.Event * *(..))";

    @Pointcut(POINTCUT_METHOD)
    public void methodWithEventAnnotation() {

    }

    @Before("methodWithEventAnnotation()")
    public void uploadEvent(JoinPoint joinPoint) throws Throwable {
        EventUploadProxy.getInstance().uploadEvent(getEventName(joinPoint),joinPoint.getArgs());
    }


    private String getEventName(JoinPoint joinPoint){
        MethodSignature methodSignature= (MethodSignature) joinPoint.getSignature();
        Method method=methodSignature.getMethod();
        Event annotation = method.getAnnotation(Event.class);
        return annotation.value();
    }

}

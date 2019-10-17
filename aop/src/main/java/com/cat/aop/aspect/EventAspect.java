package com.cat.aop.aspect;


import android.util.Log;

import com.cat.aop.BuildConfig;
import com.cat.aop.annotation.Event;
import com.cat.aop.annotation.Trace;
import com.cat.aop.event.EventParam;
import com.cat.aop.event.EventUploadProxy;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.json.JSONObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

@Aspect
public class EventAspect {



    private static final String POINTCUT_METHOD = "execution(@com.cat.aop.annotation.Event * *(..))";

    @Pointcut(POINTCUT_METHOD)
    public void methodWithEventAnnotation() {

    }

    @Before("methodWithEventAnnotation()")
    public void uploadEvent(JoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature= (MethodSignature) joinPoint.getSignature();
        Method method=methodSignature.getMethod();
        Event annotation = method.getAnnotation(Event.class);
        EventUploadProxy.getInstance().uploadEvent(annotation.value(),getEventJsonParams(method,joinPoint.getArgs()));
    }


    private String getEventJsonParams(Method method,Object[] args) throws Throwable{
        JSONObject jsonParams=new JSONObject();
        Annotation[][] paramsAnnotations = method.getParameterAnnotations();
        int paramsLength=paramsAnnotations.length;
        for (int i = 0; i <paramsLength ; i++) {
            Annotation[] annotations=paramsAnnotations[i];
            for (Annotation annotation:annotations) {
                if(annotation instanceof EventParam){
                    String paramName=((EventParam) annotation).value();
                    Object paramValue=args[i];
                    jsonParams.put(paramName,paramValue);
                    break;
                }
            }
        }
        return jsonParams.toString();
    }





}

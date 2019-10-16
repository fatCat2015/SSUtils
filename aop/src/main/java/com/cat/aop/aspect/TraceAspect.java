package com.cat.aop.aspect;


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
public class TraceAspect {



    private static final String POINTCUT_METHOD = "execution(@com.cat.aop.annotation.Trace * *(..))";

    @Pointcut(POINTCUT_METHOD)
    public void methodWithTraceAnnotation() {

    }

    @Around("methodWithTraceAnnotation()")
    public void trace(ProceedingJoinPoint joinPoint) throws Throwable {
        if (BuildConfig.DEBUG) {
            String tag=getLogTag(joinPoint);
            String methodName=joinPoint.getSignature().getName();
            Log.i(tag,String.format("%s 开始执行 参数:%s 线程id:%s",methodName, Arrays.toString(joinPoint.getArgs()),Thread.currentThread().getId()));
            long startTime=System.currentTimeMillis();
            joinPoint.proceed();
            Log.i(tag,String.format("%s 结束执行 耗时:%sms",methodName,(System.currentTimeMillis()-startTime)));
        }
    }


    private String getLogTag(JoinPoint joinPoint){
        MethodSignature methodSignature= (MethodSignature) joinPoint.getSignature();
        Method method=methodSignature.getMethod();
        Trace annotation = method.getAnnotation(Trace.class);
        return annotation.value();
    }

}

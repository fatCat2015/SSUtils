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

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

@Aspect
public class AsyncAspect {



    private static final String POINTCUT_METHOD = "execution(@com.cat.aop.annotation.Async * *(..))";

    @Pointcut(POINTCUT_METHOD)
    public void methodWithAsyncAnnotation() {

    }

    @Around("methodWithAsyncAnnotation()")
    public void executeAsynchronously(ProceedingJoinPoint joinPoint) throws Throwable {
        Observable.create((ObservableOnSubscribe<Void>) emitter -> {
            try {
                joinPoint.proceed();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }).subscribeOn(Schedulers.io()).subscribe();

    }


}

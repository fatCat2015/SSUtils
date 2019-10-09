package com.cat.aop.aspect;


import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class AvoidMultiClickAspect {

    private static final String POINTCUT_METHOD = "execution(* android.view.View.OnClickListener.onClick(..))";

    @Pointcut(POINTCUT_METHOD)
    public void onClickMethod() {
    }

    @Around("onClickMethod()")
    public void avoidMultiClick( ProceedingJoinPoint joinPoint) throws Throwable {
        Log.i("sck220", "avoidMultiClick: "+joinPoint);
        joinPoint.proceed();
    }
}

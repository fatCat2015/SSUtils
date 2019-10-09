package com.cat.aop.aspect;


import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class AvoidMultiClickAspect {

    private static final String POINTCUT_METHOD = "execution(@com.cat.aop.annotation.AvoidMultiClick * *(..))";

    @Pointcut(POINTCUT_METHOD)
    public void methodWithAvoidMultiClickAnnotation() {
    }

    @Around("methodWithAvoidMultiClickAnnotation()")
    public void avoidMultiClick(ProceedingJoinPoint joinPoint) throws Throwable {

    }
}

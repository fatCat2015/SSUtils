package com.cat.aop.aspect;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.cat.aop.BuildConfig;
import com.cat.aop.annotation.AvoidMultipleExecutions;
import com.cat.aop.annotation.NeedLog;
import com.cat.aop.annotation.Permission;
import com.cat.aop.permission.IPermissionCheck;
import com.cat.aop.permission.IPermissionCheckResult;
import com.cat.aop.permission.PermissionCheck;
import com.cat.aop.permission.PermissionCheckResult;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

@Aspect
public class LogAspect {



    private static final String POINTCUT_METHOD = "execution(@com.cat.aop.annotation.NeedLog * *(..))";

    @Pointcut(POINTCUT_METHOD)
    public void methodWithLogAnnotation() {

    }

    @Before("methodWithLogAnnotation()")
    public void log(JoinPoint joinPoint) throws Throwable {
        if (BuildConfig.DEBUG) {
            Log.i(getLogTag(joinPoint),joinPoint.getSignature().toString());
        }
    }
    private String getLogTag(JoinPoint joinPoint){
        MethodSignature methodSignature= (MethodSignature) joinPoint.getSignature();
        Method method=methodSignature.getMethod();
        NeedLog annotation = method.getAnnotation(NeedLog.class);
        return annotation.value();
    }

}

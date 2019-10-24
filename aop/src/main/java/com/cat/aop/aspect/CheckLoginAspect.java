package com.cat.aop.aspect;


import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.cat.aop.annotation.CheckLogin;
import com.cat.aop.login.LoginCheckProxy;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

@Aspect
public class CheckLoginAspect {



    private static final String POINTCUT_METHOD = "execution(@com.cat.aop.annotation.CheckLogin * *(..))";

    @Pointcut(POINTCUT_METHOD)
    public void methodWithCheckLoginAnnotation() {

    }


    @Around("methodWithCheckLoginAnnotation()")
    public void checkLogin(final ProceedingJoinPoint joinPoint) throws Throwable {
        Activity activity=null;
        Fragment fragment=null;
        boolean isActivity=true;
        Object target=joinPoint.getTarget();
        if(target instanceof FragmentActivity){
            isActivity=true;
            activity= (Activity) target;
        }else if(target instanceof Fragment){
            isActivity=false;
            fragment= (Fragment) target;
        }else{
            Object[] args = joinPoint.getArgs();
            if(args!=null&&args.length>0){
                for (Object parameter:args) {
                    if(parameter instanceof FragmentActivity){
                        isActivity=true;
                        activity= (Activity) target;
                        break;
                    }else if(parameter instanceof Fragment){
                        isActivity=false;
                        fragment= (Fragment) target;
                        break;
                    }
                }
            }
        }
        Context context=isActivity?activity:fragment.getActivity();
        if(LoginCheckProxy.getInstance().isLoggedIn(context)){
            joinPoint.proceed();
        }else{
            int requestCode=getRequestCode(joinPoint);
            if(isActivity){
                LoginCheckProxy.getInstance().onNotLoggedIn(activity,requestCode);
            }else{
                LoginCheckProxy.getInstance().onNotLoggedIn(fragment,requestCode);
            }

        }
    }

    private int getRequestCode(JoinPoint joinPoint){
        MethodSignature methodSignature= (MethodSignature) joinPoint.getSignature();
        Method method=methodSignature.getMethod();
        CheckLogin annotation = method.getAnnotation(CheckLogin.class);
        return annotation.value();
    }

}

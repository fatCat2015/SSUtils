package com.cat.aop.aspect;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.cat.aop.permission.IPermissionCheck;
import com.cat.aop.permission.IPermissionCheckResult;
import com.cat.aop.permission.PermissionCheck;
import com.cat.aop.permission.PermissionCheckResult;
import com.cat.aop.annotation.Permission;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

@Aspect
public class PermissionAspect {



    private static final String POINTCUT_METHOD = "execution(@com.cat.aop.annotation.Permission * *(..))";

    @Pointcut(POINTCUT_METHOD)
    public void methodWithPermissionAnnotated() {
    }

    @Around("methodWithPermissionAnnotated()")
    public void checkPermissions(final ProceedingJoinPoint joinPoint) throws Throwable {
        Permission permissionAnnotation=findPermissionAnnotation(joinPoint);
        if(permissionAnnotation==null||permissionAnnotation.value()==null||permissionAnnotation.value().length==0){
            joinPoint.proceed();
        }else{
            IPermissionCheck permissionCheck=new PermissionCheck();
            IPermissionCheckResult permissionCheckResult=new PermissionCheckResult(joinPoint);
            checkPermissions(joinPoint,permissionAnnotation,permissionCheck,permissionCheckResult);
        }
    }

    private Permission findPermissionAnnotation(ProceedingJoinPoint joinPoint){
        MethodSignature methodSignature= (MethodSignature) joinPoint.getSignature();
        Method method=methodSignature.getMethod();
        Permission annotation = method.getAnnotation(Permission.class);
        return annotation;
    }

    private void checkPermissions(ProceedingJoinPoint joinPoint,Permission permissionAnnotation,IPermissionCheck permissionCheck,IPermissionCheckResult permissionCheckResult){
        Object target = joinPoint.getTarget();
        if(target instanceof FragmentActivity){
            permissionCheck.checkPermissions((FragmentActivity)target,permissionCheckResult,permissionAnnotation.value());
        }else if(target instanceof Fragment){
            permissionCheck.checkPermissions((Fragment)target,permissionCheckResult,permissionAnnotation.value());
        }else{
            Object[] args = joinPoint.getArgs();
            if(args==null||args.length==0){
                return;
            }
            for (Object parameter:args) {
                if(parameter instanceof FragmentActivity){
                    permissionCheck.checkPermissions((FragmentActivity)parameter,permissionCheckResult,permissionAnnotation.value());
                    break;
                }else if(parameter instanceof Fragment){
                    permissionCheck.checkPermissions((Fragment)parameter,permissionCheckResult,permissionAnnotation.value());
                    break;
                }
            }
        }
    }


}

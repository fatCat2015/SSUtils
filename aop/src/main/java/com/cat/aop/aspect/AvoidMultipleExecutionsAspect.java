package com.cat.aop.aspect;


import com.cat.aop.annotation.AvoidMultipleExecutions;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

@Aspect
public class AvoidMultipleExecutionsAspect {

    private static final String POINTCUT_METHOD = "execution(@com.cat.aop.annotation.AvoidMultipleExecutions * *(..))";

    @Pointcut(POINTCUT_METHOD)
    public void methodWithAvoidMultiClickAnnotation() {
    }

    private Signature lastSignature;
    private long lastExecutionTime;

    @Around("methodWithAvoidMultiClickAnnotation()")
    public void avoidMultiClick(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature signature=joinPoint.getSignature();
        long allowExecutionInterval=getAllowExecutionInterval(joinPoint);
        if (lastSignature==signature) {
            if (System.currentTimeMillis()-lastExecutionTime>= allowExecutionInterval) {
                joinPoint.proceed();
                lastExecutionTime=System.currentTimeMillis();
            }
        }else{
            joinPoint.proceed();
            lastExecutionTime=System.currentTimeMillis();
        }
        this.lastSignature=signature;
    }

    private long getAllowExecutionInterval(ProceedingJoinPoint joinPoint){
        MethodSignature methodSignature= (MethodSignature) joinPoint.getSignature();
        Method method=methodSignature.getMethod();
        AvoidMultipleExecutions annotation = method.getAnnotation(AvoidMultipleExecutions.class);
        return annotation.value();
    }
}

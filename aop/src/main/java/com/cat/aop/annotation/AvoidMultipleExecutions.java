package com.cat.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AvoidMultipleExecutions {
    /**
     * 方法重复执行的最小时间间隔 (ms) 默认1000ms
     * @return
     */
    long value() default 1000;
}

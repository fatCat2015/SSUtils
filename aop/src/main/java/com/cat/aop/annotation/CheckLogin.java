package com.cat.aop.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CheckLogin {
    /**
     *
     * @return 0 那么使用startActivity启动登录页面  非0使用startActivityForResult启动登录页面
     */
    int value() default 0;
}

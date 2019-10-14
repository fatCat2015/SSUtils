package com.cat.aop.annotation;


import com.cat.aop.permission.annotation.OnPermissionDenied;
import com.cat.aop.permission.annotation.OnPermissionDeniedWithNeverAskAgain;
import com.cat.aop.permission.annotation.OnPermissionGranted;
import com.cat.aop.permission.annotation.OnPermissionSettings;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Permission {
    /**
     * 要检查的权限String[]
     * @return
     */
    String[] value();

    /**
     * {@link OnPermissionGranted}
     * {@link OnPermissionDenied}
     * {@link OnPermissionDeniedWithNeverAskAgain}
     * {@link OnPermissionSettings}
     *
     *  权限检查结果的分发用到了反射 所以默认是false,关闭的
     * @return true表示 分发权限检查结果: 定义的上述注解的方法会根据权限检查结果相应的执行
     */
    boolean dispatchCheckResult() default false;
}

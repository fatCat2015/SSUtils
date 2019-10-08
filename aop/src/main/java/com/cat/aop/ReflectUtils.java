package com.cat.aop;



import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectUtils {

    public static void executeMethodWithAnnotation(Object target, Class annotationClz){
        if(target==null){
            return ;
        }
        Method[] methods = target.getClass().getDeclaredMethods();
        for (Method method :methods) {
            method.setAccessible(true);
            Annotation annotation = method.getAnnotation(annotationClz);
            if(annotation!=null){
                try {
                    method.invoke(target);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void  executeMethodWithAnnotation(Object target, Class annotationClz, Object...args){
        if(target==null){
            return ;
        }
        Method[] methods = target.getClass().getDeclaredMethods();
        for (Method method :methods) {
            method.setAccessible(true);
            Annotation annotation = method.getAnnotation(annotationClz);
            if(annotation!=null){
                try {
                    method.invoke(target,args);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}

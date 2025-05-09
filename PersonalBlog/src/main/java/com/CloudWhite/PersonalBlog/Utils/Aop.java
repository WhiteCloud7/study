package com.CloudWhite.PersonalBlog.Utils;

import com.CloudWhite.PersonalBlog.Utils.Annotation.NoAop;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class Aop {
    @Pointcut("execution(* com.CloudWhite.PersonalBlog.Service..*.*(..))")
    public void Pointcut(){};

    @Before("Pointcut()")
    public void before(JoinPoint joinPoint){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        if (method.isAnnotationPresent(NoAop.class)) {
            return;
        }
        System.out.println("方法被执行前!");
    }

    @After("Pointcut()")
    public void after(JoinPoint joinPoint){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        if (method.isAnnotationPresent(NoAop.class)) {
            return;
        }
        System.out.println("方法被执行后!执行了，"+method.getName());
    }
//
//    @AfterReturning("Pointcut()",returning="result")
//    public void AfterReturning(JoinPoint joinPoint, Object result){
//        Signature signature = joinPoint.getSignature();
//        String method = signature.getName();
//        System.out.println("执行完了"+method+"，返回了"+result);
//    }
//
//    @AfterThrowing(value = "Pointcut()",throwing = "ex")
//    public void AfterThrowing(Exception ex){
//        System.out.println("抛出了"+ex);
//    }
}

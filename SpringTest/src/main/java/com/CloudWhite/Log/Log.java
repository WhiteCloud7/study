package com.CloudWhite.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
@Aspect
@Component
public class Log{
    @Before("execution(* com.CloudWhite.Service.testService.*(..))")
    public void before(){
        System.out.println("方法被执行前!");
    }

    @After("execution(* com.CloudWhite.Service.testService.*(..))")
    public void afterReturning(){
        System.out.println("方法被执行后!");
    }

    @Around("execution(* com.CloudWhite.Service.testService.*(..))")//先around，然后通过类型如下jp.proceed()
    // ，执行before，然后继续around，最后执行after，当然也不宜不写这个，这个是为了区分多个方法
    public Object around(ProceedingJoinPoint jp) throws Throwable {
        System.out.println("环绕前");
        System.out.println("签名:"+jp.getSignature());//执行方法的返回类型、完整方法路径名
        Object proceed = jp.proceed();//执行目标方法proceed的返回值，有这个才会执行before
        System.out.println("环绕后");
        System.out.println(proceed);
        return proceed;
    }
}

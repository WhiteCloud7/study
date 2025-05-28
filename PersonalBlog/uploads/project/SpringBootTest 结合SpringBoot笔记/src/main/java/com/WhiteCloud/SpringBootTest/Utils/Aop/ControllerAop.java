package com.WhiteCloud.SpringBootTest.Utils.Aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Aspect
@Component
public class ControllerAop {
    @Pointcut("execution(* com.WhiteCloud.SpringBootTest.Controller..*.*(..))")
    public void Pointcut(){};

    @Before("Pointcut()")
    public void Before(){
        System.out.println("执行前");
    }

    @After("Pointcut()")
    public void After(){
        System.out.println("执行后");
    }

    @AfterReturning(value = "Pointcut()",returning = "result")
    public void AfterReturning(JoinPoint joinPoint, Object result){
        Signature signature = joinPoint.getSignature();
        String method = signature.getName();
        System.out.println("执行完了"+method+"，返回了"+result);
    }

    @AfterThrowing(value = "Pointcut()",throwing = "ex")
    public void AfterThrowing(Exception ex){
        System.out.println("抛出了"+ex);
    }
}

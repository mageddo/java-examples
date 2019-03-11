package com.huongdanjava.aspectj;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class ApplicationAspect {

    @Around("execution (* com.huongdanjava.aspectj.HelloWorld.*(..))")
    public void allMethods(ProceedingJoinPoint point) throws Throwable {
        System.out.println("before");
        point.proceed();
        System.out.println("after");
    }

}
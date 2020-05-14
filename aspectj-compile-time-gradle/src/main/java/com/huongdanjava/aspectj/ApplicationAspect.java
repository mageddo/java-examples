package com.huongdanjava.aspectj;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class ApplicationAspect {

  @Around("execution (* com.huongdanjava.aspectj.HelloWorld.*(..))")
  public Object allMethods(ProceedingJoinPoint point) throws Throwable {
    System.out.println("before");
    Object r = point.proceed(new Object[]{"World!!!"});
    System.out.println("after");
    return r + " (Proxied)";
  }

}

package com.mageddo.jdbi;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.transaction.Transactional;

import org.jdbi.v3.core.Jdbi;

import lombok.RequiredArgsConstructor;

@Interceptor
@Transactional
@RequiredArgsConstructor
public class JdbiInterceptor {

  private final Jdbi jdbi;

  @AroundInvoke
  public Object intercept(InvocationContext ctx) throws Exception {
    final var transactionalAnnotation = ctx.getMethod().getAnnotation(Transactional.class);
    System.out.println("something: " + ctx);
    return jdbi.inTransaction(handle -> {
      return ctx.proceed();
    });
  }
}

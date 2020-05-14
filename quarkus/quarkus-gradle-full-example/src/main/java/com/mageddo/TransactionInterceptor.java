package com.mageddo;

import javax.annotation.Priority;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.transactionv2.Transactional;
import javax.transactionv2.TransactionalInterceptor;

import lombok.extern.slf4j.Slf4j;

import static javax.interceptor.Interceptor.Priority.APPLICATION;

@Slf4j
@Interceptor
@Transactional
@Priority(APPLICATION)
public class TransactionInterceptor {

  private final TransactionalInterceptor transactionalInterceptor;

  public TransactionInterceptor() {
    this.transactionalInterceptor = new TransactionalInterceptor();
  }

  @AroundInvoke
  public Object intercept(InvocationContext ctx) throws Exception {
    return this.transactionalInterceptor.intercept(ctx);
  }
}

package com.mageddo.javax.naming;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.spi.InitialContextFactory;

public class InMemoryContextFactory implements InitialContextFactory {

  private static final Context CTX = new InMemoryContext();

  @Override
  public Context getInitialContext(Hashtable<?, ?> environment) {
    return CTX;
  }
}

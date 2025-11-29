package com.mageddo.javax.naming;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class JavaxNamingTest {

  static final String JNDI_NAME = "java:global/my/MyService";

  @BeforeAll
  static void beforeAll(){
    System.setProperty(
        Context.INITIAL_CONTEXT_FACTORY,
        InMemoryContextFactory.class.getName()
    );
  }

  @Test
  void mustRegisterAndLookup() throws Exception {
    register(new MyService());

    final var service = lookup();

    assertNotNull(service);
  }


  static void register(final MyService service) throws NamingException {
    final var ctx = new InitialContext();
    try {
      ctx.rebind(JNDI_NAME, service);
    } finally {
      ctx.close();
    }
  }

  public static MyService lookup() throws NamingException {
    return InitialContext.doLookup(JNDI_NAME);
  }

  static class MyService {

    private final String name;

    public MyService() {
      this.name = null;
    }

    MyService(String name) {
      this.name = name;
    }

    void sayHello() {
      System.out.println("Hello");
    }
  }
}

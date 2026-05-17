package com.mageddo.temporal.samplewallet.dataprovider;

import java.util.function.Consumer;
import java.util.function.Function;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class TransactionRunner {

  private final SessionFactory sessionFactory;

  public TransactionRunner(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  public <T> T required(Function<Session, T> handler) {
    try (var session = this.sessionFactory.openSession()) {
      var transaction = session.beginTransaction();
      try {
        var result = handler.apply(session);
        transaction.commit();
        return result;
      } catch (RuntimeException e) {
        transaction.rollback();
        throw e;
      }
    }
  }

  public void requiredVoid(Consumer<Session> handler) {
    this.required(session -> {
      handler.accept(session);
      return null;
    });
  }
}

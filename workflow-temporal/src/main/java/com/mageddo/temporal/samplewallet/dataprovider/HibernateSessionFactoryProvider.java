package com.mageddo.temporal.samplewallet.dataprovider;

import com.mageddo.temporal.samplewallet.domain.FinancialEventCandidate;
import com.mageddo.temporal.samplewallet.domain.Investment;
import com.mageddo.temporal.samplewallet.domain.Investor;
import com.mageddo.temporal.samplewallet.domain.Wallet;
import java.util.Map;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateSessionFactoryProvider {

  private final SessionFactory sessionFactory;

  public HibernateSessionFactoryProvider(Map<String, Object> properties) {
    var registry = new StandardServiceRegistryBuilder()
      .applySettings(properties)
      .build();
    this.sessionFactory = new MetadataSources(registry)
      .addAnnotatedClass(Investor.class)
      .addAnnotatedClass(Wallet.class)
      .addAnnotatedClass(Investment.class)
      .addAnnotatedClass(FinancialEventCandidate.class)
      .buildMetadata()
      .buildSessionFactory();
  }

  public SessionFactory get() {
    return this.sessionFactory;
  }

  public void close() {
    this.sessionFactory.close();
  }
}

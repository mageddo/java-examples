package com.mageddo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gruelbox.transactionoutbox.Dialect;
import com.gruelbox.transactionoutbox.Instantiator;
import com.gruelbox.transactionoutbox.Persistor;
import com.gruelbox.transactionoutbox.TransactionManager;
import com.gruelbox.transactionoutbox.TransactionOutbox;

import com.mageddo.templates.Fruits;
import com.zaxxer.hikari.HikariConfig;

import com.zaxxer.hikari.HikariDataSource;

import io.quarkus.runtime.StartupEvent;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.eclipse.microprofile.reactive.messaging.*;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

import javax.sql.DataSource;

import java.util.Properties;
import java.util.stream.Stream;

public class App {

  public DataSource createDataSource() {
    final var config = new HikariConfig();
    config.setJdbcUrl("jdbc:postgresql://localhost:5432/postgres");
    config.setUsername("elfreitas");
    config.setPassword("postgres");
    config.setMaximumPoolSize(5);
    config.setMinimumIdle(2);
    config.setConnectionTimeout(3000);
    return new HikariDataSource(config);
  }

  public TransactionOutbox transactionOutbox() {
    final var messageSender = createMessageSender();
    return this.transactionOutbox(this.createDataSource(), messageSender);
  }

  private MessageSender createMessageSender() {
    final var producer = createKafkaProducer();
    return new MessageSender(producer, new ObjectMapper());
  }

  private static KafkaProducer<String, byte[]> createKafkaProducer() {
    final var props = new Properties();
    props.put("bootstrap.servers", "localhost:9092");
    props.put("key.serializer", StringSerializer.class.getName());
    props.put("value.serializer", ByteArraySerializer.class.getName());
    final var producer = new KafkaProducer<String, byte[]>(props);
    return producer;
  }


  public TransactionOutbox transactionOutbox(DataSource dataSource, MessageSender messageSender) {
    return TransactionOutbox.builder()
        .transactionManager(TransactionManager.fromDataSource(dataSource))
        .persistor(Persistor.forDialect(Dialect.POSTGRESQL_9))
        .submitter((entry, localExecutor) -> {
          loca
        })
        .instantiator(Instantiator.using(clazz -> messageSender))
        .build();
  }

  public static void main(String[] args) {
    final var app = new App();
    final var transactionOutbox = app.transactionOutbox();
    transactionOutbox.schedule(MessageSender.class).send(Fruits);


  }
}

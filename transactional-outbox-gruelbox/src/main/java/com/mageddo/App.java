package com.mageddo;

import java.util.Properties;
import java.util.Set;

import javax.sql.DataSource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gruelbox.transactionoutbox.DefaultInvocationSerializer;
import com.gruelbox.transactionoutbox.DefaultPersistor;
import com.gruelbox.transactionoutbox.Dialect;
import com.gruelbox.transactionoutbox.Instantiator;
import com.gruelbox.transactionoutbox.Persistor;
import com.gruelbox.transactionoutbox.TransactionManager;
import com.gruelbox.transactionoutbox.TransactionOutbox;
import com.mageddo.templates.Fruits;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;

@Slf4j
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

  public TransactionOutbox transactionOutbox(TransactionManager transactionManager) {
    return this.transactionOutbox(transactionManager, this.producerRecordDelegate());
  }

  private MessageSender createMessageSender(
      KafkaProducer<String, byte[]> producer,
      TransactionManager transactionManager,
      TransactionOutbox transactionOutbox
  ) {
    return new MessageSender(
        producer,
        new ObjectMapper(),
        transactionOutbox,
        transactionManager
    );
  }

  private KafkaProducer<String, byte[]> createKafkaProducer() {
    final var props = new Properties();
    props.put("bootstrap.servers", "localhost:9092");
    props.put("key.serializer", StringSerializer.class.getName());
    props.put("value.serializer", ByteArraySerializer.class.getName());
    props.put(ProducerConfig.LINGER_MS_CONFIG, "2000");
    props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, "7000");
    props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, "10000");
    props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, "10000");

    final var producer = new KafkaProducer<String, byte[]>(props);
    return producer;
  }

  public ProducerRecordDelegate producerRecordDelegate() {
    return new ProducerRecordDelegate();
  }

  public TransactionOutbox transactionOutbox(
      TransactionManager transactionManager, ProducerRecordDelegate delegate
  ) {
    return TransactionOutbox.builder()
        .transactionManager(transactionManager)
        .instantiator(Instantiator.using(clazz -> delegate))
        .persistor(DefaultPersistor.builder()
            .dialect(Dialect.POSTGRESQL_9)
//            .tableName("GBOX_OUTBOX")
            .writeLockTimeoutSeconds(1)
//            .migrate(false)
            .serializer(DefaultInvocationSerializer.builder()
                .serializableTypes(Set.of(RecordVO.class))
                .build()
            )
            .build())
        .build();
  }

  public static void main(String[] args) throws Exception {
    final var app = new App();

    final var producer = app.createKafkaProducer();
    final var txManager = TransactionManager.fromDataSource(app.createDataSource());
    final var transactionOutbox = app.transactionOutbox(txManager);
    final var messageSender = app.createMessageSender(
        producer, txManager, transactionOutbox
    );

    log.info("will produce in 10 seconds");
    Thread.sleep(10000);

    log.info("sending...");
    messageSender.send(Fruits.apple());
    messageSender.send(Fruits.orange());
    messageSender.send(Fruits.grape());

    log.info("flushing....");
    transactionOutbox.flush();

    Thread.sleep(10000);
  }
}

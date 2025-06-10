package com.mageddo.transactional.outbox.tobby;

import java.time.Duration;
import java.util.Properties;

import javax.sql.DataSource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mageddo.tobby.Tobby;
import com.mageddo.tobby.internal.utils.Threads;
import com.mageddo.tobby.replicator.IdempotenceStrategy;
import com.mageddo.tobby.replicator.ReplicatorConfig;
import com.mageddo.tobby.replicator.Replicators;
import com.mageddo.transactional.outbox.tobby.templates.Fruits;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
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

  public Tobby tobby() {
    return Tobby.build(createDataSource());
  }

  public Replicators tobbyReplicator() {
    return Tobby.replicator(ReplicatorConfig.builder()
        .dataSource(this.createDataSource())
        .producer(this.tobbyKafkaProducer())
        .idempotenceStrategy(IdempotenceStrategy.BATCH_PARALLEL_UPDATE)
        .bufferSize(100)
        .fetchSize(100)
        .stopPredicate(replicatorContextVars -> true)
        .build()
    );
  }

  private MessageSender createMessageSender() {
    return new MessageSender(createKafkaProducer(), this.tobbyProducer(), new ObjectMapper());
  }

  private Producer<String, byte[]> tobbyProducer() {
    return this.tobby().kafkaProducer(new StringSerializer(), new ByteArraySerializer());
  }

  private KafkaProducer<String, byte[]> createKafkaProducer() {
    final var props = new Properties();
    props.put("bootstrap.servers", "localhost:9092");
    props.put("key.serializer", StringSerializer.class.getName());
    props.put("value.serializer", ByteArraySerializer.class.getName());
    final var producer = new KafkaProducer<String, byte[]>(props);
    return producer;
  }

  private KafkaProducer<byte[], byte[]> tobbyKafkaProducer() {
    final var props = new Properties();
    props.put("bootstrap.servers", "localhost:9092");
    props.put("key.serializer", ByteArraySerializer.class.getName());
    props.put("value.serializer", ByteArraySerializer.class.getName());
    final var producer = new KafkaProducer<byte[], byte[]>(props);
    return producer;
  }


  public static void main(String[] args) {

    final var app = new App();

    final var messageSender = app.createMessageSender();

    messageSender.send(Fruits.apple());
    messageSender.send(Fruits.orange());
    messageSender.send(Fruits.grape());

    app.replicate();

  }

  private void replicate() {
    final var replicator = this.tobbyReplicator();
    while (true) {
      try {
        log.info("status=replicating");
        replicator.replicate();
        Threads.sleep(Duration.ofMinutes(1));
      } catch (Exception e) {
        log.warn("status=replicationFailed, msg={}", e.getMessage());
      }
    }
  }
}

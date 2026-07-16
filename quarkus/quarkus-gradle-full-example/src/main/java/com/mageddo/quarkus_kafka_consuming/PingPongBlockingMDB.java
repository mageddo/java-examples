package com.mageddo.quarkus_kafka_consuming;

import java.time.temporal.ChronoUnit;

import io.smallrye.reactive.messaging.annotations.Blocking;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import io.quarkus.scheduler.Scheduled;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class PingPongBlockingMDB {

  private final Producer<String, byte[]> producer;

  @Retry(
      delay = 5,
      delayUnit = ChronoUnit.SECONDS,
      maxRetries = Integer.MAX_VALUE,
      retryOn = Exception.class
  )
  @Blocking
  @Incoming("sys.ping-pong.blocking")
  public void consume(ConsumerRecords<String, byte[]> records) {
    log.info("status=pong, records={}", records.count());
  }

  @Scheduled(every = "PT3S")
  public void ping() {
    this.producer.send(new ProducerRecord<>("sys.ping-pong.PingPongMDB", null));
  }
}

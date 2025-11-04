package com.mageddo.kafka;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.confluent.parallelconsumer.ParallelConsumerOptions;
import io.confluent.parallelconsumer.ParallelConsumerOptions.ProcessingOrder;
import io.confluent.parallelconsumer.ParallelStreamProcessor;

public class Main {

  private static final Logger log = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) throws Exception {
    final var producer = createProducer();
    produceRandomMessages(producer);

    final var parallelConsumer = ParallelStreamProcessor.createEosStreamProcessor(
        createOptions(producer)
    );
    parallelConsumer.subscribe(List.of("testing-topic"));
    parallelConsumer.poll(context -> {
      final var records = context.streamConsumerRecords().toList();
      final var stream = records.stream();
      final var map = stream.collect(Collectors.groupingBy(
          ConsumerRecord::key, LinkedHashMap::new, Collectors.toList())
      );
      log.info(String.format(
          "status=batchProcessed, size=%d, keys=%s",
          records.size(),
          map.keySet()
      ));
    });
    Thread
        .currentThread()
        .sleep(10000);
  }

  private static ParallelConsumerOptions<String, byte[]> createOptions(
      KafkaProducer<String, byte[]> producer
  ) {
    return ParallelConsumerOptions
        .<String, byte[]>builder()
        .consumer(createConsumer())
        .producer(producer)
        .ordering(ProcessingOrder.KEY)
        .maxConcurrency(2)
        .batchSize(5)
        .build();
  }

  private static void produceRandomMessages(KafkaProducer<String, byte[]> producer) {
    for (int i = 0; i < 100; i++) {
      producer.send(new ProducerRecord<>(
          "testing-topic", String.valueOf(i % 2), (i + "").getBytes()
      ));
    }
    producer.flush();
  }

  private static KafkaProducer<String, byte[]> createProducer() {
    final var props = Map.<String, Object>of(
        "bootstrap.servers", "localhost:9092",
        "key.serializer", "org.apache.kafka.common.serialization.StringSerializer",
        "value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer"
    );
    final var producer = new KafkaProducer<String, byte[]>(props);
    return producer;
  }

  private static KafkaConsumer<String, byte[]> createConsumer() {
    return new KafkaConsumer<String, byte[]>(
        Map.of(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092",
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization" +
                ".StringDeserializer",
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common" +
                ".serialization.ByteArrayDeserializer",
            ConsumerConfig.GROUP_ID_CONFIG, "test-group",
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest",
            ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false,
//            ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 60_000,
            ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 20_000, // quanto tempo espera antes de
            // rebalancear
            ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 3_600_000
        )
    );
  }


}

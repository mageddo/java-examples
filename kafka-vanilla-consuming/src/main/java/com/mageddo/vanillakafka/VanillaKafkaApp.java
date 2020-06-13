package com.mageddo.vanillakafka;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringSerializer;

public class VanillaKafkaApp {
  public static void main(String[] args) {

    final var producerProps = new Properties();
    producerProps.setProperty("bootstrap.servers", "localhost:9092");
    producerProps.setProperty("key.serializer", StringSerializer.class.getName());
    producerProps.setProperty("value.serializer", StringSerializer.class.getName());
    producerProps.setProperty("request.timeout.ms", "1000");
    final var producer = new KafkaProducer<String, String>(producerProps);
    producer.send(new ProducerRecord<>("stock_prices_changed", "PAGS", "30.80"));

    Properties props = new Properties();
    props.setProperty("bootstrap.servers", "localhost:9092");
    props.setProperty("group.id", "stocks");
    props.setProperty("enable.auto.commit", "false");
    props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
    consumer.subscribe(Arrays.asList("stock_prices_changed"));

    while (true) {

      ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
      for (ConsumerRecord<String, String> record : records) {
        System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
        final var topicPartition = new TopicPartition(record.topic(), record.partition());
        final var offsetAndMetadata = new OffsetAndMetadata(record.offset() + 1);
        consumer.commitSync(Map.of(topicPartition, offsetAndMetadata));
      }
    }

  }
}

package com.mageddo.mdb;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import com.mageddo.kafka.client.ConsumerConfig;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.eclipse.microprofile.config.ConfigProvider;

import static org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;

@ApplicationScoped
public class ConsumerConfigFactory {
  @Produces
  public ConsumerConfig<String, byte[]> consumerConfig() {
    return ConsumerConfig
        .<String, byte[]>builder()
        .prop(MAX_POLL_INTERVAL_MS_CONFIG, (int) Duration.ofMinutes(20).toMillis())
        .prop(GROUP_ID_CONFIG, getProperty("kafka.consumer.group.id", String.class))
        .prop(BOOTSTRAP_SERVERS_CONFIG, getProperty("kafka.bootstrap.servers", String.class))
        .prop(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName())
        .prop(VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName())
        .build()
        ;
  }

  private String getProperty(String k, Class<String> clazz) {
    return ConfigProvider.getConfig().getValue(k, clazz);
  }

  @Produces
  public KafkaProducer<String, byte[]> kafkaProducer(){
    final var props = Map.<String, Object>of(
        BOOTSTRAP_SERVERS_CONFIG, "localhost:9092",
        KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName(),
        VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName()
    );
    return new KafkaProducer<>(props);
  }
}

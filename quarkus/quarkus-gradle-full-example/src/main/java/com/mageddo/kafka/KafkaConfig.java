package com.mageddo.kafka;

import java.time.Duration;
import java.util.Map;

import javax.enterprise.inject.Produces;

import com.mageddo.kafka.client.ConsumerConfig;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.arc.DefaultBean;

import static org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;

public class KafkaConfig {

  @Produces
  @DefaultBean
  public Producer<String, byte[]> producer(
      @ConfigProperty(name = "kafka.bootstrap.servers") String bootstrapServer
  ) {
    return new KafkaProducer<>(Map.of(
        CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer,
        KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName(),
        VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName()
    ));
  }

  @Produces
  @DefaultBean
  public ConsumerConfig<String, byte[]> consumers(
      @ConfigProperty(name = "kafka.bootstrap.servers") String bootstrapServer
  ) {
    return ConsumerConfig
        .<String, byte[]>builder()
        .prop(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer)
        .prop(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName())
        .prop(VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName())
        .prop(SESSION_TIMEOUT_MS_CONFIG, 30000)
        .prop(MAX_POLL_INTERVAL_MS_CONFIG, (int) Duration.ofMinutes(20).toMillis())
        .prop(GROUP_ID_CONFIG, getProperty("kafka.consumer.group.id", String.class))
        .prop(BOOTSTRAP_SERVERS_CONFIG, getProperty("kafka.bootstrap.servers", String.class))
        .build()
        ;
  }

  private String getProperty(String k, Class<String> clazz) {
    return ConfigProvider.getConfig().getValue(k, clazz);
  }
}

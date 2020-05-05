package com.mageddo.kafka;

import java.time.Duration;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

import javax.enterprise.context.ApplicationScoped;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class ConsumerFactory {

  public  <K, V> Consumer<K, V> create(Map<String, Object> props, String... topics) {
    final var defaultProps = new LinkedHashMap<String, Object>();
    defaultProps.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    defaultProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, String.valueOf(false));
    defaultProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    defaultProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());
    defaultProps.putAll(props);
    final var consumer = new KafkaConsumer<K, V>(defaultProps);
    consumer.subscribe(Arrays.asList(topics));
    return consumer;
  }

  public <K,V> void poll(
      Consumer<K, V> consumer,
      BiConsumer<ConsumerRecords<K, V>, Exception> callback,
      Duration timeout,
      Duration interval
  ){
    while (true){
      try {
        final var records = consumer.poll(timeout);
        if(log.isTraceEnabled()){
          log.trace("status=polled, records={}", records.count());
        }
        callback.accept(records, null);
      } catch (Exception e){
        if(log.isTraceEnabled()){
          log.trace("status=poll-error", e);
        }
        callback.accept(null, e);
      }
      try {
        TimeUnit.MILLISECONDS.sleep(interval.toMillis());
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        break;
      }
    }
  }
}

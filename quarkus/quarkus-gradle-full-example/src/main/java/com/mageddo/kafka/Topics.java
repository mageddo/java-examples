package com.mageddo.kafka;

import java.time.Duration;
import java.util.HashMap;

import com.mageddo.kafka.client.BatchConsumeCallback;
import com.mageddo.kafka.client.ConsumeCallback;
import com.mageddo.kafka.client.ConsumerConfig;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static org.apache.commons.lang3.ObjectUtils.firstNonNull;
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;

@Getter
@AllArgsConstructor
public enum Topics {

  STOCK_PRICE(Topic.builder()
      .name("stock_changed_v2")
      .groupId("quarkus_gradle_stock_changed_v2")
      .maxTries(3)
      .maxInterval(Duration.ofSeconds(29))
      .consumers(1)
      .build()
  ),
  ;

  private final Topic topic;

  public ConsumerConfig<String, byte[]> toBatchConfig(BatchConsumeCallback<String, byte[]> callback) {
    return this.toConfig(callback, null);
  }

  public ConsumerConfig<String, byte[]> toConfig(ConsumeCallback<String, byte[]> callback) {
    return this.toConfig(null, callback);
  }

  public ConsumerConfig<String, byte[]> toConfig(
      BatchConsumeCallback<String, byte[]> batchCallback, ConsumeCallback<String, byte[]> callback
  ) {
    final var props = new HashMap<>(
        firstNonNull(this.topic.getProps(), new HashMap<>())
    );
    return ConsumerConfig
        .<String, byte[]>builder()
        .props(props)
        .topics(this.topic.getName())
        .consumers(this.topic.getConsumers())
        .prop(
            GROUP_ID_CONFIG, firstNonNull(props.get(GROUP_ID_CONFIG), this.topic.getGroupId())
        )
        .batchCallback(batchCallback)
        .callback(callback)
        .build();
  }
}

package com.mageddo.kafka;

import com.mageddo.kafka.client.Consumer;
import com.mageddo.kafka.client.ConsumerConfig;
import com.mageddo.kafka.client.ConsumerConfigDefault;
import com.mageddo.kafka.client.ConsumerStarter;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Singleton;

import java.util.stream.Collectors;

@Slf4j
@Singleton
public class KafkaConfig {

  private final ConsumerStarter<String, byte[]> consumerStarter;

  @Inject
  public KafkaConfig(ConsumerConfig<String, byte[]> config) {
    this.consumerStarter = new ConsumerStarter<>(config);
  }

  public void init(
      @Observes StartupEvent event,
      Instance<Consumer> consumers,
      DefaultRecoverStrategy recoverStrategy
  ) {
    final var consumersConfigs = consumers
        .stream()
        .map(it -> {
          final ConsumerConfigDefault.Builder<String, byte[]> config = ConsumerConfigDefault.builderOf(it.config());
          return (ConsumerConfig<String, byte[]>) config
              .recoverCallback(ctx -> {
                KafkaConsumingReporter.report(ctx);
                recoverStrategy.recover(
                    ctx,
                    String.format(
                        "%s_DLQ",
                        it.config()
                            .topics()
                            .stream()
                            .findFirst()
                            .orElseThrow()
                    )
                );
              })
              .build();
        })
        .collect(Collectors.toList());
    this.consumerStarter.startFromConfig(consumersConfigs);
  }

  public void shutdown(@Observes ShutdownEvent event) {
    this.consumerStarter.stop();
  }
}

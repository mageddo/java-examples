package com.mageddo.kafka;

import java.util.stream.Collectors;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.mageddo.kafka.client.Consumer;
import com.mageddo.kafka.client.ConsumerConfig;
import com.mageddo.kafka.client.ConsumerConfigDefault;
import com.mageddo.kafka.client.ConsumerStarter;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class QuarkusConsumerStarter {

  private final ConsumerStarter<String, byte[]> consumerStarter;

  @Inject
  public QuarkusConsumerStarter(ConsumerConfig<String, byte[]> config) {
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
                recoverStrategy.recover(
                    ctx,
                    String.format(
                        "%s_DLT",
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

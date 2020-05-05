package com.mageddo.kafka;

import java.time.Duration;

public interface ConsumingConfig<K, V> {

  /**
   * The callback which will be called after poll the message
   */
  ConsumeCallback<K, V> getCallback();

  /**
   * How long to wait the poll call
   */
  Duration getTimeout();

  /**
   * The interval between poll calls
   */
  Duration getInterval();

}

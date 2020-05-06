package com.mageddo.kafka;

import java.time.Duration;

public interface ConsumingConfig<K, V> {

  /**
   * The callback to be called after all tries be exhausted
   */
  RecoverCallback<K, V> getRecoverCallback();

  /**
   * The callback which will be called after poll the message
   */
  ConsumeCallback<K, V> getCallback();

  /**
   * The call which will be called after poll the messages in batch mode
   */
  BatchConsumeCallback<K, V> getBatchCallback();

  /**
   * How long to wait the poll call
   */
  Duration getTimeout();

  /**
   * The interval between poll calls
   */
  Duration getInterval();

  RetryStrategy getRetryStrategy();

}

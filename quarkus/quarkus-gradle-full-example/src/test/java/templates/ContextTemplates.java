package templates;


import com.mageddo.kafka.client.CallbackContext;
import com.mageddo.kafka.client.DefaultCallbackContext;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.MockConsumer;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ContextTemplates {
  public static <K, V> CallbackContext<K, V> build(ConsumerRecords<K, V> consumerRecords) {
    return DefaultCallbackContext
        .<K,V>builder()
        .consumer(new MockConsumer<>(OffsetResetStrategy.LATEST))
        .records(consumerRecords)
        .build()
        ;
  }
}

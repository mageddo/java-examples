package templates;

import com.mageddo.kafka.client.Context;
import com.mageddo.kafka.client.DefaultContext;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.MockConsumer;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ContextTemplates {
  public static <K, V> Context<K, V> build(ConsumerRecords<K, V> consumerRecords) {
    return DefaultContext
        .<K,V>builder()
        .consumer(new MockConsumer<>(OffsetResetStrategy.LATEST))
        .records(consumerRecords)
        .build()
        ;
  }
}

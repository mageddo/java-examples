package templates;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ConsumerRecordTemplates {

  public static <V> ConsumerRecord<String, V> build(V message) {
    return build(null, message);
  }

  public static <K, V> ConsumerRecord<K, V> build(K key, V message) {
    return new ConsumerRecord<>("", 0, 0, key, message);
  }
}

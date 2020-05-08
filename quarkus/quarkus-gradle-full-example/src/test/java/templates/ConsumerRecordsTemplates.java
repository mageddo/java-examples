package templates;

import lombok.experimental.UtilityClass;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class ConsumerRecordsTemplates {
  public static<K, V> ConsumerRecords<K, V> build(String topic, ConsumerRecord<K, V>... consumerRecords) {
    final Map<TopicPartition, List<ConsumerRecord<K, V>>> recordsMap = new HashMap<>();
    recordsMap.put(new TopicPartition(topic, 1), Arrays.asList(consumerRecords));
    return new ConsumerRecords<>(recordsMap);
  }
}

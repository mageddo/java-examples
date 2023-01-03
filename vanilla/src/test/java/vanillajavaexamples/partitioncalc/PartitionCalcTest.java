package vanillajavaexamples.partitioncalc;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static vanillajavaexamples.partitioncalc.PartitionCalc.calcPartition;

class PartitionCalcTest {

  @Test
  void mustCalcPartitionEqual() {

    final var partitions = 5;
    final var map = new HashMap<Integer, AtomicInteger>();
    final var samples = 10_000;

    // act
    for (int i = 0; i < samples; i++) {
      final var partition = calcPartition(UUID.randomUUID(), partitions);
      map
          .compute(partition, (k, v) -> {
            if (v == null) {
              return new AtomicInteger(1);
            }
            v.addAndGet(1);
            return v;
          })
      ;
    }

    // assert
    assertEquals(partitions, map.size(), String.valueOf(map.keySet()));

    map.forEach((k, v) -> {
      final var proportion = (double) v.get() / samples;
      assertTrue(
          proportion >= 0.19 && proportion <= 0.21,
          String.format("proportion=%.3f, k=%d, v=%d", proportion, k, v.get())
      );
    });

  }

}

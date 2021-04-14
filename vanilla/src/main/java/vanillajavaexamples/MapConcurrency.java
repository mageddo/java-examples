package vanillajavaexamples;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

public class MapConcurrency {

  /**
   * Using ConcurrentHashMap implementation combined with {@link Map#compute(Object, BiFunction)}
   * method, you will have a thread safe
   */
  public static int concurrentSumNumbers() throws InterruptedException {
    return sumNumbers(new ConcurrentHashMap<>());
  }

  public static int nonConcurrentSumNumbers() throws InterruptedException {
    return sumNumbers(new HashMap<>());
  }

  public static int sumNumbers(Map<String, Integer> map) throws InterruptedException {
    final var pool = Executors.newFixedThreadPool(100);
    final var points = map;

    final var usedKey = "Elvis";
    for (int i = 0; i < 200; i++) {
      pool.submit(() -> {
        points.compute(usedKey, (k, v) -> {
          System.out.printf(
              "entrei, t=%s, v=%s, points=%s%n",
              Thread.currentThread().getName(), v, points
          );
          randomSleep(0, 50);

          System.out.printf(
              "sai, t=%s%n%n",
              Thread.currentThread().getName()
          );

          if (v == null) {
            // primeira vez vem aqui
            return 1;
          }
          // outras vezes vem aqui
          return v + 1;
        });
      });
    }
    pool.shutdown();
    pool.awaitTermination(30, TimeUnit.SECONDS);
    System.out.printf("map final=%s", points);
    return points.get(usedKey);
  }

  private static void randomSleep(int from, int to) {
    try {
      Thread.sleep((long) (Math.random() * to + from));
    } catch (InterruptedException e) {
    }
  }
}

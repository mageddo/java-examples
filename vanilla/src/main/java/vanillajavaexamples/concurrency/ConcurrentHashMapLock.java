package vanillajavaexamples.concurrency;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.security.SecureRandom;
import java.time.LocalTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ConcurrentHashMapLock {

  private final ConcurrentHashMap<Long, Boolean> store = new ConcurrentHashMap<>();

  public static void main(String[] args) throws Exception {
    new ConcurrentHashMapLock().lockScenario();

  }

  void lockScenario() throws Exception {
    final var poolSize = 20;
    final var pool = createPool(poolSize);
    try {
      for (int i = 0; i < poolSize; i++) {
        pool.submit(this::doStuff);
      }
    } finally {
      pool.shutdown();
      final var allExecuted = pool.awaitTermination(20, TimeUnit.SECONDS);
      log("status=done, allExecuted=%s", allExecuted);
    }
    System.out.println("------------------------");
    System.out.println(threadDump());
  }

  static ExecutorService createPool(int poolSize) {
    return Executors.newFixedThreadPool(
        poolSize,
        r -> {
          final var thread = new Thread(r);
          thread.setDaemon(true);
          return thread;
        });
  }

  public void doStuff() {

    final var key = System.nanoTime();
    final var r = new SecureRandom();

    this.store.compute(key, (k, v) -> {

      log("status=computing, key=%s", key);

      sleep(10_000);

      if (r.nextBoolean()) {
        log("status=clearing, key=%s", key);
        this.store.clear();
      }

      if (isEmpty(v)) {
        log("status=computed, key=%s", key);
        return r.nextBoolean();
      }

      return v;
    });

  }

  private void log(String s, Object... o) {
    System.out.printf(
        "%s %s %s %n",
        LocalTime.now(), Thread.currentThread().getName(), String.format(s, o)
    );
  }

  private static void sleep(int millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  static boolean isEmpty(Boolean v) {
    return v == null;
  }

  private static String threadDump() {
    return threadDump(true, true);
  }
  private static String threadDump(boolean lockedMonitors, boolean lockedSynchronizers) {
    StringBuffer threadDump = new StringBuffer(System.lineSeparator());
    ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
    for(ThreadInfo threadInfo : threadMXBean.dumpAllThreads(lockedMonitors, lockedSynchronizers)) {
      threadDump.append(threadInfo.toString());
    }
    return threadDump.toString();
  }

}

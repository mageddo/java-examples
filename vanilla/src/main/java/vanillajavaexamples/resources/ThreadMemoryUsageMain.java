package vanillajavaexamples.resources;

import java.util.LinkedList;
import java.util.List;

/**
 * To create 30k active threads java process growth to 1.3G which means 40k of memory to create
 * and keep running every thread, the heap stats didn't grow that much which means most memory
 * allocated to create a thread is done outside the heap (jvm and OS stuff to create a thread,
 * things that won't go to the heap).
 *
 * stats (kb)
 * free memory: 32,326
 * allocated memory: 53,248
 * max memory: 153,600
 * total free memory: 132,678
 * process memory: 121,273
 *
 * $ ./gradlew build shadowJar -x check && docker-compose up thread-memory-usage --build
 */
public class ThreadMemoryUsageMain {

  static final Thread[] threads = new Thread[2_001];
  static final List<byte[]> data = new LinkedList<>();

  public static void main(String[] args) throws Exception {

    System.out.printf("started with pid=%d%n", ProcessHandle.current().pid());
    System.out.println("> Before threads allocating");
    dumpSystemStats();

    System.out.println("> Allocating threads");
    for (int i = 0; i < threads.length; i++) {

      final var thread = threads[i] = new Thread(() -> {
        while (true) {
          randomSleep();
        }
      });
      thread.setDaemon(true);
      thread.start();

      if(i % 1000 == 0){
        System.out.println("> Threads: " + (i + 1));
        dumpSystemStats();
      }

    }
    System.out.println("> After threads allocated");

    for (int i = 0; i < 100; i++) {
      Thread.sleep(5_000);
      dumpSystemStats();
//      allocateRam();
    }
  }

  private static void dumpSystemStats() {
    System.out.println(JVMStatsUtils.dumpStats());
  }

  private static void allocateRam() {
    final var mb = 50;
    final var b = new byte[mb * 1024 * 1024];
    for (int i = 0; i < b.length; i++) {
      b[i] = (byte) System.nanoTime();
    }
    data.add(b);
    System.out.printf("> Allocated %dmb%n", mb);
  }

  private static void randomSleep() {
    try {
      Thread.sleep((long) (Math.random() * 1000));
    } catch (InterruptedException e) {
      throw new RuntimeException();
    }
  }
}

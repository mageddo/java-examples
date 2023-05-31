package vanillajavaexamples.resources;

import java.util.LinkedList;
import java.util.List;

/**
 *
 *
 * $ ./gradlew build shadowJar -x check && docker-compose up thread-memory-usage --build
 */
public class ThreadMemoryUsageMain {

  static final Thread[] threads = new Thread[30_000];
  static final List<byte[]> data = new LinkedList<>();

  public static void main(String[] args) throws Exception {

    System.out.printf("started with pid=%d%n", ProcessHandle.current().pid());
    System.out.println("> Before threads allocating");
    System.out.println(MemoryUtils.dumpMemory());

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
        System.out.println(MemoryUtils.dumpMemory());
      }

    }
    System.out.println("> After threads allocated");

    while (true){
      Thread.sleep(5_000);
      System.out.println(MemoryUtils.dumpMemory());
      allocateRam();
    }
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

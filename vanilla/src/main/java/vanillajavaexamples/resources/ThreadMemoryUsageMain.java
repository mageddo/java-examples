package vanillajavaexamples.resources;

/**
 * Avg of 20kb per thread created, 10k threads = 200mb allocated
 *
 *  $ ./gradlew build -x check && docker-compose up thread-memory-usage --build
 */
public class ThreadMemoryUsageMain {

  static final Thread[] threads = new Thread[50_000];

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

      System.out.printf("> Started %d %n", i);

    }
    System.out.println("> After threads allocated");

    while (true){
      Thread.sleep(5_000);
      System.out.println(MemoryUtils.dumpMemory());
    }
  }

  private static void randomSleep() {
    try {
      Thread.sleep((long) (Math.random() * 1000));
    } catch (InterruptedException e) {
      throw new RuntimeException();
    }
  }
}

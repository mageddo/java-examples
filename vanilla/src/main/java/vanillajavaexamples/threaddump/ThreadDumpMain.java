package vanillajavaexamples.threaddump;

public class ThreadDumpMain {
  public static void main(String[] args) {

    System.out.println("Started!");
    new Thread(() -> {
      while (true){
        sleeperJob();
      }
    }, "the sleeper").start();

    while (true) {
      calcTime();
    }

  }

  private static void calcTime() {
    final var x = System.currentTimeMillis() + 1;
    if (x % 10000 == 0) {
      System.out.println(x);
    }
  }

  private static void sleeperJob() {
    try {
      Thread.sleep(5_000);
    } catch (InterruptedException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }
}

package vanillajavaexamples.threads_sigterm;

public class Main {
  public static void main(String[] args) {

    {
      final var thread = new Thread(() -> {
        sleep(3_000);
      });
      thread.setDaemon(false); // false by default, set it to true and the program will exit
      // instantly
      thread.start();
    }

    {
      final var daemonThread = new Thread(() -> {
        sleep(5_000);
      });
      daemonThread.setDaemon(true);
      daemonThread.start();
    }

    System.out.println("All done exiting....");
  }

  private static void sleep(final int millis)  {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}

package vanillajavaexamples.sigint;

import sun.misc.Signal;


public class Main {
  public static void main(String[] args) throws InterruptedException {

    final var sigName = "HUP"; // use kill -l to list available
    Signal s = new Signal(sigName);

    Signal.handle(s, sig -> {
      System.out.printf("name=%s, number=%d%n", sig.getName(), sig.getNumber());
    });

    final var pid = ProcessHandle.current().pid();
    System.out.println("running with pid = " + pid);

    Thread.sleep(100000000);
  }
}

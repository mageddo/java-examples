package vanillajavaexamples;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ThreadInterruptionTest {

  @Test
  void mustInterruptThread() throws InterruptedException {

    final var t = new Thread(() -> {
      while (true) {
        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
          assertFalse(Thread.currentThread().isInterrupted());
          break;
        }
      }
    });
    t.start();
    Thread.sleep(500);

    // act
    assertFalse(t.isInterrupted());
    assertTrue(t.isAlive());
    t.interrupt();

    // assert
    assertTrue(t.isInterrupted());
    assertTrue(t.isAlive());

    t.join();

    assertFalse(t.isInterrupted());
    assertFalse(t.isAlive());


  }

}

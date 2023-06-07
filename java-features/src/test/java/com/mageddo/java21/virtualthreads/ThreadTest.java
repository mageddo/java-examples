package com.mageddo.java21.virtualthreads;

import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @see https://medium.com/javarevisited/how-to-use-java-19-virtual-threads-c16a32bad5f7
 */
class ThreadTest {

  @Test
  void virtualThreadsDontHaveANameByDefault() throws Exception {

    // arrange
    final var executor = Executors.newVirtualThreadPerTaskExecutor();

    // act
    final var text = executor
        .submit(() -> Thread.currentThread().getName() + "|" + Thread.currentThread().threadId())
        .get();
    executor.close();

    // assert
    assertTrue(text.startsWith("|"), text);
    assertTrue(text.matches("\\|\\d+"), text);
  }

  @Test
  void virtualThreadsCanHaveAName() throws Exception {
    // arrange
    final var threadName = new AtomicReference<String>();
    final var executor = Thread
        .ofVirtual()
        .name("virtual thread x")
        .start(() -> threadName.set(Thread.currentThread().getName()));

    // act
    executor.join();

    // assert
    assertEquals("virtual thread x", threadName.get());
  }

  @Test
  void virtualThreadsHaveAFlagToIdentifyThem() throws Exception {
    // arrange
    final var thread = Thread
        .ofVirtual()
        .start(() -> System.out.println("hi!"));

    // act
    thread.join();

    // assert
    assertTrue(thread.isVirtual());
  }
}

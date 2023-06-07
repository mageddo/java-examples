package com.mageddo.java21.virtualthreads;

import java.util.concurrent.Executors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadMain {
  public static void main(String[] args) throws Exception {

    // way 1
    {
      final var thread = Thread
          .ofVirtual()
          .start(() -> log.info("threadId={} bla", Thread.currentThread().getName()))
          ;
      thread.join();
    }

    // way 2
    {
      final var executor = Executors.newVirtualThreadPerTaskExecutor();
      executor.submit(() -> log.info("hi from virtual thread pool!"));
      executor.close();
    }

    // way 3
    {
      final var thread = Thread.startVirtualThread(() -> log.info("Quick virtual thread start"));
      thread.join();
    }

    log.info("status=all done!");
  }
}

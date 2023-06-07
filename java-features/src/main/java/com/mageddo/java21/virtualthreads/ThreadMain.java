package com.mageddo.java21.virtualthreads;

import java.util.concurrent.Executors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadMain {
  public static void main(String[] args) throws Exception {

    // way 1
    final var thread = Thread
        .ofVirtual()
        .start(() -> System.out.println("bla"))
        ;
    thread.join();

    // way 2
    final var executor = Executors.newVirtualThreadPerTaskExecutor();
    executor.submit(() -> System.out.println("hi from virtual thread pool!"));
    executor.close();

    log.info("status=all done!");
  }
}

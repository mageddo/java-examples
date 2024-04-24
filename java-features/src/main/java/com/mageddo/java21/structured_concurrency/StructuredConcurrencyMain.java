package com.mageddo.java21.structured_concurrency;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;
import java.util.function.Supplier;

import com.mageddo.java21.Response;

public class StructuredConcurrencyMain {
  public static void main(String[] args) throws ExecutionException, InterruptedException {
    System.out.println(handle());
  }

  static Response handle() throws ExecutionException, InterruptedException {
    try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
      Supplier<String>  user  = scope.fork(() -> findUser());
      Supplier<Integer> order = scope.fork(() -> fetchOrder());

      scope.join()            // Join both subtasks
          .throwIfFailed();  // ... and propagate errors

      // Here, both subtasks have succeeded, so compose their results
      return new Response(user.get(), order.get());
    }
  }

  static Integer fetchOrder() {
    return 46114;
  }

  static String findUser() {
    return "Jose";
  }
}

package vanillajavaexamples.caching.guava;

import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;

public class Main {

  public static void main(String[] args) {
    final var cache = CacheBuilder
        .newBuilder()
        .expireAfterWrite(5, TimeUnit.MINUTES)
        .maximumSize(100)
        .build();


  }
}

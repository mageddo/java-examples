package com.mageddo.httpclient;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.protocol.HttpContext;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConnectionTerminatorInterceptor implements HttpResponseInterceptor {

  private final Deque<Entry> queue = new ConcurrentLinkedDeque<>();
  private final ScheduledExecutorService pool = Executors.newScheduledThreadPool(1, r -> {
    final var thread = Executors.defaultThreadFactory().newThread(r);
    thread.setDaemon(true);
    return thread;
  });

  public ConnectionTerminatorInterceptor() {
    this.pool.schedule(this::cleanupRoutine, 1, TimeUnit.SECONDS);
  }

  @Override
  public void process(HttpResponse response, HttpContext context) {
    this.queue.add(Entry.of(response, Duration.ofSeconds(1)));
  }

  private void cleanupRoutine() {
    try {
      int closedConnections = 0;
      for (; ; ) {

        final var res = queue.peek();
        if (res == null || !res.hasExpired()) {
          break;
        }
        log.debug("status=closingConnection, conn={}", res);
        this.queue.poll();
        res
            .getResponse()
            .getEntity()
            .getContent()
            .close(); // << mata a connection
        closedConnections++;

      }
      log.debug("closedConnections={}", closedConnections);
//          ((CloseableHttpResponse)response).close(); // << mantem connection aberta e disponivel
    } catch (Exception e){
      log.error("status=fatalError, msg={}", e);
    }
  }

  @Value
  @RequiredArgsConstructor
  public static class Entry {

    private final LocalDateTime createdAt;
    private final HttpResponse response;
    private final Duration ttl;

    public HttpResponse getResponse() {
      return response;
    }

    public boolean hasExpired() {
      return Duration.between(this.createdAt, LocalDateTime.now()).compareTo(this.ttl) >= 0;
    }

    public static Entry of(HttpResponse entry, Duration ttl) {
      return new Entry(LocalDateTime.now(), entry, ttl);
    }
  }
}

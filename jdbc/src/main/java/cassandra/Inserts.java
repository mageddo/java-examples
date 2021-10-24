package cassandra;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PoolingOptions;
import com.datastax.driver.core.ResultSetFuture;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.QueryBuilder;

import org.apache.commons.lang3.time.StopWatch;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Inserts {

  private static final ExecutorService pool = Executors.newFixedThreadPool(10);

  public static void main(String[] args) {

    String serverIP = "127.0.0.1";
    String keyspace = "fm";
    Cluster cluster = Cluster.builder()
        .addContactPoints(serverIP)
        .withPoolingOptions(new PoolingOptions()
            .setMaxQueueSize(Integer.MAX_VALUE)
        )
        .build();

    final var session = cluster.connect(keyspace);


    while (true){
      final var stopWatch = StopWatch.createStarted();

      final List<Future<Object>> futures = new ArrayList<>();
      for (int i = 0; i < 5_000; i++) {
//        final var idx = String.valueOf(UUID.nameUUIDFromBytes(String.valueOf(i).getBytes()));
        final var id = i;
        final Future future = pool.submit(() -> {
          batchInsert(session, id);
        });
        futures.add(future);

      }
      Threads.flush(futures);
      log.info("time={}", stopWatch.getTime());
    }

//    session.close();
//    cluster.close();
  }

  // 1800
  private static void batchInsert(Session session, int idx) {
    final var batch = new ArrayList<ResultSetFuture>();
    for (int i = 0; i < 256; i++) {
      final var query = QueryBuilder.insertInto("FINANCIAL_ACCOUNT")
          .value("IDT_FINANCIAL_ACCOUNT", idx)
          .value(
              "COD_ACCOUNT",
              String.valueOf(UUID.nameUUIDFromBytes(String.valueOf(i).getBytes()))
          )
          .value("NUM_AMOUNT", Math.random());
      batch.add(session.executeAsync(query));
    }

    batch.forEach(it -> {
      try {
        it.get();
      } catch (InterruptedException | ExecutionException e) {
        throw new RuntimeException(e);
      }
    });
  }
}

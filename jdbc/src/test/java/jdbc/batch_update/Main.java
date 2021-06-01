package jdbc.batch_update;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.apache.commons.lang3.time.StopWatch;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import testing.UncheckedExecutionException;
import utils.UncheckedInterruptedException;

@Slf4j
public class Main {
  public static void main(String[] args) {

    final var config = new HikariConfig();
    config.setJdbcUrl("jdbc:postgresql://localhost:5432/db");
    config.setUsername("root");
    config.setPassword("root");
    config.setMaximumPoolSize(15);
    config.setAutoCommit(false);
    final var ds = new HikariDataSource(config);

    final var pool = Executors.newFixedThreadPool(20);

    final var accountIds = List.of(
        UUID.fromString("671782f5-650b-419d-aedb-20fc191b744e"),
        UUID.fromString("85426177-d8f7-4e22-8ba8-6686b122c379"),
        UUID.fromString("75fbce92-ffe3-48c1-a68d-b14e606994d1"),
        UUID.fromString("b51b9487-bded-49e4-8230-91b8063ca38e"),
        UUID.fromString("e5556ab7-a04e-491f-a175-2dba2c8893cc")
    );

    log.info("status=starting");

    final var hitsPerAccount = 10_000;
    final var hits = hitsPerAccount * accountIds.size();
    final var stopWatch = StopWatch.createStarted();


    final var futures = new ArrayList<Future<?>>();
    final var counter = new AtomicInteger();
    for (int i = 0; i < hitsPerAccount; i++) {
      futures.add(pool.submit(() -> {
        updateBalance(ds, counter, accountIds);
      }));
    }
    for (Future<?> future : futures) {
      try {
        future.get();
      } catch (InterruptedException e) {
        throw new UncheckedInterruptedException(e);
      } catch (ExecutionException e) {
        throw new UncheckedExecutionException(e);
      }
    }
    log.info(
        "status=done, accounts={}, hits={}, time={}",
        accountIds.size(), hits, stopWatch.toString()
    );

  }

  @SneakyThrows
  private static void updateBalance(
      HikariDataSource ds, AtomicInteger counter, List<UUID> accountIds
  ) {
    final var stopWatch = StopWatch.createStarted();
    final var connection = ds.getConnection();
    try (connection) {
      for (final var accountId : accountIds) {
        updateBalance(connection, accountId);
        counter.incrementAndGet();
      }
      connection.commit();
    }  catch (Exception e){
      connection.rollback();
      throw e;
    } finally {
      log.info("status=batchIsDone, counter={}, time={}", counter.get(), stopWatch.getTime());
    }
  }

  @SneakyThrows
  private static void updateBalance(Connection connection, UUID id) {
    final var stopWatch = StopWatch.createStarted();
    final var stm = connection.prepareStatement(
        "UPDATE FINANCIAL_ACCOUNT SET \n " +
            "  NUM_BALANCE = ? \n " +
            "WHERE IDT_FINANCIAL_ACCOUNT = ?"
    );
    try (stm) {
      stm.setBigDecimal(1, BigDecimal.valueOf(Math.random()));
      stm.setString(2, String.valueOf(id));
      stm.addBatch();
      stm.executeBatch();
    }
    log.info("status=balanceUpdated, id={}, time={}", id, stopWatch.getTime());
  }
}

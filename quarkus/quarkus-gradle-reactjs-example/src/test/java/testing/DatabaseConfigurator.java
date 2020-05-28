package testing;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Singleton;

import org.jdbi.v3.core.Jdbi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static testing.TestUtils.getResourceAsString;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class DatabaseConfigurator {

  private final Jdbi jdbi;

  public void execute(String sqlFile) {
    this.jdbi.useHandle(handle -> handle.execute(getResourceAsString(sqlFile)));
  }

  public void truncateTables() {
    log.info("status=schema-truncating");
    final StringBuilder sql = new StringBuilder().append("SELECT  \n")
        .append("	CONCAT(TABLE_SCHEMA, '.', TABLE_NAME) \n")
        .append("FROM INFORMATION_SCHEMA.TABLES \n")
        .append("WHERE TABLE_SCHEMA = CURRENT_SCHEMA() \n")
        .append("AND TABLE_NAME NOT IN (<tables>) \n")
        .append("ORDER BY TABLE_NAME \n");
    jdbi.useHandle(handle -> {
      final List<String> tables = handle
          .createQuery(sql.toString())
          .bindList("tables", skipTables())
          .map((rs, i) -> rs.getString(1))
          .list();

      handle.execute("SET CONSTRAINTS ALL DEFERRED");
      for (final String table : tables) {
        handle.execute("DELETE FROM " + table + " -- truncating table");
      }
      handle.execute("SET CONSTRAINTS ALL IMMEDIATE");
      log.info("status=schema-truncated");
      this.execute("/db/base-data.sql");
    });

  }

  Collection<String> skipTables() {
    return Stream.of("flyway_schema_history")
        .map(String::toLowerCase)
        .collect(Collectors.toList())
        ;
  }

}

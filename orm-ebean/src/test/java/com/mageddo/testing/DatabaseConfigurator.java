package com.mageddo.testing;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import io.ebean.Database;
import io.ebean.SqlRow;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class DatabaseConfigurator {

  private final Database database;

  public void execute(String sqlFile) {
    final var sql = this.readAsString(sqlFile);
    this.database.sqlUpdate(sql).execute();
  }

  public void truncate() {
    this.truncateTables();
  }

  void truncateTables() {
    final var tables = this.findTablesToTruncate();
    this.truncateTables(tables);
    this.executeBaseData();
    log.info("status=schemaTruncated, tables={}", tables);
  }

  void truncateTables(List<String> tables) {
    this.database.sqlUpdate("SET CONSTRAINTS ALL DEFERRED").execute();
    for (final String table : tables) {
      this.database.sqlUpdate("TRUNCATE " + table + " CASCADE").execute();
    }
    this.database.sqlUpdate("SET CONSTRAINTS ALL IMMEDIATE").execute();
  }

  void executeBaseData() {
    this.executeIfExists("/db/base-data-mageddo.sql");
    this.executeIfExists("/db/base-data-bookmarks.sql");
  }

  void executeIfExists(String sqlFile) {
    final var resource = DatabaseConfigurator.class.getResourceAsStream(sqlFile);
    if (resource == null) {
      log.debug("status=missingSqlFile, file={}", sqlFile);
      return;
    }

    try (resource) {
      final var sql = this.read(resource);
      if (sql.isBlank()) {
        return;
      }
      this.database.sqlUpdate(sql).execute();
    } catch (Exception e) {
      log.warn("status=failedExecuteBaseData, file={}", sqlFile, e);
    }
  }

  List<String> findTablesToTruncate() {
    final var skipped = this.skipTables().stream()
        .map(table -> "'" + table + "'")
        .reduce((left, right) -> left + "," + right)
        .orElse("'none'");

    final var sql = String.format("""
        SELECT
          format('%%I.%%I', TABLE_SCHEMA, TABLE_NAME)
        FROM INFORMATION_SCHEMA.TABLES
        WHERE TABLE_SCHEMA = CURRENT_SCHEMA()
        AND lower(TABLE_NAME) NOT IN (%s)
        AND TABLE_TYPE = 'BASE TABLE'
        ORDER BY TABLE_NAME
        """, skipped);

    return this.database.sqlQuery(sql).findList().stream()
        .map((SqlRow row) -> row.getString("format"))
        .toList();
  }

  String readAsString(String path) {
    try (final var resource = DatabaseConfigurator.class.getResourceAsStream(path)) {
      if (resource == null) {
        throw new IllegalArgumentException("file not found: " + path);
      }
      return this.read(resource);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  String read(InputStream stream) throws Exception {
    return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
  }

  Collection<String> skipTables() {
    return Stream.of(
        "state"
    ).toList();
  }
}

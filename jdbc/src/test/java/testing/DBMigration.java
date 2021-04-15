package testing;

import java.util.Properties;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.flywaydb.core.Flyway;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DBMigration {

  public static DataSource migrateEmbeddedPostgres() {
    final var props = new Properties();
   props.setProperty("driverClassName", "org.postgresql.Driver");
   props.setProperty("jdbcUrl", "jdbc:postgresql://localhost:5429/postgres?currentSchema=postgres");
   props.setProperty("username", "postgres");
   props.setProperty("password", "postgres");
   props.setProperty("locations", "classpath:/db/migration");
    final var dc = pgDataSource(10, props);
    migrate(
        dc.getJdbcUrl(),
        dc.getUsername(),
        dc.getPassword(),
        props.getProperty("locations")
    );
    return dc;
  }

  static HikariDataSource pgDataSource(int size, Properties props) {
    final var config = new HikariConfig();
    config.setDriverClassName(props.getProperty("driverClassName"));
    config.setMinimumIdle(size);
    config.setAutoCommit(false);
    config.setMaximumPoolSize(size);
    config.setJdbcUrl(props.getProperty("jdbcUrl"));
    config.setUsername(props.getProperty("username"));
    config.setPassword(props.getProperty("password"));
    return new HikariDataSource(config);
  }

  public static Flyway setup(String url, String user, String password, String... locations) {
    return Flyway.configure()
        .dataSource(url, user, password)
        .locations(locations)
        .load();
  }

  public static DataSource migrate(String url, String user, String password, String... locations) {
    return migrate(setup(url, user, password, locations));
  }

  public static DataSource migrate(Flyway flyway) {
    flyway.migrate();
    return flyway.getConfiguration()
        .getDataSource();
  }

  public static DataSource cleanAndMigrate(String url, String user, String password, String... locations) {
    return cleanAndMigrate(setup(url, user, password, locations));
  }

  public static DataSource cleanAndMigrate(Flyway flyway) {
    flyway.clean();
    flyway.migrate();
    return flyway.getConfiguration()
        .getDataSource();
  }
}

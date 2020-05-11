package ex03;

import org.h2.jdbcx.JdbcConnectionPool;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.StatementException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Ex03TransactionalMain {

  /*
   https://jdbi.org/#_transactions
   */
  public static void main(String[] args) {

    final var cp = JdbcConnectionPool.create("jdbc:h2:mem:test", "sa", "");
    final var jdbi = Jdbi.create(cp);

    jdbi.useHandle(handle -> {
      handle.execute("CREATE TABLE user (id INTEGER PRIMARY KEY, name VARCHAR)");
    });

    try {

      final var user = new User(1, "Alice");
      jdbi.useTransaction(handle -> {
        handle.execute("INSERT INTO user(id, name) VALUES (?, ?)", user.getId(), user.getName());
        handle.execute("INSERT INTO user(id, name) VALUES (?, ?)", user.getId(), user.getName());
      });
    } catch (StatementException e) {
      if (!e.getMessage().contains("PRIMARY KEY ON PUBLIC.USER")) {
        throw e;
      }
    }

    final var users = jdbi.withHandle(handle -> {
      return handle.createQuery("SELECT * FROM user ORDER BY name")
          .mapToBean(User.class)
          .list();
    });

    cp.dispose();
    System.out.println(users);
    assertTrue(users.isEmpty());
  }
}

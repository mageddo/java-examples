package ex02;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.StatementException;

import ex01.User;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Ex02Main {

  /*
   * Validates constraint exception
   */
  public static void main(String[] args) {
    Jdbi jdbi = Jdbi.create("jdbc:h2:mem:test"); // (H2 in-memory database)

    final var users = jdbi.withHandle(handle -> {
      handle.execute("CREATE TABLE user (id INTEGER PRIMARY KEY, name VARCHAR)");

      handle.execute("INSERT INTO user(id, name) VALUES (?, ?)", 1, "Alice");
      try {
        handle.execute("INSERT INTO user(id, name) VALUES (?, ?)", 1, "Angela");
      } catch (StatementException e){
        if(!e.getMessage().contains("PRIMARY KEY ON PUBLIC.USER")){
          throw e;
        }
      }
      return handle.createQuery("SELECT * FROM user ORDER BY name")
          .mapToBean(User.class)
          .list();
    });
    System.out.println(users);
    assertEquals("[{id=1, name='Alice'}]", users.toString());
  }
}

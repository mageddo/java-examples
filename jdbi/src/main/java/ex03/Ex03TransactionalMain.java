package ex03;

import java.util.List;

import org.h2.jdbcx.JdbcConnectionPool;
import org.jdbi.v3.core.Jdbi;

import ex02.DuplicatedUser;

public class Ex03TransactionalMain {

  /*
   * Validates constraint exception
   */

  public static void main(String[] args) {

    final var cp = JdbcConnectionPool.create("jdbc:h2:mem:test", "sa", "");
    final var jdbi = Jdbi.create(cp);

    jdbi.
    final var userService = new UserService(new UserDaoH2(jdbi));
    userService.createTable();
    try {
      userService.create(List.of(
          new User(1, "Alice"),
          new User(1, "Angela")
      ));
    } catch (DuplicatedUser e){}
    final var users = userService.find();

    System.out.println(users);
    cp.dispose();
  }
}

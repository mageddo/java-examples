package ex04;

import java.util.List;

import org.h2.jdbcx.JdbcConnectionPool;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import ex02.DuplicatedUser;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Ex04Main {

  public static void main(String[] args) {

    final var cp = JdbcConnectionPool.create("jdbc:h2:mem:test", "sa", "");
    final var jdbi = Jdbi
        .create(cp)
        .installPlugin(new SqlObjectPlugin());

    final var userDao = jdbi.onDemand(UserDao.class);
    final var userService = new UserService(userDao);
    userService.createTable();
    try {
      userService.create(List.of(
          new User(1, "Alice"),
          new User(1, "Angela")
      ));
    } catch (DuplicatedUser e){}
    final var users = userService.find();

    cp.dispose();
    System.out.println(users);
    assertTrue(users.isEmpty());
  }
}

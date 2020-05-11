package ex01;

import java.util.List;

import org.jdbi.v3.core.Jdbi;

public class Main {
  public static void main(String[] args) {
    Jdbi jdbi = Jdbi.create("jdbc:h2:mem:test"); // (H2 in-memory database)

    List<User> users = jdbi.withHandle(handle -> {
      handle.execute("CREATE TABLE user (id INTEGER PRIMARY KEY, name VARCHAR)");

      // Inline positional parameters
      handle.execute("INSERT INTO user(id, name) VALUES (?, ?)", 0, "Alice");

      // Positional parameters
      handle.createUpdate("INSERT INTO user(id, name) VALUES (?, ?)")
          .bind(0, 1) // 0-based parameter indexes
          .bind(1, "Bob")
          .execute();

      // Named parameters
      handle.createUpdate("INSERT INTO user(id, name) VALUES (:id, :name)")
          .bind("id", 2)
          .bind("name", "Clarice")
          .execute();

      // Named parameters from bean properties
      handle.createUpdate("INSERT INTO user(id, name) VALUES (:id, :name)")
          .bindBean(new User(3, "David"))
          .execute();

      // Easy mapping to any type
      return handle.createQuery("SELECT * FROM user ORDER BY name")
          .mapToBean(User.class)
          .list();
    });

    System.out.println(users);
  }
}

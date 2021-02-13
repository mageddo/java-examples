package ex05;

import java.util.List;

import org.jdbi.v3.core.Jdbi;

public class UserDaoH2 implements UserDao {

  private final Jdbi jdbi;

  public UserDaoH2(Jdbi jdbi) {
    this.jdbi = jdbi;
  }

  public void createTable() {
    this.jdbi.useHandle(handle -> {
      handle.execute("CREATE TABLE user (id INTEGER PRIMARY KEY, name VARCHAR)");
    });
  }

  public void create(User user) {
    this.jdbi.useHandle(h -> h
        .createUpdate("INSERT INTO USER (ID, NAME) VALUES (:id, :name)")
        .bindBean(user)
        .execute()
    );
  }

  @Override
  public List<User> find() {
    return jdbi.withHandle(handle -> {
      return handle.createQuery("SELECT * FROM user ORDER BY name")
          .mapToBean(User.class)
          .list();
    });
  }
}

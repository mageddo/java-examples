package ex06;

import java.util.List;

import org.jdbi.v3.core.Jdbi;

public class UserDaoH2 implements UserDao {

  private final Jdbi jdbi;

  public UserDaoH2(Jdbi jdbi) {
    this.jdbi = jdbi;
  }

  public void createTable() {
    this.jdbi.useHandle(handle -> {
      handle.execute("CREATE TABLE user (" +
          "idt_user INTEGER PRIMARY KEY, nam_user VARCHAR, " +
          "ind_gender VARCHAR)"
      );
    });
  }

  public void create(User user) {
    this.jdbi.useHandle(h -> h
        .createUpdate("INSERT INTO USER (IDT_USER, NAM_USER, IND_GENDER)" +
            " VALUES (:id, :name, :gender)")
        .bindBean(user)
        .execute()
    );
  }

  @Override
  public List<User> find() {
    return jdbi.withHandle(handle -> {
      return handle.createQuery("SELECT * FROM user ORDER BY NAM_USER")
//          .mapToBean(User.class)
          .mapTo(User.class)
//          .map(ConstructorMapper.of(User.class))
          .list();
    });
  }
}

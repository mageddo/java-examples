package ex04;

import java.util.List;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public class UserDaoH2 implements UserDao {

  private final Jdbi jdbi;

  public UserDaoH2(Jdbi jdbi) {
    this.jdbi = jdbi;
  }

  @SqlUpdate("CREATE TABLE user (id INTEGER PRIMARY KEY, name VARCHAR)")
  public void createTable(){
    jdbi.useHandle(handle -> {
      handle.execute("");
    });
  }

  public void create(User user){

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

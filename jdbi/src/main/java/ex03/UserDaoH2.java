package ex03;

import java.util.List;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.StatementException;

import ex02.DuplicatedUser;

public class UserDaoH2 implements UserDao {

  private final Jdbi jdbi;

  public UserDaoH2(Jdbi jdbi) {
    this.jdbi = jdbi;
  }

  public void createTable(){
    jdbi.useHandle(handle -> {
      handle.execute("CREATE TABLE user (id INTEGER PRIMARY KEY, name VARCHAR)");
    });
  }

  public void create(User user){
    try {
      jdbi.useHandle(handle -> {
        handle.execute("INSERT INTO user(id, name) VALUES (?, ?)", user.getId(), user.getName());
      });
    } catch (StatementException e){
      if(e.getMessage().contains("PRIMARY KEY ON PUBLIC.USER")){
        throw new DuplicatedUser(user.getName());
      }
      throw e;
    }
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

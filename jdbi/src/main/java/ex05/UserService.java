package ex05;

import java.util.List;

import org.jdbi.v3.core.statement.StatementException;

public class UserService {

  private final UserDao userDao;

  public UserService(UserDao userDao) {
    this.userDao = userDao;
  }

  public void createTable(){
    this.userDao.createTable();
  }

  public void create(List<User> users){
    users.forEach(this::create);
  }

  public void create(User user){
    try {
      this.userDao.create(user);
    } catch (StatementException e){
      if (!e.getMessage().contains("PRIMARY KEY ON PUBLIC.USER")) {
        throw e;
      }
    }
  }

  public List<User> find() {
    return this.userDao.find();
  }
}

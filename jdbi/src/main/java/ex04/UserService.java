package ex04;

import java.util.List;

import org.jdbi.v3.sqlobject.transaction.Transaction;

public class UserService {

  private final UserDao userDao;

  public UserService(UserDao userDao) {
    this.userDao = userDao;
  }

  public void createTable(){
    this.userDao.createTable();
  }

  @Transaction
  public void create(List<User> users){
    users.forEach(this::create);
  }

  public void create(User user){
    this.userDao.create(user);
  }

  public List<User> find() {
    return this.userDao.find();
  }
}

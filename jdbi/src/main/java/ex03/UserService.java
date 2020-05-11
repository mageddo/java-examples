package ex03;

import java.util.List;

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
    this.userDao.create(user);
  }

  public List<User> find() {
    return this.userDao.find();
  }
}

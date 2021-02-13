package ex06;

import java.util.List;

public interface UserDao {

  void createTable();

  void create(User user);

  List<User> find();
}

package ex04;

import java.util.List;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

@RegisterBeanMapper(User.class)
public interface UserDao {

  @SqlUpdate("CREATE TABLE user (id INTEGER PRIMARY KEY, name VARCHAR)")
  void createTable();

  @SqlUpdate("INSERT INTO user(id, name) VALUES (:user.id, :user.name)")
  void create(@BindBean("user") User user);

  @SqlQuery("SELECT * FROM user")
  List<User> find();
}

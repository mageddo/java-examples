package ex07;

import java.lang.reflect.Type;
import java.util.Optional;

import org.h2.jdbcx.JdbcConnectionPool;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.argument.Argument;
import org.jdbi.v3.core.argument.ArgumentFactory;
import org.jdbi.v3.core.config.ConfigRegistry;

import ex05.LombokBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Ex06Main {

  public static void main(String[] args) {

    final var cp = JdbcConnectionPool.create("jdbc:h2:mem:test", "sa", "");
    final var jdbi = Jdbi.create(cp);
    jdbi.getConfig(LombokBuilder.class).registerFreeBuilder(User.class);
    jdbi.registerArgument(new ArgumentFactory() {
      @Override
      public Optional<Argument> build(Type type, Object value, ConfigRegistry config) {
        if (value instanceof Json) {
          return Optional.of((pos, stm, ctx) -> {
            stm.setString(pos, ((Json) value).getValue());
          });
        }
        return Optional.empty();
      }
    })
    .registerColumnMapper(new JsonMapper());

    final var userService = new UserService(new UserDaoH2(jdbi));
    userService.createTable();
    userService.create(new User(1, "Alice", Gender.FEMALE, new Json("{\"age\": 12}")));
    final var users = userService.find();
    cp.dispose();
    System.out.println(users);
    assertEquals(users.size(), 1);
  }
}

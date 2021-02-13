package ex06;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

import org.h2.jdbcx.JdbcConnectionPool;
import org.jdbi.v3.core.Jdbi;

import ex02.DuplicatedUser;
import ex05.LombokBuilder;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Ex06Main {

  static <E extends Enum<?>> Optional<Class<?>> ifEnum(Type type) {
    if (type instanceof Class<?>) {
      final Class<?> cast = (Class<?>) type;
      if (Enum.class.isAssignableFrom(cast)) {
        return Optional.of((Class<?>) cast);
      }
    }
    return Optional.empty();
  }

  public static void main(String[] args) {

    final var cp = JdbcConnectionPool.create("jdbc:h2:mem:test", "sa", "");
    final var jdbi = Jdbi.create(cp);
    jdbi.getConfig(LombokBuilder.class).registerFreeBuilder(User.class);
//    jdbi.getConfig(Enums.class).setEnumStrategy(EnumStrategy.BY_NAME);
//    jdbi.registerColumnMapper(new EnumByNameMapperFactory());
//    jdbi.registerArgument(new ArgumentFactory() {
//      @Override
//      public Optional<Argument> build(Type type, Object value, ConfigRegistry config) {
////        final Optional<Class<?>> isEnum = ifEnum(type);
////        if(isEnum.isEmpty()){
////          return Optional.empty();
////        }
//        return Optional.of((position, statement, ctx) -> {
//          statement.setString(position, ((Enum<?>) value).name());
//        });
//      }
//    });
//    jdbi.registerArgument(EnumMapper.byName(Gender.class));

    final var userService = new UserService(new UserDaoH2(jdbi));
    userService.createTable();
    try {
      userService.create(List.of(
          new User(1, "Alice", Gender.FEMALE),
          new User(1, "Angela", Gender.FEMALE)
      ));
    } catch (DuplicatedUser e) {
    }
    final var users = userService.find();

    cp.dispose();
    System.out.println(users);
    assertTrue(users.size() == 1);
  }
}

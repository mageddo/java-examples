package ex08_jpa_mapping;

import com.mageddo.fruit.Fruit;
import com.mageddo.fruit.FruitDAOImpl;
import com.mageddo.fruit.FruitTemplates;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.jpa.JpaPlugin;

public class Main {
  public static void main(String[] args) {
    Jdbi jdbi = Jdbi.create("jdbc:h2:mem:test");
    jdbi.installPlugin(new JpaPlugin());

    final var fruits = jdbi.withHandle(handle -> {

      handle.execute("""
          CREATE TABLE FRUIT(
            IDT_FRUIT UUID NOT NULL PRIMARY KEY,
            NAM_FRUIT VARCHAR(255) UNIQUE NOT NULL,
            DAT_CREATED TIMESTAMP NOT NULL DEFAULT NOW()
          )
          """);

      final var dao = new FruitDAOImpl(jdbi);
      dao.create(FruitTemplates.orange());

      return handle
          .createQuery("SELECT * FROM FRUIT")
          .mapToBean(Fruit.class)
          .list();
    });

    System.out.println(fruits);

  }
}

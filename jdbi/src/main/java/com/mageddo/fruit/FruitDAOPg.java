package com.mageddo.fruit;

import java.util.List;

import org.jdbi.v3.core.Jdbi;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
public class FruitDAOPg implements FruitDAO {

  private final Jdbi jdbi;

  @Override
  public void create(Fruit fruit) {
    this.jdbi.useHandle(h -> {
//      h.createUpdate(QueryBuilder.buildInsertLenient(h))
      h.createUpdate("""
              INSERT INTO FRUIT (IDT_FRUIT, NAM_FRUIT) VALUES (:id, :name)
              """)
          .bindBean(fruit)
          .one();
    });
  }

  @Override
  public List<Fruit> find() {
    return this.jdbi.withHandle(h -> h.createQuery("SELECT * FROM FRUIT")
        .mapTo(Fruit.class)
        .list()
    );
  }

}

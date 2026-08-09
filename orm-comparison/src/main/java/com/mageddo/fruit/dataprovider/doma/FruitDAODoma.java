package com.mageddo.fruit.dataprovider.doma;

import com.mageddo.fruit.Fruit;
import com.mageddo.fruit.FruitDAO;

import com.mageddo.persistence.GenericDAO;

import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;

import org.seasar.doma.jdbc.criteria.QueryDsl;

@RequiredArgsConstructor
public class FruitDAODoma implements FruitDAO {

  private final QueryDsl queryDsl;
  private final GenericDAO<FruitRow> genericDAO;

  @Override
  public boolean createIfAbsent(Fruit fruit) {
    return this.genericDAO.createIfAbsent(FruitRowMapper.toRow(fruit));
  }

  @Override
  public boolean save(Fruit fruit) {
    return this.genericDAO.save(FruitRowMapper.toRow(fruit));
  }

  @Override
  public Fruit find(UUID id) {
    final var row = this.genericDAO.find(id, FruitRow.class);
    return FruitRowMapper.toDomain(row);
  }

  @Override
  public List<Fruit> findByName(String name) {
    final var dm = getDm();
    final var rows = this.queryDsl
        .from(dm)
        .where(c -> c.eq(dm.name, name))
        .fetch();
    return FruitRowMapper.toDomain(rows);
  }

  static FruitRow_ getDm() {
    return new FruitRow_();
  }

}

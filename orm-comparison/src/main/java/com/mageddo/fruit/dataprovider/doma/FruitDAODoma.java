package com.mageddo.fruit.dataprovider.doma;

import com.mageddo.fruit.Fruit;
import com.mageddo.fruit.FruitDAO;

import com.mageddo.fruit.config.doma.MetaModels;

import jakarta.inject.Singleton;

import java.util.UUID;

import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.Validate;
import org.seasar.doma.jdbc.criteria.QueryDsl;

@Singleton
@RequiredArgsConstructor
public class FruitDAODoma implements FruitDAO {

  private final QueryDsl queryDsl;

  @Override
  public boolean createIfAbsent(Fruit fruit) {
    final var dm = new FruitDomaRow_();
    final var row = FruitRowMapper.toRow(fruit);
    final var result = this.queryDsl
        .insert(dm)
        .single(row)
        .onDuplicateKeyIgnore()
        .execute();
    return result.getCount() == 1;
  }

  @Override
  public boolean save(Fruit fruit) {

    if (this.createIfAbsent(fruit)) {
      return true;
    }

    final var dm = new FruitDomaRow_();
    final var row = FruitRowMapper.toRow(fruit);
    final var affected = this.queryDsl
        .update(dm)
        .single(row)
        .execute()
        .getCount();
    Validate.isTrue(affected == 1, "Must update: %s", fruit.getId());
    return false;
  }

  @Override
  public Fruit find(UUID id) {
    final var dm = new FruitDomaRow_();
    final var row = this.queryDsl
        .from(dm)
        .where(c -> c.eq(MetaModels.getIdProperty(dm), id))
        .fetchOne();
    return FruitRowMapper.toDomain(row);
  }

}

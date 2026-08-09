package com.mageddo.fruit.dataprovider.doma;

import com.mageddo.fruit.Fruit;
import com.mageddo.fruit.FruitDAO;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.seasar.doma.jdbc.ConfigProvider;
import org.seasar.doma.jdbc.criteria.QueryDsl;

@Singleton
@Named("doma")
@RequiredArgsConstructor
public class FruitDAODoma implements FruitDAO {

  private final QueryDsl queryDsl;
  private final FruitDomaDao dao;

  @Override
  public boolean createIfAbsent(Fruit fruit) {
    final var _fruit = new FruitDomaRow_();
    final var result = this.queryDsl
        .insert(_fruit)
        .single(FruitDomaMapper.toRow(fruit))
        .onDuplicateKeyIgnore()
        .execute();
    return result.getCount() == 1;
  }

  @Override
  public Fruit save(Fruit fruit) {
    final var row = FruitDomaMapper.toRow(fruit);
    final var updated = this.dao.update(
        this.toDbId(row),
        row.getName(),
        row.getColor(),
        row.getSeason(),
        this.toReferrerId(row),
        this.toReferrerType(row),
        row.getCreatedAt(),
        row.getUpdatedAt()
    );

    if (updated == 0) {
      this.dao.insert(
          this.toDbId(row),
          row.getName(),
          row.getColor(),
          row.getSeason(),
          this.toReferrerId(row),
          this.toReferrerType(row),
          row.getCreatedAt(),
          row.getUpdatedAt()
      );
    }

    return this.find(fruit.getId());
  }

  @Override
  public Fruit find(UUID id) {
    return FruitDomaMapper.toDomain(this.dao.findById(id.toString()));
  }

  private String toDbId(FruitDomaRow row) {
    return row.getId().toString();
  }

  private String toReferrerId(FruitDomaRow row) {
    final var referrer = row.getReferrer();
    return referrer == null ? null : referrer.id();
  }

  private String toReferrerType(FruitDomaRow row) {
    final var referrer = row.getReferrer();
    return referrer == null ? null : referrer.type();
  }
}

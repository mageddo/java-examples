package com.mageddo.fruit.dataprovider.doma;

import java.util.UUID;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Sql;
import org.seasar.doma.Update;

@Dao
public interface FruitDomaDao {

  @Select
  @Sql("SELECT * FROM ebean_orm.FRUIT WHERE IDT_FRUIT = /* id */'00000000-0000-0000-0000-000000000000'")
  FruitDomaRow findById(UUID id);

  @Insert
  int insert(FruitDomaRow fruit);

  @Update
  int update(FruitDomaRow fruit);
}

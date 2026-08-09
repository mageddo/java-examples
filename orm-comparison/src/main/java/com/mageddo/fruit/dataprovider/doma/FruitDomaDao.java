package com.mageddo.fruit.dataprovider.doma;

import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Sql;
import org.seasar.doma.Update;

@Dao
public interface FruitDomaDao {

  @Select
  @Sql("SELECT * FROM orm.FRUIT WHERE IDT_FRUIT = CAST(/* id */'00000000-0000-0000-0000-000000000000' AS UUID)")
  FruitDomaRow findById(String id);

  @Insert
  @Sql("""
      INSERT INTO orm.FRUIT (
        IDT_FRUIT,
        NAM_FRUIT,
        NAM_COLOR,
        NAM_SEASON,
        IDT_REFERRER,
        IND_REFERRER,
        DAT_CREATED,
        DAT_UPDATED
      ) VALUES (
        CAST(/* id */'00000000-0000-0000-0000-000000000000' AS UUID),
        /* name */'',
        /* color */'',
        /* season */'',
        /* referrerId */'',
        /* referrerType */'',
        /* createdAt */null,
        /* updatedAt */null
      )
      """)
  int insert(
      String id,
      String name,
      String color,
      String season,
      String referrerId,
      String referrerType,
      java.sql.Timestamp createdAt,
      java.sql.Timestamp updatedAt
  );

  @Update
  @Sql("""
      UPDATE orm.FRUIT SET
        NAM_FRUIT = /* name */'',
        NAM_COLOR = /* color */'',
        NAM_SEASON = /* season */'',
        IDT_REFERRER = /* referrerId */'',
        IND_REFERRER = /* referrerType */'',
        DAT_CREATED = /* createdAt */null,
        DAT_UPDATED = /* updatedAt */null
      WHERE IDT_FRUIT = CAST(/* id */'00000000-0000-0000-0000-000000000000' AS UUID)
      """)
  int update(
      String id,
      String name,
      String color,
      String season,
      String referrerId,
      String referrerType,
      java.sql.Timestamp createdAt,
      java.sql.Timestamp updatedAt
  );
}

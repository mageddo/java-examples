package com.mageddo.persistence.config;

import com.mageddo.fruit.FruitDAO;
import com.mageddo.fruit.config.ebean.EbeanInsertIfAbsent;
import com.mageddo.fruit.dataprovider.doma.FruitDAODoma;
import com.mageddo.fruit.dataprovider.ebean.FruitDAOEbean;
import com.mageddo.persistence.OrmProvider;
import com.mageddo.persistence.doma.GenericDAODoma;
import com.mageddo.persistence.ebean.GenericDAOEbean;

import io.ebean.Database;

import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;

import lombok.extern.slf4j.Slf4j;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.seasar.doma.jdbc.criteria.QueryDsl;

/**
 * Escolhe em runtime, pela property {@code orm.provider}, qual implementação de
 * {@link FruitDAO} e de {@link com.mageddo.persistence.GenericDAO} será usada.
 * <p>
 * As implementações não são beans CDI: quem as monta é este produtor, então só a do ORM
 * selecionado chega ao {@link com.mageddo.fruit.FruitService}.
 */
@Slf4j
@Singleton
public class DAOProducer {

  @Produces
  @Singleton
  public OrmProvider ormProvider(@ConfigProperty(name = "orm.provider") String provider) {
    final var orm = OrmProvider.of(provider);
    log.info("status=ormSelected, orm={}", orm);
    return orm;
  }

  @Produces
  @Singleton
  public FruitDAO fruitDAO(
      OrmProvider orm,
      Database database,
      EbeanInsertIfAbsent insertIfAbsent,
      QueryDsl queryDsl
  ) {
    return switch (orm) {
      case EBEAN -> new FruitDAOEbean(database, new GenericDAOEbean<>(database, insertIfAbsent));
      case DOMA -> new FruitDAODoma(queryDsl, new GenericDAODoma<>(queryDsl));
    };
  }
}

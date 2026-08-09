package com.mageddo.persistence.ebean;

import com.mageddo.fruit.config.ebean.EbeanInsertIfAbsent;
import com.mageddo.persistence.GenericDAO;

import io.ebean.Database;
import io.ebean.InsertOptions;

import jakarta.enterprise.context.Dependent;

import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.Validate;

/**
 * Operações de persistência derivadas do mapeamento da entidade, sem SQL por tabela.
 * <p>
 * Tanto o {@link #createIfAbsent(Object)} quanto o {@link #save(Object)} resolvem o
 * conflito no próprio banco, em um único statement, mantendo a transação utilizável
 * para as operações seguintes.
 */
@Dependent
@RequiredArgsConstructor
public class GenericDAOEbean<Bean> implements GenericDAO<Bean> {

  private final Database database;
  private final EbeanInsertIfAbsent insertIfAbsent;

  /**
   * @return true se a entidade foi criada, false se já existia
   */
  @Override
  public boolean createIfAbsent(Bean bean) {
    return this.insertIfAbsent.execute(this.database, bean);
  }

  @Override
  public boolean save(Bean bean) {
    if (this.createIfAbsent(bean)) {
      return true;
    }
    this.database.update(bean);
    return false;
  }

  @Override
  public Bean find(Object id, Class<Bean> beanClass) {
    return this.database.find(beanClass, id);
  }

  @Override
  public Bean mustFind(Object id, Class<Bean> beanClass) {
    final var bean = this.find(id, beanClass);
    Validate.notNull(bean, "%s not found: %s", beanClass.getSimpleName(), id);
    return bean;
  }
}

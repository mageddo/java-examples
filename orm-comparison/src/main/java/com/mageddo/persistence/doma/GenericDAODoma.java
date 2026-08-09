package com.mageddo.persistence.doma;

import com.mageddo.fruit.config.doma.EntityModels;
import com.mageddo.fruit.config.doma.MetaModels;
import com.mageddo.persistence.GenericDAO;

import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.Validate;
import org.seasar.doma.jdbc.criteria.QueryDsl;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;

/**
 * Contraparte Doma do {@link com.mageddo.persistence.ebean.GenericDAOEbean}: operações de
 * persistência derivadas do metamodel da entidade, sem SQL por tabela.
 * <p>
 * O {@code createIfAbsent} resolve o conflito no próprio banco, em um único statement
 * ({@code INSERT ... ON CONFLICT DO NOTHING}), mantendo a transação utilizável para as
 * operações seguintes.
 */
@RequiredArgsConstructor
public class GenericDAODoma<Bean> implements GenericDAO<Bean> {

  private final QueryDsl queryDsl;

  /**
   * @return true se a entidade foi criada, false se já existia
   */
  @Override
  public boolean createIfAbsent(Bean bean) {
    final var dm = this.entityModelOf(bean);
    final var result = this.queryDsl
        .insert(dm)
        .single(bean)
        .onDuplicateKeyIgnore()
        .execute();
    return result.getCount() == 1;
  }

  @Override
  public boolean save(Bean bean) {

    if (this.createIfAbsent(bean)) {
      return true;
    }

    final var dm = this.entityModelOf(bean);
    final var affected = this.queryDsl
        .update(dm)
        .single(bean)
        .execute()
        .getCount();
    Validate.isTrue(affected == 1, "Must update: %s", bean);
    return false;
  }

  @Override
  public Bean find(Object id, Class<Bean> beanClass) {
    final var dm = EntityModels.get(beanClass);
    return this.queryDsl
        .from(dm)
        .where(c -> c.eq(MetaModels.<Object>getIdProperty(dm), id))
        .fetchOptional()
        .orElse(null);
  }

  @Override
  public Bean mustFind(Object id, Class<Bean> beanClass) {
    final var bean = this.find(id, beanClass);
    Validate.notNull(bean, "%s not found: %s", beanClass.getSimpleName(), id);
    return bean;
  }

  @SuppressWarnings("unchecked")
  EntityMetamodel<Bean> entityModelOf(Bean bean) {
    return EntityModels.get((Class<Bean>) bean.getClass());
  }
}

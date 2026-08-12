package com.mageddo.basket.dataprovider.doma;

import java.util.UUID;

import com.mageddo.basket.Basket;
import com.mageddo.basket.BasketDAO;
import com.mageddo.persistence.GenericDAO;

import lombok.RequiredArgsConstructor;

/**
 * Contraparte Doma do {@link com.mageddo.basket.dataprovider.ebean.BasketDAOEbean}, também
 * sem nenhum SQL próprio.
 */
@RequiredArgsConstructor
public class BasketDAODoma implements BasketDAO {

  private final GenericDAO<BasketRow> genericDAO;

  @Override
  public boolean createIfAbsent(Basket basket) {
    return this.genericDAO.createIfAbsent(BasketRowMapper.toRow(basket));
  }

  @Override
  public boolean save(Basket basket) {
    return this.genericDAO.save(BasketRowMapper.toRow(basket));
  }

  @Override
  public Basket find(UUID id) {
    return BasketRowMapper.toDomain(this.genericDAO.find(id, BasketRow.class));
  }
}

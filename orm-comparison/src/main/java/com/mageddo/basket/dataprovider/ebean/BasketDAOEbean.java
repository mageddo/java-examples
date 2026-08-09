package com.mageddo.basket.dataprovider.ebean;

import java.util.UUID;

import com.mageddo.basket.Basket;
import com.mageddo.basket.BasketDAO;
import com.mageddo.basket.dataprovider.BasketRow;
import com.mageddo.basket.dataprovider.mapper.BasketRowMapper;
import com.mageddo.persistence.GenericDAO;

import lombok.RequiredArgsConstructor;

/**
 * Repare que não há uma linha de SQL aqui: tudo vem do {@link GenericDAO}, que deriva
 * tabela, colunas e tipos do mapeamento de {@link BasketRow}.
 */
@RequiredArgsConstructor
public class BasketDAOEbean implements BasketDAO {

  private final GenericDAO<BasketRow> genericDAO;

  @Override
  public boolean createIfAbsent(Basket basket) {
    return this.genericDAO.createIfAbsent(BasketRowMapper.of(basket));
  }

  @Override
  public boolean save(Basket basket) {
    return this.genericDAO.save(BasketRowMapper.of(basket));
  }

  @Override
  public Basket find(UUID id) {
    return BasketRowMapper.toDomain(this.genericDAO.find(id, BasketRow.class));
  }
}

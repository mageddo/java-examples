package com.mageddo.basket;

import java.util.UUID;

public interface BasketDAO {

  boolean createIfAbsent(Basket basket);

  boolean save(Basket basket);

  Basket find(UUID id);
}

package com.mageddo.basket.dataprovider.mapper;

import com.mageddo.basket.Basket;
import com.mageddo.basket.dataprovider.BasketRow;

public class BasketRowMapper {

  private BasketRowMapper() {
  }

  public static Basket toDomain(BasketRow row) {
    if (row == null) {
      return null;
    }
    return Basket
        .builder()
        .id(row.getId())
        .name(row.getName())
        .createdAt(row.getCreatedAt())
        .updatedAt(row.getUpdatedAt())
        .build();
  }

  public static BasketRow of(Basket basket) {
    final var row = new BasketRow();
    row.setId(basket.getId());
    row.setName(basket.getName());
    row.setCreatedAt(basket.getCreatedAt());
    row.setUpdatedAt(basket.getUpdatedAt());
    return row;
  }
}

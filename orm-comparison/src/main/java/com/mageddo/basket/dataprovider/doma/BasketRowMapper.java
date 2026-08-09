package com.mageddo.basket.dataprovider.doma;

import com.mageddo.basket.Basket;

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

  public static BasketRow toRow(Basket basket) {
    final var row = new BasketRow();
    row.setId(basket.getId());
    row.setName(basket.getName());
    row.setCreatedAt(basket.getCreatedAt());
    row.setUpdatedAt(basket.getUpdatedAt());
    return row;
  }
}

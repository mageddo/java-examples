package com.mageddo.basket;

import java.time.Instant;
import java.util.UUID;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

/**
 * Segunda entidade do projeto, existe para provar que o
 * {@link com.mageddo.persistence.GenericDAO} é realmente genérico: uma tabela nova entra no
 * sistema sem nenhum SQL, HQL ou query builder por tabela.
 */
@Value
@Builder
public class Basket {

  @NonNull
  UUID id;

  @NonNull
  String name;

  @NonNull
  Instant createdAt;

  @NonNull
  Instant updatedAt;

}

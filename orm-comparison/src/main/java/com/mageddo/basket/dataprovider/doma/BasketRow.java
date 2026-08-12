package com.mageddo.basket.dataprovider.doma;

import java.time.Instant;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Metamodel;
import org.seasar.doma.Table;

@Data
@Entity(metamodel = @Metamodel)
@Table(name = "BASKET", schema = "orm")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BasketRow {

  @Id
  @Column(name = "IDT_BASKET")
  UUID id;

  @Column(name = "NAM_BASKET")
  String name;

  @Column(name = "DAT_CREATED")
  Instant createdAt;

  @Column(name = "DAT_UPDATED")
  Instant updatedAt;

}

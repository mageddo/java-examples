package com.mageddo.fruit.dataprovider.doma;

import java.time.Instant;
import java.util.UUID;

import com.mageddo.referrer.dataprovider.doma.ReferrerRow;

import lombok.AccessLevel;
import lombok.Data;

import lombok.experimental.FieldDefaults;

import org.seasar.doma.Column;
import org.seasar.doma.Embedded;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Metamodel;
import org.seasar.doma.Table;

@Data
@Entity(metamodel = @Metamodel)
@Table(name = "FRUIT", schema = "orm")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FruitDomaRow {

  @Id
  @Column(name = "IDT_FRUIT")
  UUID id;

  @Column(name = "NAM_FRUIT")
  String name;

  @Column(name = "NAM_COLOR")
  String color;

  @Column(name = "NAM_SEASON")
  String season;

  @Column(name = "DAT_CREATED")
  Instant createdAt;

  @Column(name = "DAT_UPDATED")
  Instant updatedAt;

  @Embedded
  ReferrerRow referrer;

}

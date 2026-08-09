package com.mageddo.fruit.dataprovider;

import jakarta.persistence.Column;
import java.time.Instant;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Embedded;
import jakarta.persistence.Table;
import com.mageddo.referrer.ReferrerRow;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Entity
@Table(name = "FRUIT", schema = "ebean_orm")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FruitRow {

  @Id
  @Column(name = "IDT_FRUIT", nullable = false)
  UUID id;

  @Column(name = "NAM_FRUIT")
  String name;

  @Column(name = "NAM_COLOR")
  String color;

  @Column(name = "NAM_SEASON")
  String season;

  @Column(name = "DAT_CREATED", nullable = false)
  Instant createdAt;

  @Column(name = "DAT_UPDATED", nullable = false)
  Instant updatedAt;

  @Embedded
  ReferrerRow referrer;
}

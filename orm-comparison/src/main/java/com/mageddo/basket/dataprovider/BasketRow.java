package com.mageddo.basket.dataprovider;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Entity
@Table(name = "basket", schema = "orm")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BasketRow {

  @Id
  @Column(name = "IDT_BASKET", nullable = false)
  UUID id;

  @Column(name = "NAM_BASKET")
  String name;

  @Column(name = "DAT_CREATED", nullable = false)
  Instant createdAt;

  @Column(name = "DAT_UPDATED", nullable = false)
  Instant updatedAt;
}

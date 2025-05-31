package com.mageddo.fruit;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Entity
@Table(name = "FRUIT")
public class Fruit {

  @Id
  @Column(name = "IDT_FRUIT")
  @NonNull
  UUID id;

  @NonNull
  @Column(name = "NAM_FRUIT")
  String name;

  @Column(name = "DAT_CREATED")
  LocalDateTime createdAt;
}

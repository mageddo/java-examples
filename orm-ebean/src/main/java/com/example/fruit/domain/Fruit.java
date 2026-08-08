package com.example.fruit.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "fruit")
public class Fruit {

  @Id
  private Long id;

  private String name;

  private String color;

  private String season;
}

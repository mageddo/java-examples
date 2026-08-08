package com.mageddo.fruit.dataprovider;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "fruit")
public class FruitRow {

  @Id
  @Column(name = "idt_fruit", nullable = false)
  private UUID id;

  @Column(name = "nam_name")
  private String name;

  @Column(name = "txt_color")
  private String color;

  @Column(name = "txt_season")
  private String season;
}

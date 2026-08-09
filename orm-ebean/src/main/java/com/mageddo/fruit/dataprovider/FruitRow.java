package com.mageddo.fruit.dataprovider;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Embedded;
import jakarta.persistence.Table;
import com.mageddo.referrer.ReferrerRow;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "fruit", schema = "ebean_orm")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FruitRow {

  @Id
  @Column(name = "idt_fruit", nullable = false)
  UUID id;

  @Column(name = "nam_name")
  String name;

  @Column(name = "txt_color")
  String color;

  @Column(name = "txt_season")
  String season;

  @Embedded
  ReferrerRow referrer;
}

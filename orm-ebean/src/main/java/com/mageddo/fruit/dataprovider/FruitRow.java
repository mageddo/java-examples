package com.mageddo.fruit.dataprovider;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Embedded;
import jakarta.persistence.Table;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
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

  @AttributeOverrides({
      @AttributeOverride(name = "id", column = @Column(name = "IDT_REFERRER", length = 36)),
      @AttributeOverride(name = "type", column = @Column(name = "IND_REFERRER", length = 36))
  })
  @Embedded
  ReferrerRow referrer;
}

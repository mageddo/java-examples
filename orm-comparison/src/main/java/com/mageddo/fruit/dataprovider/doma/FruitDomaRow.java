package com.mageddo.fruit.dataprovider.doma;

import com.mageddo.fruit.config.doma.DomaConfig;
import java.sql.Timestamp;
import java.util.UUID;

import lombok.Data;

import org.seasar.doma.Column;
import org.seasar.doma.Embedded;
import org.seasar.doma.Entity;
import org.seasar.doma.ExternalDomain;
import org.seasar.doma.Id;
import org.seasar.doma.Metamodel;
import org.seasar.doma.Table;

@Data
@Entity(metamodel = @Metamodel)
@Table(name = "FRUIT", schema = "orm")
public class FruitDomaRow {

  @Id
  @Column(name = "IDT_FRUIT")
  private UUID id;

  @Column(name = "NAM_FRUIT")
  private String name;

  @Column(name = "NAM_COLOR")
  private String color;

  @Column(name = "NAM_SEASON")
  private String season;

  @Column(name = "DAT_CREATED")
  private Timestamp createdAt;

  @Column(name = "DAT_UPDATED")
  private Timestamp updatedAt;

  @Embedded
  private FruitDomaReferrerRow referrer;

}

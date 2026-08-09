package com.mageddo.fruit.dataprovider.doma;

import com.mageddo.fruit.config.doma.DomaFruitConfig;
import java.time.Instant;
import java.util.UUID;
import org.seasar.doma.Column;
import org.seasar.doma.Embedded;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;

@Entity
@Table(name = "FRUIT", schema = DomaFruitConfig.FRUIT_SCHEMA)
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
  private Instant createdAt;

  @Column(name = "DAT_UPDATED")
  private Instant updatedAt;

  @Embedded
  private FruitDomaReferrerRow referrer;

  public UUID getId() {
    return this.id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getColor() {
    return this.color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public String getSeason() {
    return this.season;
  }

  public void setSeason(String season) {
    this.season = season;
  }

  public Instant getCreatedAt() {
    return this.createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  public Instant getUpdatedAt() {
    return this.updatedAt;
  }

  public void setUpdatedAt(Instant updatedAt) {
    this.updatedAt = updatedAt;
  }

  public FruitDomaReferrerRow getReferrer() {
    return this.referrer;
  }

  public void setReferrer(FruitDomaReferrerRow referrer) {
    this.referrer = referrer;
  }
}


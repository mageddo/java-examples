package com.mageddo.fruit.dataprovider.doma;

import com.mageddo.fruit.config.doma.DomaConfig;
import java.sql.Timestamp;
import java.util.UUID;
import org.seasar.doma.Column;
import org.seasar.doma.Embedded;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Metamodel;
import org.seasar.doma.Table;

@Entity(metamodel = @Metamodel)
@Table(name = "FRUIT", schema = "orm")
public class FruitDomaRow {

  @Id
  @Column(name = "IDT_FRUIT")
  private String id;

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

  public UUID getId() {
    return this.id == null ? null : UUID.fromString(this.id);
  }

  public void setId(String id) {
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

  public Timestamp getCreatedAt() {
    return this.createdAt;
  }

  public void setCreatedAt(Timestamp createdAt) {
    this.createdAt = createdAt;
  }

  public Timestamp getUpdatedAt() {
    return this.updatedAt;
  }

  public void setUpdatedAt(Timestamp updatedAt) {
    this.updatedAt = updatedAt;
  }

  public FruitDomaReferrerRow getReferrer() {
    return this.referrer;
  }

  public void setReferrer(FruitDomaReferrerRow referrer) {
    this.referrer = referrer;
  }
}

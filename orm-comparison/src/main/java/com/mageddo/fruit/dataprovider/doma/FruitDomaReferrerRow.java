package com.mageddo.fruit.dataprovider.doma;

import org.seasar.doma.Column;
import org.seasar.doma.Embeddable;

@Embeddable
public class FruitDomaReferrerRow {

  @Column(name = "IDT_REFERRER")
  private String id;

  @Column(name = "IND_REFERRER")
  private String type;

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getType() {
    return this.type;
  }

  public void setType(String type) {
    this.type = type;
  }
}


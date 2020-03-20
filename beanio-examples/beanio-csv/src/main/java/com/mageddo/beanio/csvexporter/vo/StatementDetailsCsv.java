package com.mageddo.beanio.csvexporter.vo;

import org.beanio.annotation.Field;
import org.beanio.annotation.Record;

@Record()
public class StatementDetailsCsv {

  @Field
  private Long productId;

  @Field
  private String description;

  public Long getProductId() {
    return productId;
  }

  public StatementDetailsCsv setProductId(Long productId) {
    this.productId = productId;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public StatementDetailsCsv setDescription(String description) {
    this.description = description;
    return this;
  }

  @Override
  public String toString() {
    return "StatementDetailsCsv{" +
        "productId=" + productId +
        ", description='" + description + '\'' +
        '}';
  }
}

package com.mageddo.beanio.csvexporter.vo;

import com.mageddo.beanio.csvexporter.handler.SubListHandler;

import org.beanio.annotation.Field;
import org.beanio.annotation.Record;
import org.beanio.builder.Align;

import java.math.BigDecimal;
import java.util.List;

@Record
public class StatementCsv {

	@Field(align = Align.RIGHT, padding = '0', length = 10, required = true)
	private BigDecimal amount;

	@Field(align = Align.LEFT, padding = ' ', length = 50, required = true)
	private String description;

	@Field(handlerClass = SubListHandler.class, type = SubList.class)
	private SubList details;

	public String getDescription() {
		return description;
	}

	public StatementCsv setDescription(String description) {
		this.description = description;
		return this;
	}

  public BigDecimal getAmount() {
    return amount;
  }

  public StatementCsv setAmount(BigDecimal amount) {
    this.amount = amount;
    return this;
  }

  public SubList getDetails() {
    return details;
  }

  public StatementCsv setDetails(SubList details) {
    this.details = details;
    return this;
  }

  @Override
  public String toString() {
    return "StatementCsv{" +
        "amount=" + amount +
        ", description='" + description + '\'' +
        ", details=" + details +
        '}';
  }
}

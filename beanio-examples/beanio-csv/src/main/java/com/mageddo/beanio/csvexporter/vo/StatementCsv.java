package com.mageddo.beanio.csvexporter.vo;

import com.mageddo.beanio.csvexporter.handler.ListHandler;

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

	@Field(handlerClass = ListHandler.class)
	private List<StatementDetailsCsv> details;

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

  public List<StatementDetailsCsv> getDetails() {
    return details;
  }

  public StatementCsv setDetails(List<StatementDetailsCsv> details) {
    this.details = details;
    return this;
  }
}

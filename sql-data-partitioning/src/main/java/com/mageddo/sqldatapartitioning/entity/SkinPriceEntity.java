package com.mageddo.sqldatapartitioning.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SkinPriceEntity {
	private Long id;
	private String hashName;
	private LocalDateTime occurrence;
	private BigDecimal price;

	public Long getId() {
		return id;
	}

	public SkinPriceEntity setId(Long id) {
		this.id = id;
		return this;
	}

	public String getHashName() {
		return hashName;
	}

	public SkinPriceEntity setHashName(String hashName) {
		this.hashName = hashName;
		return this;
	}

	public LocalDateTime getOccurrence() {
		return occurrence;
	}

	public SkinPriceEntity setOccurrence(LocalDateTime occurrence) {
		this.occurrence = occurrence;
		return this;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public SkinPriceEntity setPrice(BigDecimal price) {
		this.price = price;
		return this;
	}
}

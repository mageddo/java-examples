package com.mageddo.sqldatapartitioning.controller.v1.vo;

import com.mageddo.sqldatapartitioning.entity.SkinPriceEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SkinPriceReqV1 {

	private String hashName;
	private LocalDateTime occurrence;
	private BigDecimal price;

	public String getHashName() {
		return hashName;
	}

	public SkinPriceReqV1 setHashName(String hashName) {
		this.hashName = hashName;
		return this;
	}

	public LocalDateTime getOccurrence() {
		return occurrence;
	}

	public SkinPriceReqV1 setOccurrence(LocalDateTime occurrence) {
		this.occurrence = occurrence;
		return this;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public SkinPriceReqV1 setPrice(BigDecimal price) {
		this.price = price;
		return this;
	}

	public SkinPriceEntity toEntity() {
		return new SkinPriceEntity()
			.setHashName(getHashName())
			.setOccurrence(getOccurrence())
			.setPrice(getPrice())
		;
	}
}

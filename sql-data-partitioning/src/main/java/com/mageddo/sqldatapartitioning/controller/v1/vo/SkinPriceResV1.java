package com.mageddo.sqldatapartitioning.controller.v1.vo;

import com.mageddo.sqldatapartitioning.entity.SkinPriceEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SkinPriceResV1 {

	private String hashName;
	private LocalDateTime occurrence;
	private BigDecimal price;
	private Long id;

	public static SkinPriceResV1 valueOf(SkinPriceEntity entity) {
		if(entity == null){
			return null;
		}
		return new SkinPriceResV1()
			.setHashName(entity.getHashName())
			.setOccurrence(entity.getOccurrence())
			.setPrice(entity.getPrice())
			.setId(entity.getId())
		;
	}

	public String getHashName() {
		return hashName;
	}

	public SkinPriceResV1 setHashName(String hashName) {
		this.hashName = hashName;
		return this;
	}

	public LocalDateTime getOccurrence() {
		return occurrence;
	}

	public SkinPriceResV1 setOccurrence(LocalDateTime occurrence) {
		this.occurrence = occurrence;
		return this;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public SkinPriceResV1 setPrice(BigDecimal price) {
		this.price = price;
		return this;
	}

	public SkinPriceResV1 setId(Long id) {
		this.id = id;
		return this;
	}

	public Long getId() {
		return id;
	}
}

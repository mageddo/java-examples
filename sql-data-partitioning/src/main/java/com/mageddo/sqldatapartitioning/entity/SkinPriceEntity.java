package com.mageddo.sqldatapartitioning.entity;

import com.mageddo.sqldatapartitioning.enums.SkinPriceType;
import com.mageddo.sqldatapartitioning.utils.JdbcHelper;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SkinPriceEntity {
	private Long id;
	private String hashName;
	private LocalDateTime occurrence;
	private BigDecimal price;
	private SkinPriceType type;

	public static RowMapper<SkinPriceEntity> mapper() {
		return (rs, i) -> {
			return new SkinPriceEntity()
				.setId(rs.getLong("IDT_BSK_SKIN_SALE_HISTORY"))
				.setPrice(rs.getBigDecimal("NUM_PRICE"))
				.setOccurrence(JdbcHelper.getLocalDateTime(rs, "DAT_OCCURRENCE"))
				.setHashName(rs.getString("COD_SKIN"))
				.setType(SkinPriceType.fromCode(rs.getInt("IND_TYPE")))
			;
		};
	}

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

	public SkinPriceEntity setType(SkinPriceType type) {
		this.type = type;
		return this;
	}

	public SkinPriceType getType() {
		return type;
	}
}

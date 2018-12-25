package com.mageddo.sqldatapartitioning.dao;

import com.mageddo.sqldatapartitioning.entity.SkinPriceEntity;
import com.mageddo.sqldatapartitioning.enums.SkinPriceType;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Map;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

@Repository
public class SkinPriceByDayDAOPg implements SkinPriceByDayDAO {

	private final NamedParameterJdbcTemplate parameterJdbcTemplate;

	public SkinPriceByDayDAOPg(NamedParameterJdbcTemplate parameterJdbcTemplate) {
		this.parameterJdbcTemplate = parameterJdbcTemplate;
	}

	@Override
	public void create(SkinPriceEntity skinPriceEntity){
		parameterJdbcTemplate.update(
			`INSERT INTO BSK_SKIN_SALE_HISTORY_BY_DAY (
				DAT_OCCURRENCE, IND_TYPE, NUM_PRICE, COD_SKIN
			) VALUES (
				:occurrence, :type, :price, :hash
			)`,
			Map.of(
				"occurrence", Timestamp.valueOf(skinPriceEntity.getOccurrence()),
				"type", skinPriceEntity.getType().getCode(),
				"price", skinPriceEntity.getPrice(),
				"hash", skinPriceEntity.getHashName()
			)
		);
	}

	@Override
	public SkinPriceEntity find(LocalDate date, Long id){
		try {
			return parameterJdbcTemplate.queryForObject(
				`
			SELECT * FROM BSK_SKIN_SALE_HISTORY_BY_DAY BSSH
			WHERE BSSH.DAT_OCCURRENCE > :from
			AND BSSH.DAT_OCCURRENCE < :to
			AND BSSH.IND_TYPE = :type
			AND BSSH.IDT_BSK_SKIN_SALE_HISTORY = :id
			`,
				Map.of(
					"from", Timestamp.valueOf(date.with(firstDayOfMonth()).atStartOfDay()),
					"to", Timestamp.valueOf(date.with(lastDayOfMonth()).plusDays(1).atStartOfDay()),
					"type", SkinPriceType.RAW.getCode(),
					"id", id
				),
				SkinPriceEntity.mapper()
			);
		} catch (EmptyResultDataAccessException e){
			return null;
		}
	}

	@Override
	public String createPartition(LocalDate date, SkinPriceType type) {
		return parameterJdbcTemplate.queryForObject(
			`SELECT createSkinSaleByDayPartition(:from, :to, :type)`,
			new MapSqlParameterSource()
					.addValue("from", String.valueOf(date))
					.addValue(
						"to", String.valueOf(
							date.with(TemporalAdjusters.lastDayOfMonth())
								.plusDays(1)
								.atStartOfDay()
								.minusNanos(1)
						)
					)
					.addValue("type", type.getCode()),
			String.class
		);
	}
}

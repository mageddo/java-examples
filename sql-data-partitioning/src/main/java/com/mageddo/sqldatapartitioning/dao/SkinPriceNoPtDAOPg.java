package com.mageddo.sqldatapartitioning.dao;

import com.mageddo.sqldatapartitioning.entity.SkinPriceEntity;
import com.mageddo.sqldatapartitioning.enums.SkinPriceType;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Map;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

@Repository
public class SkinPriceNoPtDAOPg implements SkinPriceNoPtDAO {

	private final NamedParameterJdbcTemplate parameterJdbcTemplate;

	public SkinPriceNoPtDAOPg(NamedParameterJdbcTemplate parameterJdbcTemplate) {
		this.parameterJdbcTemplate = parameterJdbcTemplate;
	}

	@Override
	public void create(SkinPriceEntity skinPriceEntity){
		parameterJdbcTemplate.update(
			`INSERT INTO BSK_SKIN_SALE_HISTORY_NO_PT (
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
			SELECT * FROM BSK_SKIN_SALE_HISTORY_NO_PT BSSH
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

}

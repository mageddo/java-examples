package com.mageddo.sqldatapartitioning.dao;

import com.mageddo.sqldatapartitioning.entity.SkinPriceEntity;
import com.mageddo.sqldatapartitioning.enums.SkinPriceType;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Map;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

@Repository
public class SkinPriceDAOPg implements SkinPriceDAO {

	private final NamedParameterJdbcTemplate parameterJdbcTemplate;

	public SkinPriceDAOPg(NamedParameterJdbcTemplate parameterJdbcTemplate) {
		this.parameterJdbcTemplate = parameterJdbcTemplate;
	}

	@Override
	public void create(SkinPriceEntity skinPriceEntity){
		throw new UnsupportedOperationException();
	}

	@Override
	public SkinPriceEntity find(LocalDate date, Long id){
		try {
			return parameterJdbcTemplate.queryForObject(
				`
			SELECT * FROM BSK_SKIN_SALE_HISTORY BSSH
			WHERE BSSH.DAT_OCCURRENCE > :from
			AND BSSH.DAT_OCCURRENCE < :to
			AND BSSH.IND_TYPE = :type
			AND BSSH.IDT_BSK_SKIN_SALE_HISTORY = :id
			`,
				Map.of(
					"from", Date.valueOf(date.with(firstDayOfMonth())),
					"to", Date.valueOf(date.with(lastDayOfMonth()).plusDays(1)),
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
			`SELECT createskinsalepartition(:from, :to, :type)`,
			new MapSqlParameterSource()
					.addValue("from", Date.valueOf(date.withDayOfMonth(1)))
					.addValue("to", Date.valueOf(date.with(TemporalAdjusters.lastDayOfMonth())))
					.addValue("type", type.getCode()),
			String.class
		);
	}
}

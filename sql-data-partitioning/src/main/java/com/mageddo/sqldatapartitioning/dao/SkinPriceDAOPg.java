package com.mageddo.sqldatapartitioning.dao;

import com.mageddo.sqldatapartitioning.entity.SkinPriceEntity;
import com.mageddo.sqldatapartitioning.enums.SkinPriceType;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

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
		throw new UnsupportedOperationException();
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

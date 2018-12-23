package com.mageddo.sqldatapartitioning.dao;

import com.mageddo.sqldatapartitioning.entity.SkinPriceEntity;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

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
}

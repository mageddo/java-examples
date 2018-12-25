package com.mageddo.sqldatapartitioning.dao;

import com.mageddo.sqldatapartitioning.entity.SkinPriceEntity;

import java.time.LocalDate;

public interface SkinPriceNoPtDAO {
	void create(SkinPriceEntity skinPriceEntity);

	SkinPriceEntity find(LocalDate date, Long id);

}

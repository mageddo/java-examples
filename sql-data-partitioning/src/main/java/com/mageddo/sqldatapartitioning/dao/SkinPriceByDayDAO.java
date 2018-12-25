package com.mageddo.sqldatapartitioning.dao;

import com.mageddo.sqldatapartitioning.entity.SkinPriceEntity;
import com.mageddo.sqldatapartitioning.enums.SkinPriceType;

import java.time.LocalDate;

public interface SkinPriceByDayDAO {
	void create(SkinPriceEntity skinPriceEntity);

	SkinPriceEntity find(LocalDate date, Long id);

	String createPartition(LocalDate date, SkinPriceType type);
}

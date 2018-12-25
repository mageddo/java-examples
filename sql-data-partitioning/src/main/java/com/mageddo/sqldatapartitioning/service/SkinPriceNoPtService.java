package com.mageddo.sqldatapartitioning.service;

import com.mageddo.sqldatapartitioning.dao.SkinPriceNoPtDAO;
import com.mageddo.sqldatapartitioning.entity.SkinPriceEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class SkinPriceNoPtService {

	private final SkinPriceNoPtDAO skinPriceByDayDAO;

	public SkinPriceNoPtService(SkinPriceNoPtDAO skinPriceByDayDAO) {
		this.skinPriceByDayDAO = skinPriceByDayDAO;
	}

	public SkinPriceEntity find(LocalDate date, Long id) {
		return skinPriceByDayDAO.find(date, id);
	}

	public void create(SkinPriceEntity skinPrice) {
		skinPriceByDayDAO.create(skinPrice);
	}

}

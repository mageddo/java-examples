package com.mageddo.sqldatapartitioning.service;

import com.mageddo.sqldatapartitioning.ApplicationContextProvider;
import com.mageddo.sqldatapartitioning.dao.SkinPriceDAO;
import com.mageddo.sqldatapartitioning.entity.SkinPriceEntity;
import com.mageddo.sqldatapartitioning.enums.SkinPriceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class SkinPriceService {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final SkinPriceDAO skinPriceDAO;

	public SkinPriceService(SkinPriceDAO skinPriceDAO) {
		this.skinPriceDAO = skinPriceDAO;
	}

	public SkinPriceEntity find(LocalDate date, Long id) {
		return skinPriceDAO.find(date, id);
	}

	public void create(SkinPriceEntity skinPrice) {
		skinPriceDAO.create(skinPrice);
	}

	public void createPartitions() {
		LocalDate currentDate = LocalDate.now().withDayOfMonth(1);
		for (int i = 0; i < 18; i++) {
			try {
				self().createPartition(currentDate, SkinPriceType.RAW);
			} catch (Exception e){
				logger.warn("status=creation-failed", e);
			}
			currentDate = currentDate.plusMonths(1);
		}
	}

	protected SkinPriceService self(){
//		AopContext.currentProxy()
		return ApplicationContextProvider.context().getBean(getClass());
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void createPartition(LocalDate date, SkinPriceType type){
		skinPriceDAO.createPartition(date, type);
	}
}

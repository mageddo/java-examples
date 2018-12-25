package com.mageddo.sqldatapartitioning.service;

import com.mageddo.sqldatapartitioning.ApplicationContextProvider;
import com.mageddo.sqldatapartitioning.dao.SkinPriceByDayDAO;
import com.mageddo.sqldatapartitioning.entity.SkinPriceEntity;
import com.mageddo.sqldatapartitioning.enums.SkinPriceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class SkinPriceByDayService {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final SkinPriceByDayDAO skinPriceByDayDAO;

	public SkinPriceByDayService(SkinPriceByDayDAO skinPriceByDayDAO) {
		this.skinPriceByDayDAO = skinPriceByDayDAO;
	}

	public SkinPriceEntity find(LocalDate date, Long id) {
		return skinPriceByDayDAO.find(date, id);
	}

	public void create(SkinPriceEntity skinPrice) {
		skinPriceByDayDAO.create(skinPrice);
	}

	public void createPartitions() {
		LocalDate currentDate = LocalDate.now();
		for (int i = 0; i < 400; i++) {
			try {
				self().createPartition(currentDate, SkinPriceType.RAW);
			} catch (Exception e){
				if(e.getMessage().contains("already exists")){
					logger.warn("status=partition-already-exists, msg={}", e.getMessage());
					continue;
				}
				logger.error("status=creation-failed", e);
				throw e;
			}
			currentDate = currentDate.plusDays(1);
		}
	}

	protected SkinPriceByDayService self(){
		return ApplicationContextProvider.context().getBean(getClass());
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void createPartition(LocalDate date, SkinPriceType type){
		skinPriceByDayDAO.createPartition(date, type);
	}
}

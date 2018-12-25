package com.mageddo.sqldatapartitioning.job;

import com.mageddo.sqldatapartitioning.service.SkinPriceByDayService;
import com.mageddo.sqldatapartitioning.service.SkinPriceService;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

@Component
public class PartitionCreatorJob extends AbstractJob {

	private final SkinPriceService skinPriceService;
	private final SkinPriceByDayService skinPriceByDayService;

	public PartitionCreatorJob(SkinPriceService skinPriceService, SkinPriceByDayService skinPriceByDayService) {
		this.skinPriceService = skinPriceService;
		this.skinPriceByDayService = skinPriceByDayService;
	}

	@Override
	public void configure(ScheduledTaskRegistrar registrar) {
		registrar.addTriggerTask(
			() -> {
				skinPriceService.createPartitions();
				skinPriceByDayService.createPartitions();
			},
			new CronTrigger("0 */1 * * * *")
//			new CronTrigger("0 0 */12 * * *")
		);
	}
}

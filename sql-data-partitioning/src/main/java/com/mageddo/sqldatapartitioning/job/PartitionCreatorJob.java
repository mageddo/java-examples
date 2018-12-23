package com.mageddo.sqldatapartitioning.job;

import com.mageddo.sqldatapartitioning.service.SkinPriceService;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

@Component
public class PartitionCreatorJob extends AbstractJob {

	private final SkinPriceService skinPriceService;

	public PartitionCreatorJob(SkinPriceService skinPriceService) {
		this.skinPriceService = skinPriceService;
	}

	@Override
	public void configure(ScheduledTaskRegistrar registrar) {
		registrar.addTriggerTask(
			skinPriceService::createPartitions,
			new CronTrigger("0 */10 * * * *")
//			new CronTrigger("0 0 */12 * * *")
		);
	}
}

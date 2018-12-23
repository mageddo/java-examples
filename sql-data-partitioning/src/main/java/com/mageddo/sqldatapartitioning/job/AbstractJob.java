package com.mageddo.sqldatapartitioning.job;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

public abstract class AbstractJob implements SchedulingConfigurer {

	@Value("${spring.schedule.enabled:false}")
	private boolean scheduleEnabled;

	public boolean isScheduleEnabled() {
		return scheduleEnabled;
	}

	@Override
	public final void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		if(isScheduleEnabled()){
			configure(taskRegistrar);
		}
	}

	public abstract void configure(ScheduledTaskRegistrar registrar);
}

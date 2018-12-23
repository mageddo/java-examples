package com.mageddo.sqldatapartitioning;

import com.mageddo.sqldatapartitioning.controller.converter.LocalDateAnnotationFormatterFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.format.FormatterRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableTransactionManagement
@SpringBootApplication
public class DataPartitioningStarter implements WebMvcConfigurer {

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addFormatterForFieldAnnotation(new LocalDateAnnotationFormatterFactory());
	}

	public static void main(String[] args) {
		SpringApplication.run(DataPartitioningStarter.class, args);
	}
}

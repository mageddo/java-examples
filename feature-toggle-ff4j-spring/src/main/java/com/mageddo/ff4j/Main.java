package com.mageddo.ff4j;

import org.ff4j.FF4j;
import org.ff4j.audit.repository.InMemoryEventRepository;
import org.ff4j.core.Feature;
import org.ff4j.store.JdbcFeatureStore;
import org.ff4j.web.FF4jDispatcherServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@SpringBootApplication
public class Main {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

	@Bean
	public FF4j ff4j(DataSource dataSource) {
		final JdbcFeatureStore featureStore = new JdbcFeatureStore(dataSource);
		featureStore.createSchema();

		final FF4j ff4j = new FF4j();
		ff4j.setFeatureStore(featureStore);
		ff4j.autoCreate(true);
    ff4j.setEventRepository(new InMemoryEventRepository());
    ff4j.audit(true);

		for (final FeatureSwitch featureSwitch : FeatureSwitch.values()) {
			if (!featureStore.exist(featureSwitch.name())) {
				featureStore.create(new Feature(
					featureSwitch.name(),
					featureSwitch.isEnabledByDefault(),
					featureSwitch.getLabel()));
			}
		}

		FeatureSwitch.setFf4j(ff4j);
		return ff4j;
	}

	@Bean
	public ServletRegistrationBean<FF4jDispatcherServlet> ff4jConsoleServlet() {
		final ServletRegistrationBean<FF4jDispatcherServlet> registration =
			new ServletRegistrationBean<>(new FF4jDispatcherServlet(), "/actuator/features/*");
		registration.addInitParameter("ff4jProvider", SpringFf4jProvider.class.getName());
		registration.setName("ff4j-console");
		return registration;
	}

}

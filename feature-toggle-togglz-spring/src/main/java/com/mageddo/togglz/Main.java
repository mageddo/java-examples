package com.mageddo.togglz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.togglz.core.manager.FeatureManager;
import org.togglz.core.manager.FeatureManagerBuilder;
import org.togglz.core.repository.cache.CachingStateRepository;
import org.togglz.core.repository.jdbc.JDBCStateRepository;
import org.togglz.core.user.NoOpUserProvider;

import javax.sql.DataSource;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class Main {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

	@Bean
	public FeatureManager featureManager(DataSource dataSource){
		return FeatureManagerBuilder
			.begin()
			.featureEnum(FeatureSwitch.class)
			.stateRepository(new CachingStateRepository(
				JDBCStateRepository.newBuilder(dataSource).build(),
				60, TimeUnit.SECONDS))
			.userProvider(new NoOpUserProvider())
			//.activationStrategy(new GradualActivationStrategy())
			.build();
	}

}

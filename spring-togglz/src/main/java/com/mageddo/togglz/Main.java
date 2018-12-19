package com.mageddo.togglz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.togglz.core.manager.FeatureManager;
import org.togglz.core.manager.FeatureManagerBuilder;
import org.togglz.core.repository.mem.InMemoryStateRepository;
import org.togglz.core.user.NoOpUserProvider;

@SpringBootApplication
@EnableAutoConfiguration
public class Main {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

	@Bean
	public FeatureManager featureManager(){
		return FeatureManagerBuilder
			.begin()
			.featureEnum(FeatureSwitch.class)
			.stateRepository(new InMemoryStateRepository())
			.userProvider(new NoOpUserProvider())
			//.activationStrategy(new GradualActivationStrategy())
			.build();
	}

}

package com.mageddo.togglz;

import org.togglz.core.repository.CachingStateRepository;
import org.togglz.core.DefaultUserFeatureManager;
import org.togglz.core.repository.JDBCStreamedStateRepository;
import org.togglz.core.repository.UserSwitchStateRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.togglz.core.activation.DefaultActivationStrategyProvider;
import org.togglz.core.manager.EnumBasedFeatureProvider;
import org.togglz.core.manager.FeatureManager;
import org.togglz.core.user.NoOpUserProvider;

import javax.sql.DataSource;

@SpringBootApplication
@EnableTransactionManagement(proxyTargetClass = true)
public class UserTogglzMain {

	public static void main(String[] args) {
		SpringApplication.run(UserTogglzMain.class, args);
	}

	@Bean
	public FeatureManager featureManager(DataSource dataSource){
		return new DefaultUserFeatureManager(
			new EnumBasedFeatureProvider(FeatureSwitch.class),
			new UserSwitchStateRepository(new CachingStateRepository(new JDBCStreamedStateRepository(
				dataSource, "TGZ_FEATURE", "TGZ_FEATURE_PARAMETER"
			))),
			new NoOpUserProvider(),
			new DefaultActivationStrategyProvider()
		);
	}

}

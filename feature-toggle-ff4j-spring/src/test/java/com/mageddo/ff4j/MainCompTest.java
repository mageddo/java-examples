package com.mageddo.ff4j;

import org.ff4j.FF4j;
import org.ff4j.core.Feature;
import org.ff4j.store.JdbcFeatureStore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MainCompTest {

	@Autowired
	FF4j ff4j;

	@Autowired
	DataSource dataSource;

	@AfterEach
	void resetFeatureTwo() {
		this.ff4j.disable(FeatureSwitch.FEATURE_TWO.name());
	}

	@Test
	void myFirstJobIsActiveByDefault() {
		assertThat(FeatureSwitch.MY_FIRST_JOB.isActive()).isTrue();
	}

	@Test
	void featureTwoIsInactiveByDefault() {
		assertThat(FeatureSwitch.FEATURE_TWO.isActive()).isFalse();
	}

	@Test
	void togglingFeatureStateReflectsOnFeatureSwitch() {
		this.ff4j.enable(FeatureSwitch.FEATURE_TWO.name());
		FeatureSwitch.FEATURE_TWO.setValue("blue");

		assertThat(FeatureSwitch.FEATURE_TWO.isActive()).isTrue();
		assertThat(FeatureSwitch.FEATURE_TWO.getValue()).isEqualTo("blue");
	}

	@Test
	void togglingFeatureStateIsPersistedOnJdbcRepository() {
		this.ff4j.enable(FeatureSwitch.FEATURE_TWO.name());
		FeatureSwitch.FEATURE_TWO.setValue("green");

		final var freshStore = new JdbcFeatureStore(this.dataSource);
		final Feature persisted = freshStore.read(FeatureSwitch.FEATURE_TWO.name());

		assertThat(persisted.isEnable()).isTrue();
		assertThat(persisted.getProperty(FeatureSwitch.VALUE_PARAMETER).asString()).isEqualTo("green");
	}
}

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
		FeatureSwitch.FEATURE_TWO.setValue("true");

		assertThat(FeatureSwitch.FEATURE_TWO.isActive()).isTrue();
		assertThat(FeatureSwitch.FEATURE_TWO.getValue()).isEqualTo("true");

		FeatureSwitch.FEATURE_TWO.setValue("false");

		assertThat(FeatureSwitch.FEATURE_TWO.isActive()).isFalse();
		assertThat(FeatureSwitch.FEATURE_TWO.getValue()).isEqualTo("false");
	}

	@Test
	void togglingFeatureStateIsPersistedOnJdbcRepository() {
		FeatureSwitch.FEATURE_TWO.setValue("true");

		final var freshStore = new JdbcFeatureStore(this.dataSource);
		final Feature persisted = freshStore.read(FeatureSwitch.FEATURE_TWO.name());

		assertThat(persisted.isEnable()).isTrue();
		assertThat(persisted.getProperty(FeatureSwitch.VALUE_PARAMETER).asString()).isEqualTo("true");
	}
}

package com.mageddo.togglz;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.togglz.core.manager.FeatureManager;
import org.togglz.core.repository.FeatureState;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MainCompTest {

	@Autowired
	FeatureManager featureManager;

	@AfterEach
	void resetFeatureTwo() {
		this.featureManager.setFeatureState(new FeatureState(FeatureSwitch.FEATURE_TWO, false));
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
		final var state = new FeatureState(FeatureSwitch.FEATURE_TWO, true)
			.setParameter(FeatureSwitch.VALUE_PARAMETER, "blue");
		this.featureManager.setFeatureState(state);

		assertThat(FeatureSwitch.FEATURE_TWO.isActive()).isTrue();
		assertThat(FeatureSwitch.FEATURE_TWO.getValue()).isEqualTo("blue");
	}
}

package org.togglz.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.togglz.core.activation.ActivationStrategyProvider;
import org.togglz.core.manager.FeatureManager;
import org.togglz.core.manager.FeatureManagerBuilder;
import org.togglz.core.metadata.FeatureMetaData;
import org.togglz.core.repository.FeatureState;
import org.togglz.core.repository.StateRepository;
import org.togglz.core.spi.ActivationStrategy;
import org.togglz.core.spi.FeatureProvider;
import org.togglz.core.user.FeatureUser;
import org.togglz.core.user.UserProvider;
import org.togglz.core.util.Validate;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class DefaultUserFeatureManager implements UserFeatureManager {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final String name;
	private final StateRepository stateRepository;
	private final FeatureManager featureManager;

	public DefaultUserFeatureManager(
		FeatureProvider featureProvider, StateRepository stateRepository,
		UserProvider userProvider, ActivationStrategyProvider activationStrategyProvider
	) {
		this.name = "DefaultUserFeatureManager";
		this.stateRepository = stateRepository;
		featureManager = FeatureManagerBuilder
			.begin()
			.featureProvider(featureProvider)
			.stateRepository(stateRepository)
			.userProvider(userProvider)
			.activationStrategyProvider(activationStrategyProvider)
			.build();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Set<Feature> getFeatures() {
		return featureManager.getFeatures();
	}

	@Override
	public FeatureMetaData getMetaData(Feature feature) {
		return featureManager.getMetaData(feature);
	}

	@Override
	public boolean isActive(Feature feature) {
		return featureManager.isActive(feature);
	}

	@Override
	public boolean isActive(Feature feature, FeatureUser user) {
		Validate.notNull(feature, "feature is required");

		final FeatureState globalSate = Optional
			.ofNullable(getFeatureState(feature))
			.orElse(getMetaData(feature).getDefaultFeatureState());

		if (!globalSate.isEnabled()) {
			return false;
		}

		final String strategyId = globalSate.getStrategyId();
		if (strategyId == null || strategyId.isEmpty()) {
			return true;
		}

		// check the selected strategy
		for (final ActivationStrategy strategy : featureManager.getActivationStrategies()) {
			if (strategy.getId().equalsIgnoreCase(strategyId)) {
				return strategy.isActive(globalSate, user);
			}
		}
		logger.debug("status=strategy-not-found, feature={}, strategy={}", feature.name(), strategyId);
		return false;
	}

	@Override
	public FeatureUser getCurrentFeatureUser() {
		return featureManager.getCurrentFeatureUser();
	}

	@Override
	public FeatureState getFeatureState(Feature feature) {
		return featureManager.getFeatureState(feature);
	}

	@Override
	public void setFeatureState(FeatureState state) {
		featureManager.setFeatureState(state);
	}

	@Override
	public List<ActivationStrategy> getActivationStrategies() {
		return featureManager.getActivationStrategies();
	}


}

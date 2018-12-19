package org.togglz.core.repository;

import org.togglz.core.Feature;
import org.togglz.core.strategy.StreamedUserSwitchActivationStrategy;

public class UserSwitchStateRepository implements StateRepository {

	private final StateRepository stateRepository;

	public UserSwitchStateRepository(StateRepository stateRepository) {
		this.stateRepository = stateRepository;
	}

	@Override
	public FeatureState getFeatureState(Feature feature) {
		return stateRepository.getFeatureState(feature);
	}

	@Override
	public void setFeatureState(FeatureState featureState) {
		if(featureState.getParameterNames().isEmpty()){
			stateRepository.setFeatureState(featureState);
		}
		for (final String parameterName : featureState.getParameterNames()) {
			final FeatureState tmpState = copy(featureState);
			if(parameterName.equalsIgnoreCase(StreamedUserSwitchActivationStrategy.ADD_PARAM_KEY)){
				tmpState.setParameter(StreamedUserSwitchActivationStrategy.getUserActivationKey(featureState.getParameter(parameterName)), StreamedUserSwitchActivationStrategy.ACTIVATED_USER_VAL);
			} else  if(parameterName.equalsIgnoreCase(StreamedUserSwitchActivationStrategy.REMOVE_PARAM_KEY)){
				tmpState.setParameter(StreamedUserSwitchActivationStrategy.getUserActivationKey(featureState.getParameter(parameterName)), StreamedUserSwitchActivationStrategy.DEACTIVATED_USER_VAL);
			} else {
				tmpState.setParameter(parameterName, featureState.getParameter(parameterName));
			}
			stateRepository.setFeatureState(tmpState);
		}
	}

	FeatureState copy(FeatureState featureState) {
		final FeatureState tmpState = new FeatureState(featureState.getFeature(), featureState.isEnabled());
		tmpState.setStrategyId(featureState.getStrategyId());
		return tmpState;
	}
}

package org.togglz.core.strategy;

import org.togglz.core.activation.Parameter;
import org.togglz.core.activation.ParameterBuilder;
import org.togglz.core.repository.FeatureState;
import org.togglz.core.spi.ActivationStrategy;
import org.togglz.core.user.FeatureUser;

import java.util.Objects;

public class StreamedUserSwitchActivationStrategy implements ActivationStrategy {

	public static final String RESTRICTED_FEATURE_PARAM = "sw-state";
	public static final String ADD_PARAM_KEY = "sw-add";
	public static final String REMOVE_PARAM_KEY = "sw-remove";

	public static final String ACTIVATED_USER_VAL = "1";
	public static final String DEACTIVATED_USER_VAL = "0";
	public static final String RESTRICTED_FEATURE_VAL = "restricted";

	@Override
	public String getId() {
		return "StreamedUserSwitchActivationStrategy";
	}

	@Override
	public String getName() {
		return "Streamed User Activation By Name";
	}

	@Override
	public boolean isActive(FeatureState state, FeatureUser user) {
		if(!RESTRICTED_FEATURE_VAL.equalsIgnoreCase(state.getParameter(RESTRICTED_FEATURE_PARAM))){
			return true;
		}
		if(user == null){
			return false;
		}
		final String val = state.getParameter(getUserActivationKey(user.getName()));
		return Objects.equals(ACTIVATED_USER_VAL, val);
	}

	public static String getUserActivationKey(String user) {
		return String.format("%s-sw", user);
	}

	@Override
	public Parameter[] getParameters() {
		return new Parameter[]{
			ParameterBuilder
				.create(ADD_PARAM_KEY)
				.optional()
				.label("Add User")
				.description("Provide unique user name/id to activate this feature"),
			ParameterBuilder.create(REMOVE_PARAM_KEY)
			.optional()
			.label("Remove User")
			.description("Provide unique user name/id to deactivate this feature"),
			ParameterBuilder
				.create(RESTRICTED_FEATURE_PARAM)
				.label("restriction")
				.matching("(?:restricted|active)")
				.description("Whether feature is active for all users (active) or restricted to specified users (restricted)")
		};
	}

}

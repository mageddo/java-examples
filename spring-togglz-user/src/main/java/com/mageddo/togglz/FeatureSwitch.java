package com.mageddo.togglz;

import org.togglz.core.UserFeatureContext;
import org.togglz.core.UserFeatureManager;
import org.togglz.core.Feature;
import org.togglz.core.annotation.EnabledByDefault;
import org.togglz.core.annotation.Label;
import org.togglz.core.repository.FeatureState;
import org.togglz.core.user.SimpleFeatureUser;

public enum FeatureSwitch implements Feature {

	@EnabledByDefault
	@AutomaticWithdrawGroup
	AUTOMATIC_WITHDRAW,

	@Label("Second Feature")
	FEATURE_TWO;

	public static final String VALUE_PARAMETER = "value";

	public boolean isActive() {
		return fm().isActive(this);
	}

	public boolean isActive(String user){
		return fm().isActive(this, new SimpleFeatureUser(user));
	}

	public String getValue() {
		return fm().getFeatureState(this).getParameter(VALUE_PARAMETER);
	}

	public void setValue(String value) {
		final FeatureState fs = fm().getFeatureState(this);
		fs.setParameter(VALUE_PARAMETER, value);
		fm().setFeatureState(fs);
	}

	private UserFeatureManager fm() {
		return UserFeatureContext.getFeatureManager();
	}
}

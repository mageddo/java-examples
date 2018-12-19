package com.mageddo.togglz;

import org.togglz.core.Feature;
import org.togglz.core.annotation.EnabledByDefault;
import org.togglz.core.annotation.Label;
import org.togglz.core.context.FeatureContext;
import org.togglz.core.manager.FeatureManager;
import org.togglz.core.repository.FeatureState;

public enum FeatureSwitch implements Feature {

	@EnabledByDefault
	@Label("My very first job")
	MY_FIRST_JOB,

	@Label("Second Feature")
	FEATURE_TWO;

	public static final String VALUE_PARAMETER = "value";

	public boolean isActive() {
		return fm().isActive(this);
	}

	public boolean isActive(String user){
		return fm().isActive(this);
	}

	public String getValue() {
		return fm().getFeatureState(this).getParameter(VALUE_PARAMETER);
	}

	public void setValue(String value) {
		final FeatureState fs = new FeatureState(this);
		fs.setParameter(VALUE_PARAMETER, value);
		fm().setFeatureState(fs);
	}


	private FeatureManager fm() {
		return FeatureContext.getFeatureManager();
	}
}

package org.togglz.core;

import org.togglz.core.manager.FeatureManager;

import static org.togglz.core.context.FeatureContext.getFeatureManagerOrNull;

public class UserFeatureContext {
	public static UserFeatureManager getFeatureManager() {
		FeatureManager manager = getFeatureManagerOrNull();
		if (manager != null) {
			if(manager instanceof UserFeatureManager){
				return (UserFeatureManager) manager;
			}
			throw new IllegalArgumentException("Must be a UserFeatureManager");
		}
		throw new IllegalStateException("Could not find the FeatureManager. " +
			"For web applications please verify that the TogglzFilter starts up correctly. " +
			"In other deployment scenarios you will typically have to implement a FeatureManagerProvider " +
			"as described in the 'Advanced Configuration' chapter of the documentation.");
	}
}

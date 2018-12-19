package org.togglz.core;

import org.togglz.core.Feature;
import org.togglz.core.manager.FeatureManager;
import org.togglz.core.user.FeatureUser;

public interface UserFeatureManager extends FeatureManager {
	boolean isActive(Feature feature, FeatureUser user);
}

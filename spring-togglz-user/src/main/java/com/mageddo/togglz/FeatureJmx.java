package com.mageddo.togglz;

import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

import java.util.Objects;

@ManagedResource
@Component
public class FeatureJmx {

	@ManagedOperation
	public boolean isActive(String feature){
		return getFeature(feature).isActive();
	}

	@ManagedOperation
	public void update(String feature, String value){
		getFeature(feature).setValue(value);
	}

	@ManagedOperation
	public String getValue(String name){
		return getFeature(name).getValue();
	}

	@ManagedOperation
	public boolean isActive(String feature, String user){
		return getFeature(feature).isActive(user);
	}

	@ManagedOperation
	public void update(String feature, String user, String value){
		getFeature(feature).setValue(user);
	}

	@ManagedOperation
	public String getValue(String name, String user){
		return getFeature(name).getValue();
	}

	private FeatureSwitch getFeature(String name) {
		return Objects.requireNonNull(FeatureSwitch.valueOf(name));
	}
}

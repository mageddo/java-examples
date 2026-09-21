package com.mageddo.togglz;

import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

import java.util.Objects;

@ManagedResource
@Component
public class FeatureJmx {

	@ManagedOperation
	public boolean isActive(String name){
		return getFeature(name).isActive();
	}

	@ManagedOperation
	public void updateFeature(String name, String value){
		FeatureSwitch.valueOf(name).setValue(value);
	}

	@ManagedOperation
	public String getFeatureValue(String name){
		return getFeature(name).getValue();
	}

	private FeatureSwitch getFeature(String name) {
		return Objects.requireNonNull(FeatureSwitch.valueOf(name));
	}
}

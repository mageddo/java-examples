package com.mageddo.togglz;

import org.togglz.core.annotation.FeatureGroup;
import org.togglz.core.annotation.Label;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Label("Automatic Withdraw")
@FeatureGroup
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AutomaticWithdrawGroup {
}

package com.mageddo.ff4j;

import org.apache.commons.lang3.StringUtils;
import org.ff4j.FF4j;
import org.ff4j.core.Feature;
import org.ff4j.property.Property;
import org.ff4j.property.PropertyString;

public enum FeatureSwitch {

  MY_FIRST_JOB(true, "My very first job"),

  FEATURE_TWO(false, "Second Feature");

  public static final String VALUE_PARAMETER = "value";

  private static FF4j ff4j;

  private final boolean enabledByDefault;
  private final String label;

  FeatureSwitch(boolean enabledByDefault, String label) {
    this.enabledByDefault = enabledByDefault;
    this.label = label;
  }

  public boolean isEnabledByDefault() {
    return this.enabledByDefault;
  }

  public String getLabel() {
    return this.label;
  }

  public boolean isActive() {
    return ff4j().check(this.name());
  }

  public boolean isActive(String user) {
    return ff4j().check(this.name());
  }

  public String getValue() {
    final Property<?> property = ff4j()
        .getFeature(this.name())
        .getProperty(VALUE_PARAMETER);
    return property == null ? null : property.asString();
  }

  public void setValue(String value) {
    final Feature feature = ff4j().getFeature(this.name());
    feature.addProperty(new PropertyString(VALUE_PARAMETER, value));
    feature.setEnable(Boolean.parseBoolean(value));
    ff4j()
        .getFeatureStore()
        .update(feature);
  }

  static FF4j ff4j() {
    return ff4j;
  }

  static void setFf4j(FF4j ff4j) {
    FeatureSwitch.ff4j = ff4j;
  }
}

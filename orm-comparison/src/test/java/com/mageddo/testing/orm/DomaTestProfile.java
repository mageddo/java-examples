package com.mageddo.testing.orm;

import java.util.Map;

import io.quarkus.test.junit.QuarkusTestProfile;

public class DomaTestProfile implements QuarkusTestProfile {

  @Override
  public Map<String, String> getConfigOverrides() {
    return Map.of("orm.provider", "doma");
  }
}

package com.mageddo.springexamples.ex01;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "root")
public class Props {

  private final Map<String, Object> props = new HashMap<>();

  public Map<String, Object> getProps() {
    return this.props;
  }

}

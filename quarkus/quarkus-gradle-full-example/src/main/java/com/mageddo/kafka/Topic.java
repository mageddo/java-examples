package com.mageddo.kafka;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.thymeleaf.util.Validate;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder(builderClassName = "TopicBuilder")
public class Topic {

  @NonNull String name;

  @NonNull String dlt;

  @NonNull Integer consumers;

  Duration interval;

  @NonNull Duration maxInterval;

  @NonNull Integer maxTries;

  Map<String, Object> props;
  String groupId;

  boolean autoConfigure = true;

  public static class TopicBuilder {

    public TopicBuilder name(String name) {
      this.name = name;
      dlt(String.format("%s_DLQ", name));
      return this;
    }

    public TopicBuilder autoGroupId() {
      Validate.notNull(this.name, "must set topic name first");
      final String groupIdPrefix = System.getProperty("kafka.group.id.prefix", "group");
      this.groupId = String.format("%s_%s", groupIdPrefix, this.name);
      return this;
    }

    public TopicBuilder interval(Duration interval) {
      this.interval = interval;
      maxInterval(max(maxInterval, interval));
      return this;
    }

    public TopicBuilder maxInterval(Duration maxInterval) {
      this.maxInterval = max(maxInterval, this.interval);
      return this;
    }

    private Duration max(Duration a, Duration b) {
      return ObjectUtils.max(a, b);
    }

  }

  public static class MapBuilder {
    private final Map<String, Object> map;

    public MapBuilder(Map<String, Object> map) {
      this.map = map;
    }

    public static MapBuilder map() {
      return new MapBuilder(new HashMap<>());
    }

    public MapBuilder prop(String k, Object v) {
      this.map.put(k, v);
      return this;
    }

    public Map<String, Object> get() {
      return this.map;
    }
  }

}

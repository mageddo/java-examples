package com.mageddo.jackson;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class JsonUtils {

  public static final ObjectMapper instance = objectMapper();
  public static final ObjectMapper prettyInstance = objectMapper()
      .enable(SerializationFeature.INDENT_OUTPUT);

  public static ObjectMapper notAutoClosableMapper() {
    return objectMapper()
        .disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
  }

  public static ObjectMapper objectMapper() {
    final SimpleModule m = new SimpleModule();
    return new ObjectMapper()
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        .registerModule(m);
  }

  public static ObjectMapper setup(boolean production) {
    if (production) {
      instance.disable(SerializationFeature.INDENT_OUTPUT);
    }
    return instance;
  }

  public static JsonNode readTree(InputStream in) {
    try {
      return instance.readTree(in);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static JsonNode readTree(String o) {
    try {
      return instance.readTree(o);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static String writeValueAsString(Object o) {
    try {
      return instance.writeValueAsString(o);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public static <T> T readValue(String value, TypeReference<T> t) {
    try {
      return instance.readValue(value, t);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static <T> T readValue(JsonParser jsonParser, TypeReference<T> t) {
    try {
      return instance.readValue(jsonParser, t);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static String prettify(String json) {
    try {
      return prettyInstance.writeValueAsString(instance.readTree(json));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static <T> T readValue(InputStream in, Class<T> o) {
    try {
      return instance.readValue(in, o);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  public static <T> T readValue(JsonParser data, Class<T> clazz) {
    try {
      return instance.readValue(data, clazz);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }
}

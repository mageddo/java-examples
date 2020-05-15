package com.mageddo.thymeleaf;

import java.util.Map;

import org.thymeleaf.context.Context;

public final class Thymeleafs {

  private static final ThymeleafTemplate instance = new ThymeleafTemplate();

  private Thymeleafs() {
  }

  public static String fromPath(String templateName, Map<String, Object> variables) {
    return instance.fromPath(templateName, variables);
  }

  public static String fromPath(String templateName, Context context) {
    return instance.fromPath(templateName, context);
  }

  public static String from(String template, Map<String, Object> variables) {
    return instance.fromPath(template, variables);
  }

  public static String from(String template, Context context) {
    return instance.fromPath(template, context);
  }
}

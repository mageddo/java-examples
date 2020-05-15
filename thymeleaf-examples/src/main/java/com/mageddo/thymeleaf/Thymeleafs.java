package com.mageddo.thymeleaf;

import java.util.Map;

import org.thymeleaf.context.Context;

public final class Thymeleafs {

  private static final ThymeleafTemplate instance = new ThymeleafTemplate();

  private Thymeleafs() {
  }

  public static String processFromPath(String templateName, Map<String, Object> variables) {
    return instance.processFromPath(templateName, variables);
  }

  public static String processFromPath(String templateName, Context context) {
    return instance.processFromPath(templateName, context);
  }

  public static String process(String template, Map<String, Object> variables) {
    return instance.processFromPath(template, variables);
  }

  public static String process(String template, Context context) {
    return instance.processFromPath(template, context);
  }
}

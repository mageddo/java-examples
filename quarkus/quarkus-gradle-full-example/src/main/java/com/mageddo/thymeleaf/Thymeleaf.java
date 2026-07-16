package com.mageddo.thymeleaf;

import java.io.StringWriter;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import lombok.Builder;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Builder
public class Thymeleaf {

  private final TemplateEngine templateEngine;
  private final List<ContextFiller> contextFillers;

  public String fromPath(String templateName, Map<String, Object> variables) {
    return this.fromPath(templateName, this.createContext(variables));
  }

  public String fromPath(String templateName, Context context) {
    return this.from(templateName, context);
  }

  public String from(String template) {
    return from(template, Map.of());
  }

  public String from(String template, Map<String, Object> variables) {
    return from(template, this.createContext(variables));
  }

  public String from(String template, Context context) {
    StringWriter stringWriter = new StringWriter();
    this.templateEngine.process(template, context, stringWriter);
    return stringWriter.toString();
  }

  Context createContext(Map<String, Object> variables) {
    final var ctx = new Context(Locale.getDefault(), variables);
    this.fillVariables(ctx);
    return ctx;
  }

  private void fillVariables(Context ctx) {
    this.contextFillers.forEach(filler -> {
      filler.fill(ctx);
    });
  }

}

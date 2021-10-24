package com.mageddo.thymeleaf;

import java.io.StringWriter;
import java.util.Locale;
import java.util.Map;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

/**
 * Version 1.1
 */
public class Thymeleaf {

  private TemplateEngine templateEngine;

  public Thymeleaf() {
    this(true);
  }

  public Thymeleaf(boolean cacheable) {
    this.templateEngine = new TemplateEngine();
    final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
    templateResolver.setCacheable(cacheable);
    templateEngine.setTemplateResolver(templateResolver);
  }

  public String fromPath(String templateName, Map<String, Object> variables) {
    return this.fromPath(templateName, new Context(Locale.getDefault(), variables));
  }

  public String fromPath(String templateName, Context context) {
    return this.from(templateName, context);
  }

  public String from(String template) {
    return from(template, Map.of());
  }

  public String from(String template, Map<String, Object> variables) {
    return from(template, new Context(Locale.getDefault(), variables));
  }

  public String from(String template, Context context) {
    StringWriter stringWriter = new StringWriter();
    this.templateEngine.process(template, context, stringWriter);
    return stringWriter.toString();
  }

}

package com.mageddo.thymeleaf;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

public class ThymeleafTemplateBuilder {
  public static TemplateEngine build(boolean cacheable) {
    final var templateEngine = new TemplateEngine();
    final var templateResolver = new ClassLoaderTemplateResolver();
    templateResolver.setCacheable(cacheable);
    templateEngine.setTemplateResolver(templateResolver);
    return templateEngine;
  }
}

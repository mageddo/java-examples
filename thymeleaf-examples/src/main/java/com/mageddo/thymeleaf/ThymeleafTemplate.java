package com.mageddo.thymeleaf;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.util.Locale;
import java.util.Map;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

public class ThymeleafTemplate {

  private TemplateEngine templateEngine;

  public ThymeleafTemplate() {
    this.init();
  }

  private void init() {
    this.templateEngine = new TemplateEngine();
    StringTemplateResolver templateResolver = new StringTemplateResolver();
    templateResolver.setTemplateMode(TemplateMode.HTML);
    templateEngine.setTemplateResolver(templateResolver);
  }

  public String processFromPath(String templateName, Map<String, Object> variables) {
    return this.processFromPath(templateName, new Context(Locale.getDefault(), variables));
  }

  public String processFromPath(String templateName, Context context) {
    return this.process(resourceToString(templateName), context);
  }

  public String process(String template, Map<String, Object> variables) {
    return process(template, new Context(Locale.getDefault(), variables));
  }

  public String process(String template, Context context) {
    StringWriter stringWriter = new StringWriter();
    this.templateEngine.process(template, context, stringWriter);
    return stringWriter.toString();
  }

  private String resourceToString(String resourcePath) {
    try {
      final StringBuilder sb = new StringBuilder();
      final byte[] buff = new byte[128];
      try(InputStream in = getClass().getResourceAsStream(resourcePath)){
        for (;;){
          final int read = in.read(buff);
          if(read == -1){
            break;
          }
          for (int i = 0; i < read; i++) {
            sb.append((char)buff[i]);
          }
        }
        return sb.toString();
      }
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}

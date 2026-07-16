package com.mageddo.thymeleaf;

import io.quarkus.runtime.Quarkus;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Produces;

import org.eclipse.microprofile.config.inject.ConfigProperty;

public class ThymeleafConfig {

  @Singleton
  @Produces
  public Thymeleaf thymeleaf(
      Instance<ContextFiller> contextFillers,
      @ConfigProperty(name = "thymeleaf.cache.enabled", defaultValue = "false") boolean cacheable

  ) {
    return Thymeleaf
        .builder()
        .templateEngine(ThymeleafTemplateBuilder.build(cacheable))
        .contextFillers(contextFillers.stream()
            .toList()
        )
        .build();
  }

}

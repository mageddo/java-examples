package com.mageddo.thymeleaf.quarkushotswapfix;

import com.mageddo.reflection.FieldUtils;

import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.configuration.ProfileManager;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ognl.OgnlContext;

import javax.enterprise.event.Observes;

@Slf4j
public class QuarkusConfig {
  @SneakyThrows
  public void init(@Observes StartupEvent event) {
    if (!this.inDevMode()) {
      return;
    }
    log.warn("status=fixing-thymelaf-classloading-at-quakus-hot-swap");
    FieldUtils.writeStaticField(
        OgnlContext.class.getField("DEFAULT_CLASS_RESOLVER"),
        new HotSwapClassResolver()
    );
  }

  private boolean inDevMode() {
    return ProfileManager
        .getActiveProfile()
        .contains("dev");
  }
}

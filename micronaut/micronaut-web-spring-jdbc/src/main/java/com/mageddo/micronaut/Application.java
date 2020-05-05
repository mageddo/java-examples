package com.mageddo.micronaut;

import com.mageddo.micronaut.config.ApplicationContextUtils;
import com.mageddo.micronaut.embeddedpostgres.Postgres;

import io.micronaut.runtime.Micronaut;
import io.micronaut.runtime.event.annotation.EventListener;
import io.micronaut.runtime.server.event.ServerShutdownEvent;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;
import lombok.SneakyThrows;

public class Application {

  private static EmbeddedPostgres POSTGRES_INSTANCE;

  public static void main(String[] args) {
    POSTGRES_INSTANCE = Postgres.start();
    Micronaut.run(Application.class);
  }

  @EventListener
  public void onStartup(ServerStartupEvent event) {
    ApplicationContextUtils.context(
        event.getSource()
            .getApplicationContext()
    );
  }

  @SneakyThrows
  @EventListener
  public void onStartup(ServerShutdownEvent event) {
    POSTGRES_INSTANCE.close();
  }
}

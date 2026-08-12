package com.mageddo.testing;


import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DatabaseConfiguratorExtension implements BeforeAllCallback {

  private final List<Consumer<EmbeddedPostgres.Builder>> builderCustomizers =
      new CopyOnWriteArrayList<>();

  @Override
  public void beforeAll(ExtensionContext context) throws Exception {
    log.debug("status=configuring embedded database");
    final var store = this.getStore(context);
    if (store.get(EmbeddedPostgres.class.getName(), EmbeddedPostgres.class) == null) {
      setNewInstanceOnContext(store);
    }
  }

  ExtensionContext.Store getStore(ExtensionContext context) {
    return context
        .getRoot()
        .getStore(ExtensionContext.Namespace.GLOBAL);
  }

  void setNewInstanceOnContext(ExtensionContext.Store store) throws IOException {
    this.customize(customizer -> {
      customizer.setPort(5430);
    });
    final EmbeddedPostgres instance = this.pg();
    store.put(EmbeddedPostgres.class.getName(), instance);
    Runtime
        .getRuntime()
        .addShutdownHook(new Thread(() -> {
          try {
            instance.close();
          } catch (Exception e) {
            throw new RuntimeException(e);
          }
        }));
    log.debug("status=configured");
  }

  EmbeddedPostgres pg() throws IOException {
    final EmbeddedPostgres.Builder builder = EmbeddedPostgres.builder();
    this.builderCustomizers.forEach(c -> c.accept(builder));
    final var postgres = builder.start();
    log.debug("status=postgresStarted");
    return postgres;
  }

  DatabaseConfiguratorExtension customize(Consumer<EmbeddedPostgres.Builder> customizer) {
    this.builderCustomizers.add(customizer);
    return this;
  }

}

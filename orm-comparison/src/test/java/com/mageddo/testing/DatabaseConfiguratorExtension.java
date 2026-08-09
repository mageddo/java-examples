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

  /**
   * A instância guardada é sempre lida como {@link Object}: cada perfil de teste do Quarkus
   * reinicia a aplicação com um classloader novo, então o {@code EmbeddedPostgres} da primeira
   * classe não é do mesmo tipo visto pelas seguintes — só interessa saber se já foi iniciado.
   */
  private static final String POSTGRES_KEY = EmbeddedPostgres.class.getName();

  private final List<Consumer<EmbeddedPostgres.Builder>> builderCustomizers =
      new CopyOnWriteArrayList<>();

  @Override
  public void beforeAll(ExtensionContext context) throws Exception {
    log.debug("status=configuring embedded database");
    final var store = this.getStore(context);
    if (store.get(POSTGRES_KEY) == null) {
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
    store.put(POSTGRES_KEY, instance);
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

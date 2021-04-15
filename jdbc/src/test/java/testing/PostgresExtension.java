package testing;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;

public class PostgresExtension implements BeforeAllCallback {

  private final List<Consumer<EmbeddedPostgres.Builder>> builderCustomizers = new CopyOnWriteArrayList<>();

  @Override
  public void beforeAll(ExtensionContext context) throws Exception {

    final var store = this.getStore(context);
    if (store.get(EmbeddedPostgres.class.getName(), EmbeddedPostgres.class) == null) {
      setNewInstanceOnContext(store);
    }
  }

  private ExtensionContext.Store getStore(ExtensionContext context) {
    return context
        .getRoot()
        .getStore(ExtensionContext.Namespace.GLOBAL);
  }

  private void setNewInstanceOnContext(ExtensionContext.Store store) throws IOException {
    this.customize(customizer -> {
      customizer.setPort(5429);
    });
    final EmbeddedPostgres instance = this.pg();
    store.put(EmbeddedPostgres.class.getName(), instance);
    Runtime.getRuntime()
        .addShutdownHook(new Thread() {
          public void run() {
            try {
              instance.close();
            } catch (Exception e) {
              throw new RuntimeException(e);
            }
          }
        });
  }

  private EmbeddedPostgres pg() throws IOException {
    final EmbeddedPostgres.Builder builder = EmbeddedPostgres.builder();
    this.builderCustomizers.forEach(c -> c.accept(builder));
    return builder.start();
  }

  public PostgresExtension customize(Consumer<EmbeddedPostgres.Builder> customizer) {
    this.builderCustomizers.add(customizer);
    return this;
  }

}

/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package testing;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Store;

import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;
import lombok.SneakyThrows;

public class SingleInstancePostgresExtension implements BeforeAllCallback, AfterAllCallback {

  private final List<Consumer<EmbeddedPostgres.Builder>> builderCustomizers = new CopyOnWriteArrayList<>();

  public SingleInstancePostgresExtension() {
  }

  @Override
  public void beforeAll(ExtensionContext context) throws Exception {
    final Store store = context
        .getRoot()
        .getStore(ExtensionContext.Namespace.GLOBAL);

    if (store.get(EmbeddedPostgres.class.getName(), EmbeddedPostgres.class) == null) {
      setNewInstanceOnContext(store);
    }

  }

  @Override
  public void afterAll(ExtensionContext context) throws Exception {

  }

  private void setNewInstanceOnContext(Store store) throws IOException {
    this.customize(customizer -> {
      customizer.setPort(5631);
    });
    final EmbeddedPostgres instance = this.pg();
    store.put(EmbeddedPostgres.class.getName(), instance);
    Runtime.getRuntime()
        .addShutdownHook(new Thread() {
          @SneakyThrows
          public void run() {
            instance.close();
          }
        });
  }

  private EmbeddedPostgres pg() throws IOException {
    final EmbeddedPostgres.Builder builder = EmbeddedPostgres.builder();
    this.builderCustomizers.forEach(c -> c.accept(builder));
    return builder.start();
  }

  public SingleInstancePostgresExtension customize(Consumer<EmbeddedPostgres.Builder> customizer) {
    this.builderCustomizers.add(customizer);
    return this;
  }

}

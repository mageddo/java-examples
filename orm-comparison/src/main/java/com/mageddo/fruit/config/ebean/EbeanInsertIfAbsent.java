package com.mageddo.fruit.config.ebean;

import java.util.IdentityHashMap;
import java.util.Map;

import io.ebean.Database;
import io.ebean.InsertOptions;
import io.ebean.event.BeanPersistAdapter;
import io.ebean.event.BeanPersistRequest;
import jakarta.inject.Singleton;

/**
 * Insere a entidade se ela ainda não existir, informando se a linha foi de fato criada.
 *
 * O conflito é resolvido pelo banco em um único statement,
 * {@code INSERT ... ON CONFLICT (...) DO NOTHING}, montado pelo próprio Ebean a partir
 * do mapeamento da entidade. Não há find prévio, exceção como fluxo, savepoint nem SQL
 * por tabela, e a transação segue utilizável para as operações seguintes.
 *
 * O Ebean sabe internamente se a linha entrou, mas
 * {@link Database#insert(Object, InsertOptions)} é {@code void}. Quando o conflito é
 * ignorado ele pula o pós-processamento do insert e, com isso, não chama
 * {@link #postInsert(BeanPersistRequest)} — é esse silêncio que serve de sinal aqui.
 *
 * Por depender do momento em que o statement executa, o resultado só é confiável fora
 * do JDBC batch mode: com batch ligado o insert fica enfileirado, o {@code postInsert}
 * só ocorre no flush e o retorno seria um falso negativo.
 */
@Singleton
public class EbeanInsertIfAbsent extends BeanPersistAdapter {

  private static final ThreadLocal<Map<Object, Boolean>> INSERTED =
      ThreadLocal.withInitial(IdentityHashMap::new);

  /**
   * @return true se a linha foi criada, false se já existia
   */
  public boolean execute(Database database, Object bean) {
    final var inserted = INSERTED.get();
    inserted.put(bean, Boolean.FALSE);
    try {
      database.insert(bean, InsertOptions.ON_CONFLICT_NOTHING);
      return inserted.get(bean);
    } finally {
      inserted.remove(bean);
    }
  }

  @Override
  public void postInsert(BeanPersistRequest<?> request) {
    INSERTED
        .get()
        .computeIfPresent(request.bean(), (bean, inserted) -> Boolean.TRUE);
  }

  @Override
  public boolean isRegisterFor(Class<?> cls) {
    return true;
  }
}

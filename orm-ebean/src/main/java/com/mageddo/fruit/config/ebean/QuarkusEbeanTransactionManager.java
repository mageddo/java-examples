package com.mageddo.fruit.config.ebean;

import io.ebean.config.ExternalTransactionManager;
import io.ebean.util.JdbcClose;
import io.ebeaninternal.api.SpiTransaction;
import io.ebeaninternal.server.transaction.JtaTransaction;
import io.ebeaninternal.server.transaction.TransactionManager;
import io.ebeaninternal.server.transaction.TransactionScopeManager;
import jakarta.transaction.Status;
import jakarta.transaction.Synchronization;
import jakarta.transaction.TransactionSynchronizationRegistry;
import jakarta.transaction.UserTransaction;

import java.util.Objects;

/**
 * Faz o Ebean participar da transação Jakarta/Narayana controlada pelo Quarkus.
 *
 * O commit e o rollback continuam sendo responsabilidade do
 * @jakarta.transaction.Transactional.
 */
public final class QuarkusEbeanTransactionManager
    implements ExternalTransactionManager {

  /**
   * O recurso fica associado à transação Narayana atual.
   *
   * Cada instância deste adapter deve pertencer a um único Database Ebean.
   */
  private final Object resourceKey = new Object();

  private final TransactionSynchronizationRegistry registry;
  private final UserTransaction userTransaction;

  private TransactionManager transactionManager;
  private TransactionScopeManager scope;

  public QuarkusEbeanTransactionManager(
      TransactionSynchronizationRegistry registry,
      UserTransaction userTransaction
  ) {
    this.registry = Objects.requireNonNull(registry);
    this.userTransaction = Objects.requireNonNull(userTransaction);
  }

  @Override
  public void setTransactionManager(Object transactionManager) {
    this.transactionManager = (TransactionManager) transactionManager;
    this.scope = this.transactionManager.scope();
  }

  @Override
  public Object getCurrentTransaction() {
    final var status = this.registry.getTransactionStatus();

    if (status == Status.STATUS_NO_TRANSACTION) {
      return this.currentEbeanTransaction();
    }

    if (
        status != Status.STATUS_ACTIVE
            && status != Status.STATUS_MARKED_ROLLBACK
    ) {
      return null;
    }

    final var current = (SpiTransaction) this.registry.getResource(
        this.resourceKey
    );

    if (current != null) {
      /*
       * Importante para os casos de suspensão/retomada da transação,
       * como REQUIRES_NEW.
       */
      if (this.scope.inScope() != current) {
        this.scope.replace(current);
      }
      return current;
    }

    return this.enlistCurrentTransaction();
  }

  private SpiTransaction enlistCurrentTransaction() {
    /*
     * Como já existe uma transação Narayana ativa, o JtaTransaction não
     * inicia outra transação. Ele apenas obtém do Agroal uma Connection
     * enlistada na transação existente.
     */
    final var transaction = new JtaTransaction(
        true,
        this.userTransaction,
        this.transactionManager.dataSource(),
        this.transactionManager
    );

    this.registry.putResource(this.resourceKey, transaction);
    this.registry.registerInterposedSynchronization(
        new EbeanSynchronization(transaction)
    );

    this.scope.replace(transaction);

    return transaction;
  }

  private SpiTransaction currentEbeanTransaction() {
    final var transaction = this.scope.inScope();

    if (transaction == null) {
      return null;
    }

    if (transaction.isActive()) {
      return transaction;
    }

    this.scope.clearExternal();
    return null;
  }

  private final class EbeanSynchronization implements Synchronization {

    private final SpiTransaction transaction;

    private EbeanSynchronization(SpiTransaction transaction) {
      this.transaction = transaction;
    }

    @Override
    public void beforeCompletion() {
      /*
       * Faz flush de batch e executa os callbacks internos do Ebean
       * antes de o Narayana concluir o commit.
       */
      this.transaction.preCommit();
    }

    @Override
    public void afterCompletion(int status) {
      try {
        if (status == Status.STATUS_COMMITTED) {
          this.transaction.postCommit();
        } else {
          this.transaction.postRollback(null);
        }
      } finally {
        this.transaction.deactivateExternal();

        if (scope.inScope() == this.transaction) {
          scope.clearExternal();
        }

        /*
         * Devolve a Connection ao Agroal somente depois de a transação
         * Narayana ter terminado.
         */
        JdbcClose.close(this.transaction.internalConnection());
      }
    }
  }
}

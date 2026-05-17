package com.mageddo.temporal.samplewallet.dataprovider;

import com.mageddo.temporal.samplewallet.domain.Investment;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class InvestmentDaoPg implements InvestmentDAO {

  @Inject
  EntityManager entityManager;

  @Override
  @Transactional
  public void save(Investment investment) {
    this.entityManager.merge(investment);
  }

  @Override
  @Transactional
  public Investment findById(String investmentId) {
    return this.entityManager.find(Investment.class, investmentId);
  }

  @Override
  @Transactional
  public List<Investment> findByWalletId(String walletId) {
    return this.entityManager.createNativeQuery(
        """
          select *
          from investment
          where wallet_id = :walletId
          order by id
          """,
        Investment.class)
      .setParameter("walletId", walletId)
      .getResultList();
  }
}

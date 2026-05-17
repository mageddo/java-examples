package com.mageddo.investment_product.investment.dataprovider;

import com.mageddo.investment_product.investment.Investment;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class InvestmentDaoPg implements InvestmentDAO {

  final EntityManager entityManager;

  @Override
  public void save(Investment investment) {
    this.entityManager.merge(investment);
  }

  @Override
  public Investment findById(String investmentId) {
    return this.entityManager.find(Investment.class, investmentId);
  }

  @Override
  public List<Investment> findByWalletId(String walletId) {
    return this.entityManager.createNativeQuery(
        """
          SELECT *
          FROM INV.INVESTMENT
          WHERE IDT_WALLET = :walletId
          ORDER BY IDT_INVESTMENT
          """,
        Investment.class)
      .setParameter("walletId", walletId)
      .getResultList();
  }
}

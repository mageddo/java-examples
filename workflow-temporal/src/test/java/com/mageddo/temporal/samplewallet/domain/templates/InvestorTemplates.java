package com.mageddo.temporal.samplewallet.domain.templates;

import com.mageddo.investment_product.investor.Investor;
import com.mageddo.investment_product.investor.InvestorProfile;

public class InvestorTemplates {

  public static Investor moderado(String investorId) {
    return Investor.builder()
      .id(investorId)
      .profile(InvestorProfile.MODERADO)
      .build();
  }

  public static Investor arrojado(String investorId) {
    return Investor.builder()
      .id(investorId)
      .profile(InvestorProfile.ARROJADO)
      .build();
  }
}

package com.mageddo.investment_product.investor.entrypoint;

import com.mageddo.investment_product.investor.Investor;
import com.mageddo.investment_product.investor.InvestorProfile;
import com.mageddo.investment_product.investor.InvestorService;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@RunOnVirtualThread
@Path("/jmx/investors")
@Tag(name = "investor-jmx")
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class InvestorJMX {

  final InvestorService investorService;

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  public CreateInvestorRes createInvestor() {
    final var investorId = "investor-" + UUID.randomUUID();
    this.investorService.save(Investor.builder()
      .id(investorId)
      .profile(InvestorProfile.MODERADO)
      .build());
    return CreateInvestorRes.builder()
      .investorId(investorId)
      .build();
  }

  @Builder
  public record CreateInvestorRes(
    String investorId
  ) {
  }
}

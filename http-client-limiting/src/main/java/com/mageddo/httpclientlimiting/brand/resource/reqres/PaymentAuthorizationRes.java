package com.mageddo.httpclientlimiting.brand.resource.reqres;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentAuthorizationRes {
  private Boolean authorized;
}

package com.mageddo.httpclientlimiting.checkout.brand;

import com.mageddo.httpclientlimiting.checkout.Payment;

public interface BrandClient {
  boolean authorize(Payment paymentReq);
}

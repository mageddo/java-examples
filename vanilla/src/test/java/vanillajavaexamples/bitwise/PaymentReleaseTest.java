package vanillajavaexamples.bitwise;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static vanillajavaexamples.bitwise.PaymentRelease.*;

class PaymentReleaseTest {

  @Test
  public void isActive_recebeNotificaoApenasNovasPaymentReleasesTest(){
    assertTrue(PaymentRelease.isActive(NEW_PAYMENT_RELEASES.getFlag(), NEW_PAYMENT_RELEASES));
    assertTrue(PaymentRelease.isActive(NEW_AND_UPDATE_PAYMENT_RELEASES.getFlag(), NEW_PAYMENT_RELEASES));
    assertFalse(PaymentRelease.isActive(NEW_PAYMENT_RELEASES.getFlag(), UPDATE_PAYMENT_RELEASES));
    assertFalse(PaymentRelease.isActive(DISABLED.getFlag(), NEW_PAYMENT_RELEASES));
  }

  @Test
  public void isActive_recebeNotificaoParaNovasEAtualizacoesDeEscrowTest(){
    assertTrue(PaymentRelease.isActive(NEW_AND_UPDATE_PAYMENT_RELEASES.getFlag(), NEW_PAYMENT_RELEASES));
    assertTrue(PaymentRelease.isActive(NEW_AND_UPDATE_PAYMENT_RELEASES.getFlag(), UPDATE_PAYMENT_RELEASES));
    assertFalse(PaymentRelease.isActive(UPDATE_PAYMENT_RELEASES.getFlag(), NEW_AND_UPDATE_PAYMENT_RELEASES));
    assertFalse(PaymentRelease.isActive(DISABLED.getFlag(), NEW_AND_UPDATE_PAYMENT_RELEASES));
  }

}

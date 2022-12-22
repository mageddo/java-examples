package vanillajavaexamples.bitwise;

public enum PaymentRelease {

  /**
   * Desativado
   */
  DISABLED(1 << 0), // 1

  /**
   * Notificar evento de novas payment releases
   */
  NEW_PAYMENT_RELEASES(1 << 1), // 2

  /**
   * Notificar evento de atualizacao de escrow
   */
  UPDATE_PAYMENT_RELEASES(1 << 2), // 4

  /**
   * Notificar evento de atualizacao de escrow e de novas payment releases
   */
  NEW_AND_UPDATE_PAYMENT_RELEASES(NEW_PAYMENT_RELEASES.getFlag() | UPDATE_PAYMENT_RELEASES.getFlag());

  private int flag;

  PaymentRelease(final int flag) {
    this.flag = flag;
  }

  /**
   * Verifica se esta feature está ativa para o valor passado.
   */
  public static boolean isActive(
      final int currentFlag, PaymentRelease wantedFeature
  ) {
    return (currentFlag & wantedFeature.getFlag()) == wantedFeature.getFlag();
  }

  public int getFlag() {
    return flag;
  }
}

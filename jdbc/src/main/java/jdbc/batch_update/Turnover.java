package jdbc.batch_update;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class Turnover {
  @NonNull
  private UUID debtor;

  @NonNull
  private UUID creditor;

  @NonNull
  private AccountType debitAccountType;

  @NonNull
  private AccountType creditAccountType;

  @NonNull
  private BigDecimal amount;
}

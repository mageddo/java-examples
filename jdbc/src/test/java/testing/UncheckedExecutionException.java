package testing;

import java.util.concurrent.ExecutionException;

public class UncheckedExecutionException extends RuntimeException {
  public UncheckedExecutionException(ExecutionException cause) {
    super(cause);
  }
}

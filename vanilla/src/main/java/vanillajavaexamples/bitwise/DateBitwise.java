package vanillajavaexamples.bitwise;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class DateBitwise {

  public static BigInteger toFlags(List<LocalDate> dates) {
    return dates
        .stream()
        .map(it -> toFlag(dates.get(0), it))
        .reduce(BigInteger::or)
        .get();
  }

  public static BigInteger toFlag(LocalDate first, LocalDate date) {
    final var days = (int) ChronoUnit.DAYS.between(first, date);
    final var v = BigInteger.ONE.shiftLeft(days);
    if (v.compareTo(BigInteger.ONE) > 0) {
      return v;
    }
    return BigInteger.ONE;
  }

  public static boolean hasFlag(BigInteger flags, BigInteger flag) {
    return flags.and(flag).equals(flag);
  }

  public static BigInteger removeFlag(BigInteger flags, BigInteger flag) {
    return flags.andNot(flag);
  }

  public static boolean hasDate(BigInteger flags, LocalDate first, LocalDate d) {
    return hasFlag(flags, toFlag(first, d));
  }

  public static BigInteger removeDate(BigInteger flags, LocalDate first, LocalDate d) {
    return removeFlag(flags, toFlag(first, d));
  }

}

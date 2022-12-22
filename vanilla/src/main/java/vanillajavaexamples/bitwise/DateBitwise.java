package vanillajavaexamples.bitwise;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class DateBitwise {

  public static long toFlags(List<LocalDate> dates) {
    return dates
        .stream()
        .map(it -> toFlag(dates.get(0), it))
        .reduce((a, b) -> a | b)
        .get();
  }

  public static long toFlag(LocalDate first, LocalDate date) {
    final var days = ChronoUnit.DAYS.between(first, date);
    return Math.max(1L, 1L << days);
//    return Integer.parseInt(date.toString().replace("-", ""));
  }

  public static boolean hasFlag(long flags, long flag) {
    return (flags & flag) == flag;
  }

  public static long removeFlag(long flags, long flag) {
    return flags & (~flag);
  }

  public static boolean hasDate(long flags, LocalDate first, LocalDate d) {
    return hasFlag(flags, toFlag(first, d));
  }

  public static long removeDate(long flags, LocalDate first, LocalDate d) {
    return removeFlag(flags, toFlag(first, d));
  }

}

package vanillajavaexamples.bitwise;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class DateBitwise {

  public static int toFlags(List<LocalDate> dates) {
    return dates
        .stream()
        .map(it -> toFlag(dates.get(0), it))
        .reduce((a, b) -> a | b)
        .get();
  }

  public static int toFlag(LocalDate first, LocalDate date) {
    final var days = ChronoUnit.DAYS.between(first, date);
    return Math.max(1, 1 << days);
//    return Integer.parseInt(date.toString().replace("-", ""));
  }

  public static boolean hasFlag(int flags, int flag) {
    return (flags & flag) == flag;
  }

  public static int removeFlag(int flags, int flag) {
    return flags & (~flag);
  }

  public static boolean hasDate(int flags, LocalDate first, LocalDate d) {
    return hasFlag(flags, toFlag(first, d));
  }

  public static int removeDate(int flags, LocalDate first, LocalDate d) {
    return removeFlag(flags, toFlag(first, d));
  }

}

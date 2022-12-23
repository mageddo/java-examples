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
    return toFlag((int) ChronoUnit.DAYS.between(first, date));
  }

  public static BigInteger toFlag(int idx) {
    final var v = BigInteger.ONE.shiftLeft(idx);
    if (v.compareTo(BigInteger.ONE) > 0) {
      return v;
    }
    return BigInteger.ONE;
  }

  /**
   * Removes a flag from the flag haystack.
   *
   * @return a new flag haystack without the removed flag.
   */
  public static BigInteger removeFlag(BigInteger flags, BigInteger flag) {
    return flags.andNot(flag);
  }

  /**
   * Removes the respective for the d date calculating it's position using the first date.
   *
   * It only will work if all the dates between first and d were stored to the flags, example,
   * if 2021-01-01,2021-01-02,2021-01-03 were stored and you are removing 2021-01-02 then pass it
   * as d and 2021-01-01 as first.
   *
   * @param first the first date stored at the flags
   * @param d the data to be removed
   *
   */
  public static BigInteger removeFlag(BigInteger flags, LocalDate first, LocalDate d) {
    return removeFlag(flags, toFlag(first, d));
  }

  /**
   * Removes a flag with the passed index from the haystack, it's useful when storing
   * dates or objects that are not sequential, then you can put them to a list and use the index
   * here, example consider (Apple, Orange, Pineapple, Grape), if you pass idx=2 it will
   * remove the flag 4 which is Pineapple.
   *
   * <code><pre>
   *   final int Apple = 1, Orange = Apple << 1, Pineapple = Orange << 1, Grape = Pineapple << 1;
   *   final var flags = BigInteger.valueOf(Apple | Orange | Apple | Pineapple | Grape);
   *   System.out.printf(
   *       "Apple=%d, Orange=%d, Pineapple=%d, Grape=%d%n",
   *       Apple, Orange, Pineapple, Grape
   *   );
   *   System.out.println("hasPineapple=" + hasFlag(flags, BigInteger.valueOf(Pineapple)));
   *   final var resultFlags = removeFlag(flags, 2);
   *   System.out.println("hasPineapple=" + hasFlag(resultFlags, BigInteger.valueOf(Pineapple)));
   *
   *   // Apple=1, Orange=2, Pineapple=4, Grape=8
   *   // hasPineapple=true
   *   // hasPineapple=false
   * </pre></code>
   */
  public static BigInteger removeFlag(BigInteger flags, int idx) {
    return removeFlag(flags, toFlag(idx));
  }

  public static boolean hasFlag(BigInteger flags, int idx) {
    return hasFlag(flags, toFlag(idx));
  }

  public static boolean hasFlag(BigInteger flags, LocalDate first, LocalDate d) {
    return hasFlag(flags, toFlag(first, d));
  }

  public static boolean hasFlag(BigInteger flags, BigInteger flag) {
    return flags.and(flag).equals(flag);
  }

}

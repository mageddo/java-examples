package vanillajavaexamples.bitwise;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import vanillajavaexamples.gzip.GzipUtils;
import static vanillajavaexamples.gzip.GzipUtils.gUnzip;

@Slf4j
class DateBitwiseTest {

  public static final int DAYS = 1000;
  public static final LocalDate START_AT = LocalDate.parse("2021-03-03");

  @Test
  void mustAddDaysAndAllTestTrue() {

    // arrange
    final var days = this.createDaysRange();
    final var flags = DateBitwise.toFlags(days);

    // act
    // assert
    assertEquals(days.size(), DAYS);
    days.forEach(it -> {
      assertTrue(DateBitwise.hasDate(flags, START_AT, it));
    });
  }

  @Test
  void mustAddDaysAndRandomlyRemoveEach() {

    // arrange
    final var days = this.createDaysRange();
    var flags = DateBitwise.toFlags(days);

    // act
    // assert
    final var haystack = new HashSet<>(days);
    int removed = 0;
    while (!haystack.isEmpty()) {

      final var randomDate = days.get(randomPos());
      final var hasDate = DateBitwise.hasDate(flags, START_AT, randomDate);

      if (hasDate) {
        flags = DateBitwise.removeFlag(flags, START_AT, randomDate);
        haystack.remove(randomDate);
        removed++;
      }
      assertFalse(DateBitwise.hasDate(flags, START_AT, randomDate));

      for (final var date : haystack) {
        final var msg = String.format(
            "removed=%d, randomDate=%s, flags=%d, date=%s",
            removed, randomDate, flags, date
        );
        assertTrue(DateBitwise.hasDate(flags, START_AT, date), msg);
      }
    }
    System.out.printf("status=finished, removed=%d%n", removed);

  }

  @Test
  void validateBytesUsed() {
    // arrange
    final var days = this.createDaysRange();
    final var flags = DateBitwise.toFlags(days);

    // act
    // assert

    // bytes
    final var flagsBytes = flags.toByteArray();
    assertEquals(126, flagsBytes.length);
    System.out.printf("length=%d, bytes=...%n", flagsBytes.length);

    // raw string
    final var str = flags.toString();
    assertEquals(302, str.length(), str);
    System.out.printf("length=%d, rawString=%s%n", str.length(), str);

    // using radix 36
    {
      final var hexStr = flags.toString(Character.MAX_RADIX);
      assertEquals(194, hexStr.length(), hexStr);
      assertEquals(flags, new BigInteger(hexStr, Character.MAX_RADIX));
      System.out.printf("length=%d, radix36=%s%n", hexStr.length(), hexStr);
    }

    // base 64
    {
      final var base64 = Base64.encodeBase64String(flags.toByteArray());
      assertEquals(168, base64.length());
      System.out.printf("length=%d, base64=%s%n", base64.length(), base64);
    }

    // gzip + base64
    {
      final var gzipBase64 = GzipUtils.gzipToBase64(flags.toByteArray());
      System.out.printf("length=%d, gzipBase64=%s%n", gzipBase64.length(), gzipBase64);
      assertEquals(32, gzipBase64.length());
      assertEquals(flags, new BigInteger(gUnzip(gzipBase64)));
    }
  }

  @Test
  void maxBytesNeedleToStoreFlags() {

    // arrange
    final var days = this.createDaysRange();
    var flags = DateBitwise.toFlags(days);
    int max = Integer.MIN_VALUE;

    // act
    // assert

    // gzip + base64
    {
      for (int i = 0; i < days.size(); i++) {
        final var day = days.get(i);

        assertTrue(DateBitwise.hasDate(flags, START_AT, day));
        flags = DateBitwise.removeFlag(flags, START_AT, day);
        assertFalse(DateBitwise.hasDate(flags, START_AT, day));

        final var gzipBase64 = GzipUtils.gzipToBase64(flags.toByteArray());
        max = Math.max(max, gzipBase64.length());
        System.out.printf(
            "length=%d, i=%d, gzipBase64=%s%n",
            gzipBase64.length(), i + 1, gzipBase64
        );
      }
    }
    assertEquals(40, max);
  }

  @Test
  void timeTest() {
    final var stopWatch = StopWatch.createStarted();
    final var days = this.createDaysRange();
    final var flags = DateBitwise.toFlags(days);
    assertTrue(DateBitwise.hasDate(flags, START_AT, START_AT));
    final var newFlags = DateBitwise.removeFlag(flags, START_AT, START_AT);
    assertFalse(DateBitwise.hasDate(newFlags, START_AT, START_AT));
    assertTrue(stopWatch.getTime() < 300);
  }

  int randomPos() {
    return (int) (Math.random() * DAYS);
  }

  List<LocalDate> createDaysRange() {
    return IntStream
        .range(0, DAYS)
        .boxed()
        .map(START_AT::plusDays)
        .collect(Collectors.toList());
  }

}

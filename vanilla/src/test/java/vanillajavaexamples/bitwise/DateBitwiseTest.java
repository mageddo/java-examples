package vanillajavaexamples.bitwise;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class DateBitwiseTest {

  public static final int DAYS = 64;
  public static final LocalDate START_AT = LocalDate.parse("2021-03-03");

  @Test
  void mustAdd720DaysAndAllTestTrue() {

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
  void mustAdd720DaysAndRandomlyRemoveEach() {

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
        flags = DateBitwise.removeDate(flags, START_AT, randomDate);
        haystack.remove(randomDate);
        removed++;
      }
      assertFalse(DateBitwise.hasDate(flags, START_AT, randomDate));

      for (final var date : haystack) {
        assertTrue(
            DateBitwise.hasDate(flags, START_AT, date),
            String.format(
                "removed=%d, randomDate=%s, flags=%d, date=%s",
                removed, randomDate, flags, date
            )
        );
      }

    }

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

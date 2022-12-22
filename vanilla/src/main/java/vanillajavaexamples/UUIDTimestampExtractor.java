package vanillajavaexamples;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.UUID;

import com.fasterxml.uuid.Generators;

public class UUIDTimestampExtractor {

  public static final long BASE_CONVERT_1OO_NANO_TO_MILLIS = 10_000L;

  public static void main(String[] args) {

    final var v1Uuid = Generators
        .timeBasedGenerator()
        .generate();

    System.out.println(findUuidInstant(v1Uuid).atZone(ZoneId.systemDefault()));

  }

  /**
   * https://stackoverflow.com/a/15179513/2979435
   *
   * @return the timestamp of the uuid in UTC.
   */
  static Instant findUuidInstant(UUID v1Uuid) {

    final var uuidTimestampCountsFrom = LocalDate.parse("1582-10-15");
    final var javaTimestampCountsFrom = LocalDate.parse("1970-01-01");

    final var millisBetweenUuidAndJavaTimestamp =
        toMillis(javaTimestampCountsFrom) - toMillis(uuidTimestampCountsFrom);

    final long timestampInMillis = v1Uuid.timestamp() / BASE_CONVERT_1OO_NANO_TO_MILLIS;
    final long javaTimestampInMillis = timestampInMillis - millisBetweenUuidAndJavaTimestamp;

    return Instant.ofEpochMilli(javaTimestampInMillis);
  }

  static long toMillis(LocalDate date) {
    return date
        .atStartOfDay()
        .toInstant(ZoneOffset.UTC)
        .toEpochMilli();
  }
}

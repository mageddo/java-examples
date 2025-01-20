package vanillajavaexamples.month_name_local_date_time;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LocalDateTimeMonthNameParseTest {

  @Test
  void mustParseLocalDateTimeWithMonthName() {
    final var countryLocale = Locale.forLanguageTag("pt-BR");
    final var formatter = new DateTimeFormatterBuilder()
        .parseCaseInsensitive()
        .append(DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy"))
        .toFormatter(countryLocale);

    assertEquals(
        LocalDate.parse("2024-12-31"),
        LocalDate.parse("31 de Dezembro de 2024", formatter)
    );
  }
}

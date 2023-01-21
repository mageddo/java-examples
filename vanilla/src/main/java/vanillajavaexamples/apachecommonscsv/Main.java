package vanillajavaexamples.apachecommonscsv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;

public class Main {
  public static void main(String[] args) throws IOException {
    final var csvPath = Paths.get("/home/typer/Downloads/csv2jdbc/1GB.csv");
    try (var parser = createCsvParser(csvPath)) {

      final var headersMap = new LinkedHashMap<String, Integer>();
      final var headers = parser.getHeaderNames();

      for (CSVRecord record : parser) {
        for (String header : headers) {
          headersMap.compute(header, (k, v) -> {
            final var len = getLength(record.get(header));
            if (v == null) {
              return len;
            }
            return Math.max(v, len);
          });
        }
      }
      System.out.println(headersMap);
    }
    System.out.println("all read");
//    createAndReadCsv();
  }

  static int getLength(String s) {
    return s == null ? 255 : s.length();
  }

  private static void createAndReadCsv() throws IOException {
    final var csvPath = Files.createTempFile("tmp", ".csv");
    try (var printer = createCsvPrinter(csvPath)) {
      printer.printRecord(List.of("version", "name", "age"));
      printer.printRecord(List.of("v1", "John", "23"));
    }

    try (var parser = createCsvParser(csvPath)) {
      System.out.println(parser.getHeaderNames());
      for (CSVRecord record : parser) {
        System.out.println(record.toString());
      }
    }
    Files.delete(csvPath);
  }

  static CSVParser createCsvParser(Path csvPath) throws IOException {
    final CSVFormat csvFormat = buildCsvFormat();
    return CSVParser.parse(csvPath, StandardCharsets.UTF_8, csvFormat);
  }

  static CSVPrinter createCsvPrinter(Path csvPath) throws IOException {
    return buildCsvFormat()
        .builder()
        .build()
        .print(csvPath, StandardCharsets.UTF_8)
        ;
  }

  private static CSVFormat buildCsvFormat() {
    return CSVFormat.Builder
        .create(CSVFormat.DEFAULT)
        .setHeader()
        .setSkipHeaderRecord(true)
        .setDelimiter(',')
        .build();
  }

}

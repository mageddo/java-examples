package com.mageddo.beanio.csvexporter;

import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.util.List;

import com.mageddo.beanio.csvexporter.exporter.StatementsExporter;
import com.mageddo.beanio.csvexporter.vo.StatementCsv;
import com.mageddo.beanio.csvexporter.vo.StatementDetailsCsv;

public class CsvExporterMain {
  public static void main(String[] args) {

    final var statement = new StatementCsv()
        .setAmount(BigDecimal.TEN)
        .setDescription("Sold $10.00")
        .setDetails(List.of(
            new StatementDetailsCsv()
            .setDescription("Brahma 350ml")
            .setProductId(1233242342211L)
        ));

    final var out = new OutputStreamWriter(System.out);
    final var beanWriter = new StatementsExporter().createWriter(out);
    try {
      beanWriter.write(statement);
      beanWriter.write(statement);
    } finally {
      beanWriter.close();
    }
  }
}

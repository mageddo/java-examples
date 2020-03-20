package com.mageddo.beanio.csvexporter;

import java.io.OutputStreamWriter;
import java.math.BigDecimal;

import com.mageddo.beanio.csvexporter.exporter.StatementsExporter;
import com.mageddo.beanio.csvexporter.vo.StatementCsv;

public class CsvExporterMain {
  public static void main(String[] args) {

    final var statement = new StatementCsv()
        .setAmount(BigDecimal.TEN)
        .setDescription("Sold $10.00");

    final var out = new OutputStreamWriter(System.out);
    final var beanWriter = new StatementsExporter().createWriter(out);

    beanWriter.write(statement);
    beanWriter.write(statement);

    beanWriter.flush();
  }
}

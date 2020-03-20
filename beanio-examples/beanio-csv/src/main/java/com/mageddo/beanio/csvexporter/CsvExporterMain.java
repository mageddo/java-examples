package com.mageddo.beanio.csvexporter;

import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.List;

import com.mageddo.beanio.csvexporter.factory.StatementsCsvFactory;
import com.mageddo.beanio.csvexporter.vo.StatementCsv;
import com.mageddo.beanio.csvexporter.vo.StatementDetailsCsv;
import com.mageddo.beanio.csvexporter.vo.SubList;

public class CsvExporterMain {

  public static void main(String[] args) {

    final var statement = new StatementCsv()
        .setAmount(BigDecimal.TEN)
        .setDescription("Sold $10.00")
        .setDetails(SubList.of(List.of(
            new StatementDetailsCsv()
                .setDescription("Brahma 350ml")
                .setProductId(1233242342211L),
            new StatementDetailsCsv()
                .setDescription("Skol 269ml")
                .setProductId(9809866849864L)
            ))
        );

    // writing
    final var statementsCsvFactory = new StatementsCsvFactory();
    final var stringWriter = new StringWriter();
    final var beanWriter = statementsCsvFactory.createWriter(stringWriter);
    try {
      beanWriter.write(statement);
      beanWriter.write(statement);
    } finally {
      beanWriter.close();
    }
    System.out.println(stringWriter.toString());

    // reading
    final var stringReader = new StringReader(stringWriter.toString());
    final var beanReader = statementsCsvFactory.createReader(stringReader);
    try {
      while (beanReader.getLineNumber() != -1) {
        final var o = beanReader.read();
        if (o == null) {
          continue;
        }
        System.out.println(o);
      }
    } finally {
      beanReader.close();
    }

  }
}

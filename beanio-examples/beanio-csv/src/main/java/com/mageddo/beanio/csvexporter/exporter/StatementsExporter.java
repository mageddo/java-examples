package com.mageddo.beanio.csvexporter.exporter;

import com.mageddo.beanio.csvexporter.handler.ListHandler;
import com.mageddo.beanio.csvexporter.vo.StatementCsv;

import com.mageddo.beanio.csvexporter.vo.StatementDetailsCsv;

import org.beanio.BeanWriter;
import org.beanio.StreamFactory;
import org.beanio.builder.CsvParserBuilder;
import org.beanio.builder.StreamBuilder;
import org.beanio.stream.RecordParserFactory;

import java.io.Writer;
import java.util.List;

public final class StatementsExporter {

  private final StreamFactory factory;

  public StatementsExporter() {
    final RecordParserFactory parserFactory = new CsvParserBuilder()
        .delimiter(';')
        .recordTerminator("\r\n")
        .build()
        .getInstance();

    final StreamBuilder builder = new StreamBuilder(getClass().getName())
        .format("csv")
        .parser(parserFactory)
        .addTypeHandler(ListHandler.class.getName(), new ListHandler())
        .addRecord(StatementCsv.class)
        .addRecord(StatementDetailsCsv.class)
        ;

    factory = StreamFactory.newInstance();
    factory.define(builder);
  }

  public BeanWriter createWriter(Writer w) {
    return this.factory.createWriter(getClass().getName(), w);
  }
}

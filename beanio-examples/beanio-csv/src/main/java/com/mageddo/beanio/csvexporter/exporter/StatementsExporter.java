package com.mageddo.beanio.csvexporter.exporter;

import com.mageddo.beanio.csvexporter.vo.StatementCsv;

import org.beanio.BeanWriter;
import org.beanio.StreamFactory;
import org.beanio.builder.CsvParserBuilder;
import org.beanio.builder.StreamBuilder;
import org.beanio.stream.RecordParserFactory;

import java.io.Writer;

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
        .addRecord(StatementCsv.class);

    factory = StreamFactory.newInstance();
    factory.define(builder);
  }

  public BeanWriter createWriter(Writer w){
    return this.factory.createWriter(getClass().getName(), w);
  }
}

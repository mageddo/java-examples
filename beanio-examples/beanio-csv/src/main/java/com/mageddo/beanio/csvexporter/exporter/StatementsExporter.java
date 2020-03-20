package com.mageddo.beanio.csvexporter.exporter;

import java.io.Writer;

import com.mageddo.beanio.csvexporter.handler.SubListHandler;
import com.mageddo.beanio.csvexporter.vo.StatementCsv;

import org.beanio.BeanWriter;
import org.beanio.StreamFactory;
import org.beanio.builder.CsvParserBuilder;
import org.beanio.builder.StreamBuilder;
import org.beanio.stream.RecordParserFactory;

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
        .addTypeHandler(SubListHandler.class.getName(), new SubListHandler())
        .addRecord(StatementCsv.class)
        ;

    factory = StreamFactory.newInstance();
    factory.define(builder);
  }

  public BeanWriter createWriter(Writer w) {
    return this.factory.createWriter(getClass().getName(), w);
  }
}

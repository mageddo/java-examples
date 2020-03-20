package com.mageddo.beanio.csvexporter.factory;

import java.io.Reader;
import java.io.Writer;

import com.mageddo.beanio.csvexporter.vo.StatementDetailsCsv;

import org.beanio.BeanReader;
import org.beanio.BeanWriter;
import org.beanio.StreamFactory;
import org.beanio.builder.CsvParserBuilder;
import org.beanio.builder.StreamBuilder;
import org.beanio.stream.RecordParserFactory;

public final class SubListCsvFactory {

  private final StreamFactory factory;

  public SubListCsvFactory() {
    final RecordParserFactory parserFactory = new CsvParserBuilder()
        .delimiter(';')
        .recordTerminator("|")
        .build()
        .getInstance();

    final StreamBuilder builder = new StreamBuilder(getClass().getName())
        .format("csv")
        .parser(parserFactory)
        .addRecord(StatementDetailsCsv.class)
        ;

    this.factory = StreamFactory.newInstance();
    this.factory.define(builder);
  }

  public BeanWriter createWriter(Writer w) {
    return this.factory.createWriter(getClass().getName(), w);
  }

  public BeanReader createReader(Reader r) {
    return this.factory.createReader(getClass().getName(), r);
  }
}

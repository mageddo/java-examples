package com.mageddo.beanio.csvexporter.factory;

import java.io.Reader;
import java.io.Writer;

import com.mageddo.beanio.csvexporter.handler.SubListHandler;
import com.mageddo.beanio.csvexporter.vo.StatementCsv;

import org.beanio.BeanReader;
import org.beanio.BeanWriter;
import org.beanio.StreamFactory;
import org.beanio.builder.CsvParserBuilder;
import org.beanio.builder.StreamBuilder;
import org.beanio.stream.RecordParserFactory;

public final class StatementsCsvFactory {

  private final StreamFactory factory;

  public StatementsCsvFactory() {
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

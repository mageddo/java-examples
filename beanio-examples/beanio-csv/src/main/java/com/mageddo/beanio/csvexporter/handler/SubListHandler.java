package com.mageddo.beanio.csvexporter.handler;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UncheckedIOException;

import com.mageddo.beanio.csvexporter.factory.SubListCsvFactory;
import com.mageddo.beanio.csvexporter.vo.SubList;

import org.beanio.types.TypeConversionException;
import org.beanio.types.TypeHandler;

public class SubListHandler implements TypeHandler {

  private SubListCsvFactory subListCsvFactory = new SubListCsvFactory();

  @Override
  public Object parse(String text) throws TypeConversionException {
    final var stringReader = new StringReader(text.replaceAll("\\|", "\r\n"));
    final var reader = subListCsvFactory.createReader(stringReader);
    try {
      final var subList = new SubList<>();
      while (reader.getLineNumber() != -1){
        final var o = reader.read();
        if(o == null) {
          continue;
        }
        subList.add(o);
      }
      return subList;
    } finally {
      reader.close();
    }
  }

  @Override
  public String format(Object value) {
    if (value == null) {
      return null;
    }
    final var subList = (SubList<?>) value;
    final var stringWriter = new StringWriter();
    final var beanWriter = this.subListCsvFactory.createWriter(stringWriter);
    try {
      for (Object item : subList.getItems()) {
        beanWriter.write(item);
      }
    } finally {
      try {
        stringWriter.close();
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    }
    return stringWriter.toString();
  }

  @Override
  public Class<?> getType() {
    return SubList.class;
  }
}

package com.mageddo.beanio.csvexporter.handler;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.util.Collection;
import java.util.List;

import com.mageddo.beanio.csvexporter.exporter.SubListExporter;
import com.mageddo.beanio.csvexporter.vo.SubList;

import org.beanio.BeanWriter;
import org.beanio.types.TypeConversionException;
import org.beanio.types.TypeHandler;

public class SubListHandler implements TypeHandler {

  private SubListExporter subListExporter = new SubListExporter();

  @Override
  public Object parse(String text) throws TypeConversionException {
    throw new UnsupportedOperationException();
  }

  @Override
  public String format(Object value) {
    if (value == null) {
      return null;
    }
//    if(!(value instanceof List)){
//      return value.toString();
//    }
//    StringBuilder sb = new StringBuilder();
//    for (Object o : ((Collection) value)) {
//      sb.append(o);
//      sb.append("|");
//    }
//    if (sb.length() > 0) {
//      sb.delete(sb.length() - 1, sb.length());
//    }
    final var subList = (SubList) value;
    final var stringWriter = new StringWriter();
    final var beanWriter = this.subListExporter.createWriter(stringWriter);
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

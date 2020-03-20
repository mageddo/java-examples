package com.mageddo.beanio.csvexporter.handler;

import com.mageddo.beanio.csvexporter.vo.StatementDetailsCsv;

import org.beanio.types.TypeConversionException;
import org.beanio.types.TypeHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListHandler implements TypeHandler {

  @Override
  public Object parse(String text) throws TypeConversionException {
    throw new UnsupportedOperationException();
  }

  @Override
  public String format(Object value) {
    if (value == null) {
      return null;
    }
    StringBuilder sb = new StringBuilder();
    for (Object o : ((Collection) value)) {
      sb.append(o);
      sb.append("|");
    }
    if (sb.length() > 0) {
      sb.delete(sb.length() - 1, sb.length());
    }
    return sb.toString();
  }

  @Override
  public Class<?> getType() {
    return StatementDetailsCsv.class;
  }
}

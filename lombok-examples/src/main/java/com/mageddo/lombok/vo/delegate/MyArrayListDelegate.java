package com.mageddo.lombok.vo.delegate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lombok.Getter;
import lombok.experimental.Delegate;

public class MyArrayListDelegate<T> implements List<T> {

  @Getter
  private int addItems = 0;

  @Delegate(excludes = ListAdd.class)
  private List<T> delegate = new ArrayList<>();

  @Override
  public boolean add(T e) {
    this.addItems++;
    return this.delegate.add(e);
  }

  @Override
  public boolean addAll(Collection<? extends T> c) {
    this.addItems += c.size();
    return this.delegate.addAll(c);
  }

  private interface ListAdd {
    <T> boolean add(T e);

    <T> boolean addAll(Collection<? extends T> c);
  }
}

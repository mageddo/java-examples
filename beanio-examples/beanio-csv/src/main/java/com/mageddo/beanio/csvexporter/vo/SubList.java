package com.mageddo.beanio.csvexporter.vo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SubList<T> {

  private final List<T> items;

  public SubList(){
    this(new ArrayList<>());
  }

  public SubList(List<T> items) {
    this.items = items;
  }

  public SubList<T> add(T o){
    this.items.add(o);
    return this;
  }

  public int size(){
    return this.items.size();
  }

  public List<Object> getItems() {
    return Collections.unmodifiableList(this.items);
  }

  public static <T>SubList<T> of(List<T> o){
    return new SubList<>(o);
  }

  @Override
  public String toString() {
    return this.items.toString();
  }
}

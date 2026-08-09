package com.mageddo.persistence;

public interface GenericDAO<Bean> {

  boolean createIfAbsent(Bean bean);

  boolean save(Bean bean);

  Bean mustFind(Object id, Class<Bean> beanClass);

}

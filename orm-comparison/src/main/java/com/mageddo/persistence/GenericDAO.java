package com.mageddo.persistence;

public interface GenericDAO<Bean> {

  boolean createIfAbsent(Bean bean);

  boolean save(Bean bean);

  Bean find(Object id, Class<Bean> beanClass);

  Bean mustFind(Object id, Class<Bean> beanClass);

}

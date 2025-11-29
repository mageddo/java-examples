package com.mageddo.javax.naming;

import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NameClassPair;
import javax.naming.NameNotFoundException;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;

public class InMemoryContext implements Context {

  private static final Map<String, Object> REGISTRY = new ConcurrentHashMap<>();

  @Override
  public Object lookup(String name) throws NamingException {
    var obj = REGISTRY.get(name);
    if (obj == null) {
      throw new NameNotFoundException("Name not found: " + name);
    }
    return obj;
  }

  @Override
  public void bind(String name, Object obj) throws NamingException {
    if (REGISTRY.putIfAbsent(name, obj) != null) {
      throw new NameAlreadyBoundException("Already bound: " + name);
    }
  }

  @Override
  public void rebind(String name, Object obj) {
    REGISTRY.put(name, obj);
  }

  @Override
  public void unbind(String name) {
    REGISTRY.remove(name);
  }

  @Override
  public void close() {
  }

  @Override
  public Object lookup(Name name) throws NamingException {
    return lookup(name.toString());
  }

  @Override
  public void bind(Name name, Object obj) throws NamingException {
    bind(name.toString(), obj);
  }

  @Override
  public void rebind(Name name, Object obj) throws NamingException {
    rebind(name.toString(), obj);
  }

  @Override
  public void unbind(Name name) throws NamingException {
    unbind(name.toString());
  }

  @Override
  public void rename(String oldName, String newName) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void rename(Name oldName, Name newName) {
    throw new UnsupportedOperationException();
  }

  @Override
  public NamingEnumeration<NameClassPair> list(String name) {
    throw new UnsupportedOperationException();
  }

  @Override
  public NamingEnumeration<NameClassPair> list(Name name) {
    throw new UnsupportedOperationException();
  }

  @Override
  public NamingEnumeration<Binding> listBindings(String name) {
    throw new UnsupportedOperationException();
  }

  @Override
  public NamingEnumeration<Binding> listBindings(Name name) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void destroySubcontext(String name) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void destroySubcontext(Name name) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Context createSubcontext(String name) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Context createSubcontext(Name name) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Object lookupLink(String name) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Object lookupLink(Name name) {
    throw new UnsupportedOperationException();
  }

  @Override
  public NameParser getNameParser(String name) {
    throw new UnsupportedOperationException();
  }

  @Override
  public NameParser getNameParser(Name name) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Name composeName(Name name, Name prefix) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String composeName(String name, String prefix) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Object addToEnvironment(String propName, Object propVal) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Object removeFromEnvironment(String propName) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Hashtable<?, ?> getEnvironment() {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getNameInNamespace() {
    throw new UnsupportedOperationException();
  }
}

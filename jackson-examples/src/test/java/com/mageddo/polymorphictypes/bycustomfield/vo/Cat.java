package com.mageddo.polymorphictypes.bycustomfield.vo;

public class Cat implements Animal {

  private String name;
  private String favoriteToy;

  public Cat() {
  }

  public Cat(String name, String favoriteToy) {
    setName(name);
    setFavoriteToy(favoriteToy);
  }

  public String getFavoriteToy() {
    return favoriteToy;
  }

  public void setFavoriteToy(String favoriteToy) {
    this.favoriteToy = favoriteToy;
  }

  @Override
  public String getName() {
    return name;
  }

  public Cat setName(String name) {
    this.name = name;
    return this;
  }
}

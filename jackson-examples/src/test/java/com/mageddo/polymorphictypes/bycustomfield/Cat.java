package com.mageddo.polymorphictypes.bycustomfield;

public class Cat extends Animal {

  public String getFavoriteToy() {
    return favoriteToy;
  }

  public Cat() {
  }

  public Cat(String name, String favoriteToy) {
    setName(name);
    setFavoriteToy(favoriteToy);
  }

  public void setFavoriteToy(String favoriteToy) {
    this.favoriteToy = favoriteToy;
  }

  private String favoriteToy;

}

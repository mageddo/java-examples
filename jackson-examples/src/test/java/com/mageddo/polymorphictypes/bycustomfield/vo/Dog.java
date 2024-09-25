package com.mageddo.polymorphictypes.bycustomfield.vo;

public class Dog implements Animal {

    private String name;
    private String breed;

  public Dog() {
  }

  public Dog(String name, String breed) {
        setName(name);
        setBreed(breed);
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

  @Override
  public String getName() {
    return name;
  }

  public Dog setName(String name) {
    this.name = name;
    return this;
  }
}

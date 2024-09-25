package com.mageddo.polymorphictypes.bycustomfield;

public class Dog extends Animal {

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
}

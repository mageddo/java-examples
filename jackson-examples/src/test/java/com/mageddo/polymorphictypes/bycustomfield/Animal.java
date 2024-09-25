package com.mageddo.polymorphictypes.bycustomfield;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY, property = "__name",
    visible = true
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Dog.class, name = "Dog"),
    @JsonSubTypes.Type(value = Cat.class, name = "Cat")
})
public abstract class Animal {

  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}

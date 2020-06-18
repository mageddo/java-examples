package com.mageddo.jvmti.classdelegate.scanning;

import com.mageddo.jvmti.classdelegate.ObjectReference;
import com.mageddo.jvmti.classdelegate.scanning.rules.EqualToRule;
import com.mageddo.jvmti.classdelegate.scanning.rules.GreaterEqualThanRule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReferenceFilterFactoryTest {

  final ReferenceFilterFactory referenceFilterFactory = new ReferenceFilterFactory();

  @Test
  void mustFindPeople20YearsOld(){
    // arrange
    final List<ObjectReference> people = new ArrayList<>(Arrays.asList(
      ObjectReference.of(new Person(20)),
      ObjectReference.of(new Person(21)),
      ObjectReference.of(new Person(19)),
      ObjectReference.of(new Person(20))
    ));

    // act
    this.referenceFilterFactory.filter(
      people,
      new InstanceFilter()
      .addFilter(FieldFilter
        .builder()
        .fieldName("age")
        .addRule(new EqualToRule("20"))
        .build()
      )

    );

    // assert
    assertEquals(2, people.size());
  }

  @Test
  void mustFindPeopleAtLeast20YearsOld(){
    // arrange
    final List<ObjectReference> people = new ArrayList<>(Arrays.asList(
      ObjectReference.of(new Person(20)),
      ObjectReference.of(new Person(21)),
      ObjectReference.of(new Person(19)),
      ObjectReference.of(new Person(20))
    ));

    // act
    this.referenceFilterFactory.filter(
      people,
      new InstanceFilter()
        .addFilter(FieldFilter
          .builder()
          .fieldName("age")
          .addRule(new GreaterEqualThanRule(20))
          .build()
        )

    );

    // assert
    assertEquals(3, people.size());
  }

  @Test
  void mustCheckName(){
    // arrange
    final List<ObjectReference> people = new ArrayList<>(Arrays.asList(
      ObjectReference.of(new PersonV2("Uncle", "Bob")),
      ObjectReference.of(new PersonV2("Sara", "Graham")),
      ObjectReference.of(new PersonV2("Barack", "Obama"))
    ));

    // act
    this.referenceFilterFactory.filter(
      people,
      new InstanceFilter()
        .addFilter(MethodFilter
          .builder()
          .methodName("fullName")
          .addRule(new EqualToRule("Uncle Bob"))
          .build()
        )

    );

    // assert
    assertEquals(1, people.size());
  }

  @Getter
  @AllArgsConstructor
  static class Person {
    int age;
  }

  @Getter
  @AllArgsConstructor
  static class PersonV2 {

    String firstName;
    String lastName;

    public String fullName(){
      return String.format("%s %s", this.firstName, this.lastName);
    }
  }
}
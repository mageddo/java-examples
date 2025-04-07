package com.mageddo.lombok.vo.utils;

import java.lang.reflect.Modifier;

import org.junit.Test;

import lombok.val;
import static org.junit.Assert.assertTrue;

public class StringUtilsTest {

  @Test
  public void utilityClassMustBeFinalAndHavePrivateConstructor() throws Exception {

    // arrange
    val clazz = StringUtils.class;

    // act
    val defaultConstructor = clazz.getDeclaredConstructor();

    // assert
    assertTrue(Modifier.isFinal(clazz.getModifiers()));
    assertTrue(Modifier.isPublic(clazz.getModifiers()));

    assertTrue(Modifier.isPrivate(defaultConstructor.getModifiers()));

  }
}

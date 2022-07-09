package com.mageddo.class_disassembler;


import java.io.IOException;
import java.io.UncheckedIOException;

import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;

public class ClassDisassembler {

  public static String findStructureAsText(Class<?> clazz) {

    final String classPath =
        String.format(
            "/%s.class",
            clazz.getName()
                .replace('.', '/')
        );

    final ClassParser classParser = new ClassParser(
        JavaClass.class.getResourceAsStream(classPath),
        clazz.getSimpleName() + ".java"
    );


    try {
      return classParser.parse()
          .toString()
          .replaceAll("\t", "  ")
          ;
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public static void main(String[] args) {
    System.out.println(ClassDisassembler.findStructureAsText(ClassDisassembler.class));
  }

}

package com.mageddo.java14.switchexpressions;

public class SwitchExpressions {

  public String findColor(int id){
    final var colorName = switch (id){
      case 1 -> "Grape";
      case 2 -> "Orange";
      default -> throw new UnsupportedOperationException("Unknown color");
    };
    return colorName;
  }

  public int calc(String operator, int a, int b){
    return switch (operator){
      case "+" -> {
        System.out.println("sum operation was chose");
        yield  a + b;
      }
      case "-" -> a - b;
      default -> throw new UnsupportedOperationException("Unknown operator");
    };
  }

}

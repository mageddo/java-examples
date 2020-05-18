package com.mageddo.java14;

import com.mageddo.java14.npeexceptions.NullPointerExceptions;
import com.mageddo.java14.switchexpressions.SwitchExpressions;

public class Java14Main {
  public static void main(String[] args) {
    System.out.println("# Switch Expressions");
    final var switchExpressions = new SwitchExpressions();

    final var sum = switchExpressions.calc("-", 10, 7);
    System.out.printf("%n## calc%n");
    System.out.printf("sum = %d%n%n", sum);

    final var color = switchExpressions.findColor(2);
    System.out.printf("## find color%n");
    System.out.printf("color = %s%n", color);

    System.out.println("# NullPointerExceptions");
    final var nullPointerExceptions = new NullPointerExceptions();
    System.out.println("getting a value in at three level map tree");
    try {
      nullPointerExceptions.treeValueFind();
    } catch (NullPointerException e){
      e.printStackTrace();
    }

  }
}

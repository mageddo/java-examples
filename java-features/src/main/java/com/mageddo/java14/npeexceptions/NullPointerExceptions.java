package com.mageddo.java14.npeexceptions;

import java.util.Map;

public class NullPointerExceptions {
  public String treeValueFind () {
    final var tree = Map.of("a", Map.of("b", Map.of("c", "You found it!")));
    return tree.get("c").get("b").get("a");
  }
}

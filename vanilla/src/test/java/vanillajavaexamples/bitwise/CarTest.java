package vanillajavaexamples.bitwise;

import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static vanillajavaexamples.bitwise.Car.ALL_FLAGS;
import static vanillajavaexamples.bitwise.Car.MULTIMEDIA;

class CarTest {
  @Test
  void mustSetFlagsWithRightValue() {
    final var flags = Car
        .getFlags()
        .stream()
        .map(Car::format)
        .collect(Collectors.joining("\n"));

    assertEquals("DOORS=1\n" +
        "WHEELS=2\n" +
        "FUEL=4\n" +
        "BATTERY=16\n" +
        "MULTIMEDIA=65536\n" +
        "ALL_FLAGS=65559", flags);
  }

  @Test
  void mustContainFlag(){
    assertTrue(Car.hasFlag(ALL_FLAGS, MULTIMEDIA));
  }

  @Test
  void mustNOTContainFlagAfterRemoveIt(){
    assertFalse(Car.hasFlag(Car.removeFlag(ALL_FLAGS, MULTIMEDIA), MULTIMEDIA));
  }
}

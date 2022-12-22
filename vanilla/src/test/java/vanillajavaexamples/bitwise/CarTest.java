package vanillajavaexamples.bitwise;

import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static vanillajavaexamples.bitwise.Car.ALL_FLAGS;
import static vanillajavaexamples.bitwise.Car.BATTERY;
import static vanillajavaexamples.bitwise.Car.FUEL;
import static vanillajavaexamples.bitwise.Car.MULTIMEDIA;
import static vanillajavaexamples.bitwise.Car.WHEELS;

class CarTest {

  @Test
  void mustSetFlagsWithRightValue() {
    final var flags = Car
        .getFlags()
        .stream()
        .map(Car::format)
        .collect(Collectors.joining("\n"));

    assertEquals("TURBO=1\n" +
        "WHEELS=2\n" +
        "FUEL=4\n" +
        "BATTERY=8\n" +
        "MULTIMEDIA=16\n" +
        "ALL_FLAGS=31", flags);
  }

  @Test
  void mustContainFlag(){
    assertTrue(Car.hasFlag(ALL_FLAGS, MULTIMEDIA));
  }

  @Test
  void mustNOTContainFlagAfterRemoveIt(){
    final var removedFlag = Car.removeFlag(ALL_FLAGS, MULTIMEDIA);
    assertFalse(Car.hasFlag(removedFlag, MULTIMEDIA));
    assertTrue(Car.hasFlag(removedFlag, WHEELS));
    assertTrue(Car.hasFlag(removedFlag, BATTERY));
    assertTrue(Car.hasFlag(removedFlag, FUEL));
  }
}

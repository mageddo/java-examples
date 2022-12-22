package vanillajavaexamples.bitwise;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.reflect.FieldUtils;

import lombok.SneakyThrows;

public class Car {

  public static final int DOORS = 1;
  public static final int WHEELS = 1 << DOORS;
  public static final int FUEL = 1 << WHEELS;
  public static final int BATTERY = 1 << FUEL;
  public static final int MULTIMEDIA = 1 << BATTERY;

  public static final int ALL_FLAGS = DOORS | WHEELS | FUEL | BATTERY | MULTIMEDIA;

  public static void main(String[] args) {
    getFlags().forEach(Car::printValue);
    System.out.println(Car.hasFlag(ALL_FLAGS, MULTIMEDIA));
    System.out.println(Car.hasFlag(Car.removeFlag(ALL_FLAGS, MULTIMEDIA), MULTIMEDIA));
  }

  public static boolean hasFlag(int flags, int flag) {
    return (flags & flag) == flag;
  }

  public static int removeFlag(int flags, int flag) {
    return flags & (~flag);
  }

  static void printValue(Field it) {
    System.out.println(format(it));
  }

  @SneakyThrows
  static String format(Field field) {
    return String.format("%s=%s", field.getName(), field.getInt(null));
  }

  public static List<Field> getFlags() {
    return FieldUtils
        .getAllFieldsList(Car.class)
        .stream()
        .filter(it -> Modifier.isStatic(it.getModifiers()))
        .collect(Collectors.toList());
  }
}

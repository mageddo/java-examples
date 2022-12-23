package vanillajavaexamples.bitwise;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.reflect.FieldUtils;

import lombok.SneakyThrows;

public class Car {

  public static final int TURBO = 1;
  public static final int WHEELS = TURBO << 1;
  public static final int FUEL = WHEELS << 1;
  public static final int BATTERY = FUEL << 1;
  public static final int MULTIMEDIA = BATTERY << 1;

  public static final int ALL_FLAGS = TURBO | WHEELS | FUEL | BATTERY | MULTIMEDIA;

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
